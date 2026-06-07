package com.projekakhir.rawatkasih.model

import androidx.compose.ui.graphics.Color

data class Patient(
    val id: Long,
    val name: String,
    val age: Int,
    val condition: String,
    val bloodPressure: String,
    val medicineStatus: String,
    val statusColor: Color,
    val hasSchedule: Boolean = false,
    val mood: String = "-",
    val notes: String = "-",
    val medicineProgress: Float = 0f,
    val totalMedicine: Int = 0,
    val takenMedicine: Int = 0
)