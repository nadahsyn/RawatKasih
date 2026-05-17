package com.projekakhir.rawatkasih.model

import androidx.compose.ui.graphics.Color

data class Patient(
    val name: String,
    val age: Int,
    val condition: String,
    val bloodPressure: String,
    val medicineStatus: String,
    val statusColor: Color
)