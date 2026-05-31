package com.projekakhir.rawatkasih.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HealthProfile(
    val id: Long? = null,

    @SerialName("patient_id")
    val patientId: Long,

    val height: Int? = null,

    val weight: Int? = null,

    @SerialName("blood_type")
    val bloodType: String? = null,

    val allergy: String? = null,

    @SerialName("medical_history")
    val medicalHistory: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null

)
