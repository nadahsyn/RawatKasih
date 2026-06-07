package com.projekakhir.rawatkasih.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RawatKasihDao {
    // User operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserEntity?

    @Query("SELECT * FROM users WHERE role = 'patient' AND caregiverId = :caregiverId")
    fun getPatientsForCaregiver(caregiverId: Long): Flow<List<UserEntity>>

    // Medicine Schedule operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<MedicineScheduleEntity>)

    @Query("SELECT * FROM medicine_schedules WHERE patientId = :patientId")
    fun getSchedulesForPatient(patientId: Long): Flow<List<MedicineScheduleEntity>>

    // Medicine Log operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicineLogs(logs: List<MedicineLogEntity>)

    @Query("SELECT * FROM medicine_logs WHERE patientId = :patientId AND takenDate = :date")
    fun getTodayMedicineLogs(patientId: Long, date: String): Flow<List<MedicineLogEntity>>

    // Daily Condition operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCondition(condition: DailyConditionEntity)

    @Query("SELECT * FROM daily_conditions WHERE patientId = :patientId ORDER BY date DESC")
    fun getConditionsForPatient(patientId: Long): Flow<List<DailyConditionEntity>>

    @Query("DELETE FROM users")
    suspend fun clearAllUsers()
}
