package com.projekakhir.rawatkasih.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projekakhir.rawatkasih.data.AuthResult
import com.projekakhir.rawatkasih.data.CaregiverOption
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: RawatKasihRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _caregivers = MutableStateFlow<List<CaregiverOption>>(emptyList())
    val caregivers = _caregivers.asStateFlow()

    fun login(email: String, password: String, onSuccess: (AuthResult) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.login(email, password)
                onSuccess(result)
            } catch (e: Exception) {
                _error.value = e.message ?: "Login gagal. Periksa kembali email dan password Anda."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCaregivers() {
        viewModelScope.launch {
            try {
                val list = repository.loadCaregivers()
                _caregivers.value = list
            } catch (e: Exception) {
                _error.value = "Gagal memuat daftar caregiver."
            }
        }
    }

    fun registerPatient(
        name: String,
        email: String,
        phone: String,
        password: String,
        caregiverId: Long,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.registerPatient(name, email, phone, password, caregiverId)
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message ?: "Registrasi gagal. Silakan coba lagi."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
