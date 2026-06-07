package com.projekakhir.rawatkasih.data

import com.projekakhir.rawatkasih.SupabaseClient
import com.projekakhir.rawatkasih.data.local.*
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class AppUser(
    val id: Long? = null,
    @SerialName("auth_id") val authId: String? = null,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val role: String,
    @SerialName("caregiver_id") val caregiverId: Long? = null,

    val age: Int? = null,
    val gender: String? = null,

    @SerialName("blood_type")
    val bloodType: String? = null,

    val height: Int? = null,
    val weight: Int? = null,

    val allergy: String? = null,

    @SerialName("medical_history")
    val medicalHistory: String? = null,

    @SerialName("profile_image")
    val profileImage: String? = null
)

@Serializable
data class MedicineSchedule(
    val id: Long? = null,
    @SerialName("patient_id") val patientId: Long,
    @SerialName("medicine_name") val medicineName: String,
    val dosage: String? = null,
    @SerialName("schedule_time") val scheduleTime: String,
    val note: String? = null,
    @SerialName("is_active") val isActive: Boolean = true
)

@Serializable
data class MedicineLog(
    val id: Long? = null,
    @SerialName("schedule_id") val scheduleId: Long,
    @SerialName("patient_id") val patientId: Long,
    @SerialName("taken_date") val takenDate: String,
    @SerialName("taken_at") val takenAt: String? = null,
    val status: String = "taken"
)

@Serializable
data class DailyCondition(
    val id: Long? = null,
    @SerialName("patient_id") val patientId: Long,
    val date: String,
    val condition: String,
    val mood: String,
    @SerialName("blood_pressure") val bloodPressure: String? = null,
    val notes: String? = null
)

data class AuthResult(
    val user: AppUser,
    val schedules: List<MedicineSchedule> = emptyList(),
    val logs: List<MedicineLog> = emptyList(),
    val condition: DailyCondition? = null
)

@Serializable
data class CaregiverOption(val id: Long, val name: String)

@Serializable
data class UpdateProfileRequest(
    val name: String,
    val phone: String,
    val age: Int?,
    val gender: String?
)

@Serializable
data class UpdateHealthProfileRequest(

    @SerialName("patient_id")
    val patientId: Long,

    val height: Int?,

    val weight: Int?,

    @SerialName("blood_type")
    val bloodType: String?,

    val allergy: String?,

    @SerialName("medical_history")
    val medicalHistory: String?
)

class RawatKasihRepository(private val dao: RawatKasihDao) {
    private val client = SupabaseClient.client
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    suspend fun login(email: String, password: String): AuthResult {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        val authUser = client.auth.currentSessionOrNull()?.user ?: error("Gagal login")
        val profile = loadCurrentProfile(authUser.id)
        dao.insertUser(profile.toEntity())
        
        return if (profile.role == "patient") {
            val schedules = loadMedicineSchedules(profile.id)
            val logs = loadPatientMedicineLogs(profile.id) // Konsisten dengan loadPatientMedicineLogs
            val condition = loadTodayCondition(profile.id)
            AuthResult(profile, schedules, logs, condition)
        } else {
            AuthResult(profile)
        }
    }

    suspend fun registerPatient(name: String, email: String, phone: String, password: String, caregiverId: Long) {
        val user = client.auth.signUpWith(Email) { this.email = email; this.password = password }
        val authId = user?.id ?: client.auth.currentSessionOrNull()?.user?.id
        client.from("users").insert(mapOf(
            "auth_id" to authId, "name" to name, "email" to email, 
            "phone" to phone, "role" to "patient", "caregiver_id" to caregiverId
        ))
    }

    suspend fun loadCaregivers(): List<CaregiverOption> {
        return client.from("users").select(columns = Columns.list("id", "name")) {
            filter { eq("role", "caregiver") }
        }.decodeList()
    }

    fun getPatientsFlow(caregiverId: Long): Flow<List<AppUser>> {
        return dao.getPatientsForCaregiver(caregiverId).map { list ->
            list.map { it.toModel() }
        }
    }

    suspend fun refreshCaregiverPatients(caregiverId: Long) {
        val remotePatients = client.from("users").select {
            filter { eq("caregiver_id", caregiverId); eq("role", "patient") }
        }.decodeList<AppUser>()
        remotePatients.forEach { dao.insertUser(it.toEntity()) }
    }

    suspend fun loadCaregiverPatients(caregiverId: Long): List<AppUser> {
        return client.from("users").select {
            filter { eq("caregiver_id", caregiverId); eq("role", "patient") }
        }.decodeList()
    }

    suspend fun loadUserById(userId: Long): AppUser {
        val local = dao.getUserById(userId)
        if (local != null) return local.toModel()
        val remote = client.from("users").select { filter { eq("id", userId) } }.decodeSingle<AppUser>()
        dao.insertUser(remote.toEntity())
        return remote
    }

    suspend fun loadPatientHome(user: AppUser): AuthResult {
        val schedules = loadMedicineSchedules(user.id)
        val logs = loadPatientMedicineLogs(user.id)
        val condition = loadTodayCondition(user.id)
        return AuthResult(user, schedules, logs, condition)
    }

    suspend fun saveDailyCondition(patientId: Long, cond: String, mood: String, bp: String, notes: String) {
        val condition = DailyCondition(patientId = patientId, date = today(), condition = cond, mood = mood, bloodPressure = bp, notes = notes)
        client.from("daily_conditions").insert(condition)
        dao.insertCondition(condition.toEntity())
    }

    suspend fun saveMedicineSchedule(schedule: MedicineSchedule) {
        client.from("medicine_schedules").insert(schedule)
    }

    suspend fun loadMedicineSchedules(patientId: Long?): List<MedicineSchedule> {
        if (patientId == null) return emptyList()
        val remote = client.from("medicine_schedules").select { filter { eq("patient_id", patientId) } }.decodeList<MedicineSchedule>()
        dao.insertSchedules(remote.map { it.toEntity() })
        return remote
    }

    suspend fun markMedicineTaken(schedule: MedicineSchedule) {
        val log = MedicineLog(scheduleId = schedule.id ?: 0, patientId = schedule.patientId, takenDate = today())
        client.from("medicine_logs").insert(log)
        dao.insertMedicineLogs(listOf(log.toEntity()))
    }

    suspend fun loadPatientMedicineLogs(patientId: Long?): List<MedicineLog> {
        if (patientId == null) return emptyList()
        return client.from("medicine_logs").select {
            filter { eq("patient_id", patientId); eq("taken_date", today()) }
        }.decodeList()
    }

    suspend fun loadTodayCondition(patientId: Long?): DailyCondition? {
        if (patientId == null) return null
        return client.from("daily_conditions").select {
            filter { eq("patient_id", patientId); eq("date", today()) }
        }.decodeList<DailyCondition>().lastOrNull()
    }

    // Fungsi yang hilang untuk Error 2
    suspend fun loadPatientCondition(patientId: Long?): DailyCondition? {
        return loadTodayCondition(patientId)
    }

    suspend fun loadDailyConditions(patientId: Long): List<DailyCondition> {
        return client.from("daily_conditions").select { filter { eq("patient_id", patientId) } }.decodeList()
    }

    suspend fun uploadProfileImage(
        userId: Long,
        fileBytes: ByteArray
    ): String {

        val fileName = "avatar_${userId}_${System.currentTimeMillis()}.jpg"

        client.storage
            .from("Avatars")
            .upload(
                path = fileName,
                data = fileBytes,
                upsert = true
            )

        val publicUrl = client.storage
            .from("Avatars")
            .publicUrl(fileName)

        client.from("users").update(
            mapOf("profile_image" to publicUrl)
        ) {
            filter {
                eq("id", userId)
            }
        }

        return publicUrl
    }

    suspend fun updateProfile(
        userId: Long,
        name: String,
        phone: String,
        age: Int?,
        gender: String?
    ) {

        val request = UpdateProfileRequest(
            name = name,
            phone = phone,
            age = age,
            gender = gender
        )

        client.from("users").update(request) {
            filter {
                eq("id", userId)
            }
        }

        val updated = client.from("users")
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingle<AppUser>()

        dao.insertUser(updated.toEntity())
    }

    suspend fun loadHealthProfile(patientId: Long): HealthProfile? {
        return client.from("health_profiles").select { filter { eq("patient_id", patientId) } }.decodeList<HealthProfile>().firstOrNull()
    }

    suspend fun saveHealthProfile(
        patientId: Long,
        h: Int?,
        w: Int?,
        bt: String?,
        al: String?,
        mh: String?
    ) {

        val request = UpdateHealthProfileRequest(
            patientId = patientId,
            height = h,
            weight = w,
            bloodType = bt,
            allergy = al,
            medicalHistory = mh
        )

        client.from("health_profiles")
            .upsert(request)
    }

    private suspend fun loadCurrentProfile(authId: String): AppUser {
        return client.from("users").select { filter { eq("auth_id", authId) } }.decodeSingle()
    }

    private fun today(): String = dateFormat.format(Date())

    private fun AppUser.toEntity() =
        UserEntity(
            id = id!!,
            authId = authId,
            name = name,
            email = email,
            phone = phone,
            role = role,
            caregiverId = caregiverId,
            age = age,
            gender = gender,
            profileImage = profileImage
        )
    private fun UserEntity.toModel() =
        AppUser(
            id = id,
            authId = authId,
            name = name,
            email = email,
            phone = phone,
            role = role,
            caregiverId = caregiverId,
            age = age,
            gender = gender,
            profileImage = profileImage
        )
    private fun MedicineSchedule.toEntity() = MedicineScheduleEntity(id ?: 0, patientId, medicineName, dosage, scheduleTime, note, isActive)
    private fun DailyCondition.toEntity() = DailyConditionEntity(remoteId = id, patientId = patientId, date = date, condition = condition, mood = mood, bloodPressure = bloodPressure, notes = notes)
    private fun MedicineLog.toEntity() = MedicineLogEntity(scheduleId = scheduleId, patientId = patientId, takenDate = takenDate, status = status)
}
