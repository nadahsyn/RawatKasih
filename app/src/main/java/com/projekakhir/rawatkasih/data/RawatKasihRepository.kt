package com.projekakhir.rawatkasih.data

import com.projekakhir.rawatkasih.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import io.github.jan.supabase.postgrest.query.Columns
import com.projekakhir.rawatkasih.data.HealthProfile

@Serializable
data class AppUser(
    val id: Long? = null,

    @SerialName("auth_id")
    val authId: String? = null,

    val name: String,
    val email: String? = null,
    val phone: String? = null,

    val role: String,

    @SerialName("caregiver_id")
    val caregiverId: Long? = null,

    val age: Int? = null,
    val gender: String? = null,

    @SerialName("blood_type")
    val bloodType: String? = null,

    val height: Int? = null,
    val weight: Int? = null,

    @SerialName("profile_image")
    val profileImage: String? = null
)

@Serializable
data class CaregiverOption(
    val id: Long,
    val name: String
)

@Serializable
data class UserInsert(
    @SerialName("auth_id")
    val authId: String?,

    val name: String,
    val email: String,
    val phone: String,
    val role: String,

    @SerialName("caregiver_id")
    val caregiverId: Long
)

@Serializable
data class MedicineSchedule(
    val id: Long,
    @SerialName("patient_id")
    val patientId: Long,
    @SerialName("medicine_name")
    val medicineName: String,
    val dosage: String? = null,
    @SerialName("schedule_time")
    val scheduleTime: String,
    val note: String? = null
)

@Serializable
data class MedicineLog(
    val id: Long? = null,
    @SerialName("schedule_id")
    val scheduleId: Long,
    @SerialName("patient_id")
    val patientId: Long,
    @SerialName("taken_date")
    val takenDate: String,
    @SerialName("taken_at")
    val takenAt: String? = null,
    val status: String = "taken"
)

@Serializable
data class DailyCondition(
    val id: Long? = null,
    @SerialName("patient_id")
    val patientId: Long,
    val date: String,
    val condition: String,
    val mood: String,
    @SerialName("blood_pressure")
    val bloodPressure: String? = null
)

data class AuthResult(
    val user: AppUser,
    val schedules: List<MedicineSchedule> = emptyList(),
    val logs: List<MedicineLog> = emptyList(),
    val condition: DailyCondition? = null
)

object RawatKasihRepository {
    private val client = SupabaseClient.client
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val timestampFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    suspend fun login(email: String, password: String): AuthResult {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        val authUser = client.auth.currentSessionOrNull()?.user
            ?: error("Sesi login tidak ditemukan.")
        val profile = loadCurrentProfile(authUser.id)
        return if (profile.role == "patient") {
            val schedules = loadMedicineSchedules(profile.id)
            val logs = loadTodayMedicineLogs(profile.id)
            val condition = loadTodayCondition(profile.id)
            AuthResult(profile, schedules, logs, condition)
        } else {
            AuthResult(profile)
        }
    }

    suspend fun registerPatient(
        name: String,
        email: String,
        phone: String,
        password: String,
        caregiverId: Long
    ) {
        val user = client.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

        val authId = user?.id ?: client.auth.currentSessionOrNull()?.user?.id
        client.from("users").insert(
            UserInsert(
                authId = authId,
                name = name,
                email = email,
                phone = phone,
                role = "patient",
                caregiverId = caregiverId
            )
        )
    }

    suspend fun loadCaregivers(): List<CaregiverOption> {

        android.util.Log.d(
            "RAWATKASIH",
            "QUERY KE SUPABASE"
        )

        val result = client.from("users")
            .select(columns = Columns.list("id", "name")) {
                filter {
                    eq("role", "caregiver")
                }
            }
            .decodeList<CaregiverOption>()

        android.util.Log.d(
            "RAWATKASIH",
            "HASIL = $result"
        )

        return result
    }
    suspend fun loadCaregiverPatients(caregiverId: Long?): List<AppUser> {
        if (caregiverId == null) return emptyList()

        return client.from("users")
            .select {
                filter {
                    eq("caregiver_id", caregiverId)
                    eq("role", "patient")
                }
            }
            .decodeList<AppUser>()
    }

    suspend fun loadPatientHome(user: AppUser): AuthResult {
        val schedules = loadMedicineSchedules(user.id)
        val logs = loadTodayMedicineLogs(user.id)
        val condition = loadTodayCondition(user.id)
        return AuthResult(user, schedules, logs, condition)
    }

    suspend fun loadPatientCondition(patientId: Long?): DailyCondition? {
        return loadTodayCondition(patientId)
    }

    suspend fun loadPatientMedicineLogs(patientId: Long?): List<MedicineLog> {
        return loadTodayMedicineLogs(patientId)
    }

    suspend fun markMedicineTaken(schedule: MedicineSchedule) {
        client.from("medicine_logs").insert(
            MedicineLog(
                scheduleId = schedule.id,
                patientId = schedule.patientId,
                takenDate = today(),
                takenAt = nowTimestamp(),
                status = "taken"
            )
        )
    }

    suspend fun saveDailyCondition(
        patientId: Long,
        condition: String,
        mood: String,
        bloodPressure: String
    ) {
        client.from("daily_conditions").insert(
            DailyCondition(
                patientId = patientId,
                date = today(),
                condition = condition,
                mood = mood,
                bloodPressure = bloodPressure.ifBlank { null }
            )
        )
    }

    suspend fun loadHealthProfile(
        patientId: Long
    ): HealthProfile? {

        return client.from("health_profiles")
            .select {
                filter {
                    eq("patient_id", patientId)
                }
            }
            .decodeList<HealthProfile>()
            .firstOrNull()
    }

    suspend fun saveHealthProfile(
        patientId: Long,
        height: Int?,
        weight: Int?,
        bloodType: String?,
        allergy: String?,
        medicalHistory: String?
    ) {

        val existingProfile =
            loadHealthProfile(patientId)

        if (existingProfile == null) {

            client.from("health_profiles")
                .insert(
                    HealthProfile(
                        patientId = patientId,
                        height = height,
                        weight = weight,
                        bloodType = bloodType,
                        allergy = allergy,
                        medicalHistory = medicalHistory
                    )
                )

        } else {

            client.from("health_profiles")
                .update(
                    UpdateHealthProfileRequest(
                        height = height,
                        weight = weight,
                        bloodType = bloodType,
                        allergy = allergy,
                        medicalHistory = medicalHistory
                    )
                ) {
                    filter {
                        eq("patient_id", patientId)
                    }
                }
        }
    }

    @Serializable
    private data class UpdateProfileRequest(
        val name: String,
        val phone: String?,
        val age: Int?,
        val gender: String?
    )
    @Serializable
    private data class UpdateHealthProfileRequest(

        val height: Int?,

        val weight: Int?,

        @SerialName("blood_type")
        val bloodType: String?,

        val allergy: String?,

        @SerialName("medical_history")
        val medicalHistory: String?
    )
    suspend fun updateProfile(
        userId: Long,
        name: String,
        phone: String,
        age: Int?,
        gender: String?
    ) {
        client.from("users")
            .update(
                UpdateProfileRequest(
                    name = name,
                    phone = phone.ifBlank { null },
                    age = age,
                    gender = gender
                )
            ) {
                filter {
                    eq("id", userId)
                }
            }
    }
    suspend fun loadUserById(userId: Long): AppUser {
        return client.from("users")
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingle()
    }
    private suspend fun loadCurrentProfile(authId: String): AppUser {
        return client.from("users")
            .select {
                filter {
                    eq("auth_id", authId)
                }
            }
            .decodeSingle()
    }

    private suspend fun loadMedicineSchedules(patientId: Long?): List<MedicineSchedule> {
        if (patientId == null) return emptyList()
        return client.from("medicine_schedules")
            .select {
                filter {
                    eq("patient_id", patientId)
                }
            }
            .decodeList()
    }

    private suspend fun loadTodayMedicineLogs(patientId: Long?): List<MedicineLog> {
        if (patientId == null) return emptyList()
        return client.from("medicine_logs")
            .select {
                filter {
                    eq("patient_id", patientId)
                    eq("taken_date", today())
                }
            }
            .decodeList()
    }

    private suspend fun loadTodayCondition(patientId: Long?): DailyCondition? {
        if (patientId == null) return null
        return client.from("daily_conditions")
            .select {
                filter {
                    eq("patient_id", patientId)
                    eq("date", today())
                }
            }
            .decodeList<DailyCondition>()
            .lastOrNull()
    }

    private fun today(): String = dateFormat.format(Date())

    private fun nowTimestamp(): String = timestampFormat.format(Date())

}
