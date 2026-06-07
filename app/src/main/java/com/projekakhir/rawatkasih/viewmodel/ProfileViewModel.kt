package com.projekakhir.rawatkasih.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projekakhir.rawatkasih.data.AppUser
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import java.io.File

class ProfileViewModel(private val repository: RawatKasihRepository) : ViewModel() {
    private val _user = MutableStateFlow<AppUser?>(null)
    val user = _user.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadProfile(userId: Long) {
        viewModelScope.launch {
            try {
                val profile = repository.loadUserById(userId)
                _user.value = profile
            } catch (e: Exception) {
                _error.value = "Gagal memuat profil."
            }
        }
    }

    fun updateProfile(userId: Long, name: String, phone: String, age: Int?, gender: String?, onResult: (AppUser) -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                repository.updateProfile(userId, name, phone, age, gender)
                val updatedUser = repository.loadUserById(userId)
                _user.value = updatedUser
                onResult(updatedUser)
            } catch (e: Exception) {
                 e.printStackTrace()
                _error.value = "Gagal memperbarui profil."
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun uploadProfileImage(
        userId: Long,
        bytes: ByteArray
    ) {
        viewModelScope.launch {
            try {
                repository.uploadProfileImage(userId, bytes)

                val updated = repository.loadUserById(userId)
                _user.value = updated

            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Gagal upload foto"
            }
        }
    }
}
