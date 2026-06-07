package com.projekakhir.rawatkasih.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projekakhir.rawatkasih.data.AuthResult
import com.projekakhir.rawatkasih.data.MedicineSchedule
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PatientViewModel(private val repository: RawatKasihRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthResult?>(null)
    val uiState = _uiState.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _conditionSaved = MutableStateFlow(false)
    val conditionSaved = _conditionSaved.asStateFlow()

    fun loadPatientData(userId: Long) {
        viewModelScope.launch {
            try {
                val user = repository.loadUserById(userId)
                val homeData = repository.loadPatientHome(user)
                _uiState.value = homeData
                _conditionSaved.value = homeData.condition != null
            } catch (e: Exception) {
                _message.value = "Gagal memuat data."
            }
        }
    }

    fun markMedicineTaken(schedule: MedicineSchedule) {
        val current = _uiState.value ?: return
        viewModelScope.launch {
            _isSubmitting.value = true
            try {
                repository.markMedicineTaken(schedule)
                val updatedLogs = repository.loadPatientMedicineLogs(current.user.id)
                _uiState.value = current.copy(logs = updatedLogs)
                _message.value = "Hebat! Jangan lupa tetap jaga kesehatan"
            } catch (e: Exception) {
                _message.value = "Gagal menyimpan status obat."
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    fun saveDailyCondition(patientId: Long, condition: String, mood: String, bp: String, notes: String) {
        viewModelScope.launch {
            try {
                repository.saveDailyCondition(patientId, condition, mood, bp, notes)
                _conditionSaved.value = true
                _message.value = "Terima kasih sudah mengisi kondisi hari ini!"
            } catch (e: Exception) {
                _message.value = "Gagal menyimpan kondisi."
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}
