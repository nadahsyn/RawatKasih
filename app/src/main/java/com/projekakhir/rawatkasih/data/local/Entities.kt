package com.projekakhir.rawatkasih.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val authId: String?,
    val name: String,
    val email: String?,
    val phone: String?,
    val role: String,
    val caregiverId: Long?,
    val age: Int?,
    val gender: String?,
    val profileImage: String?
)

@Entity(tableName = "medicine_schedules")
data class MedicineScheduleEntity(
    @PrimaryKey val id: Long,
    val patientId: Long,
    val medicineName: String,
    val dosage: String?,
    val scheduleTime: String,
    val note: String?,
    val isActive: Boolean
)

@Entity(tableName = "medicine_logs")
data class MedicineLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scheduleId: Long,
    val patientId: Long,
    val takenDate: String,
    val status: String
)

@Entity(tableName = "daily_conditions")
data class DailyConditionEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val remoteId: Long?,
    val patientId: Long,
    val date: String,
    val condition: String,
    val mood: String,
    val bloodPressure: String?,
    val notes: String?
)
