package com.projekakhir.rawatkasih.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import com.projekakhir.rawatkasih.model.Patient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.projekakhir.rawatkasih.data.MedicineSchedule

class CaregiverViewModel(private val repository: RawatKasihRepository) : ViewModel() {
    private val _allPatients = MutableStateFlow<List<Patient>>(emptyList())
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    val filteredPatients: StateFlow<List<Patient>> = combine(_allPatients, _searchQuery) { patients, query ->
        if (query.isBlank()) {
            patients
        } else {
            patients.filter { it.name.contains(query, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun loadPatients(caregiverId: Long?) {
        if (caregiverId == null) return
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Sync from remote first (Modul 8/9 pattern)
                repository.refreshCaregiverPatients(caregiverId)
                
                // Then observe flow or load from local
                val patientProfiles = repository.loadCaregiverPatients(caregiverId) // This should be updated in repo to use Room
                
                val mappedPatients = patientProfiles.map { profile ->
                    val condition = repository.loadPatientCondition(profile.id)
                    val logs = repository.loadPatientMedicineLogs(profile.id)
                    val schedules = repository.loadMedicineSchedules(profile.id)
                    val status = condition?.condition ?: "Belum input"
                    
                    val totalMed = schedules.size
                    val takenMed = logs.size
                    val progress = if (totalMed > 0) takenMed.toFloat() / totalMed.toFloat() else 0f
                    
                    Patient(
                        id = profile.id ?: 0,
                        name = profile.name,
                        age = profile.age ?: 0,
                        condition = status,
                        bloodPressure = condition?.bloodPressure ?: "-",
                        medicineStatus = if (logs.isEmpty()) "Belum diminum" else if (takenMed >= totalMed && totalMed > 0) "Lengkap" else "Sebagian",
                        statusColor = statusColor(status),
                        hasSchedule = totalMed > 0,
                        mood = condition?.mood ?: "-",
                        notes = condition?.notes ?: "-",
                        medicineProgress = progress,
                        totalMedicine = totalMed,
                        takenMedicine = takenMed
                    )
                }
                _allPatients.value = mappedPatients
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat data pasien."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveMedicineSchedule(schedule: MedicineSchedule, caregiverId: Long?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.saveMedicineSchedule(schedule)
                loadPatients(caregiverId) // Refresh after save
                onSuccess()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun statusColor(status: String): Color {
        return when (status.lowercase()) {
            "baik", "stabil" -> Color(0xFF63C7B2)
            "pusing", "lemas", "perhatian" -> Color(0xFFFFB74D)
            "darurat" -> Color(0xFFE57373)
            else -> Color(0xFF8E8E8E)
        }
    }
}
