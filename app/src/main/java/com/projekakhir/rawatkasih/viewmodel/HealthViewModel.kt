package com.projekakhir.rawatkasih.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projekakhir.rawatkasih.data.HealthProfile
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import com.projekakhir.rawatkasih.data.DailyCondition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HealthViewModel(private val repository: RawatKasihRepository) : ViewModel() {
    private val _healthProfile = MutableStateFlow<HealthProfile?>(null)
    val healthProfile = _healthProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _history = MutableStateFlow<List<DailyCondition>>(emptyList())
    val history = _history.asStateFlow()

    fun loadHealthProfile(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val profile = repository.loadHealthProfile(userId)
                _healthProfile.value = profile
            } catch (e: Exception) {
                _error.value = "Gagal memuat profil kesehatan."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveHealthProfile(
        userId: Long,
        height: Int?,
        weight: Int?,
        bloodType: String?,
        allergy: String?,
        medicalHistory: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.saveHealthProfile(userId, height, weight, bloodType, allergy, medicalHistory)
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Gagal menyimpan profil kesehatan."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadHistory(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = repository.loadDailyConditions(userId)
                _history.value = list
            } catch (e: Exception) {
                _error.value = "Gagal memuat riwayat kesehatan."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
