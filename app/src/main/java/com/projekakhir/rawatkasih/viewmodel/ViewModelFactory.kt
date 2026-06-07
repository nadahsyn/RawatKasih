package com.projekakhir.rawatkasih.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projekakhir.rawatkasih.data.RawatKasihRepository

class ViewModelFactory(private val repository: RawatKasihRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository) as T
            modelClass.isAssignableFrom(CaregiverViewModel::class.java) -> CaregiverViewModel(repository) as T
            modelClass.isAssignableFrom(PatientViewModel::class.java) -> PatientViewModel(repository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository) as T
            modelClass.isAssignableFrom(HealthViewModel::class.java) -> HealthViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
