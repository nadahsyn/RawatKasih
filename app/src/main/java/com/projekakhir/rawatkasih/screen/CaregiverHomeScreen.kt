package com.projekakhir.rawatkasih.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projekakhir.rawatkasih.components.PatientCard
import com.projekakhir.rawatkasih.components.SearchBar
import com.projekakhir.rawatkasih.components.SummaryCard
import com.projekakhir.rawatkasih.data.AppUser
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import com.projekakhir.rawatkasih.model.Patient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverHomeScreen(user: AppUser) {
    var patients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(user.id) {
        try {
            val patientProfiles = RawatKasihRepository.loadCaregiverPatients(user.id)
            patients = patientProfiles.map { profile ->
                val condition = RawatKasihRepository.loadPatientCondition(profile.id)
                val logs = RawatKasihRepository.loadPatientMedicineLogs(profile.id)
                val status = condition?.condition ?: "Belum input"
                Patient(
                    name = profile.name,
                    age = profile.age ?: 0,
                    condition = status,
                    bloodPressure = condition?.bloodPressure ?: "-",
                    medicineStatus = if (logs.isEmpty()) "Belum diminum" else "Sudah diminum",
                    statusColor = statusColor(status)
                )
            }
        } catch (e: Exception) {
            errorMessage = "Data pasien belum bisa dimuat."
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "RawatKasih",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF8FFFD)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FFFD))
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Halo, ${user.name}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pantau kondisi pasien hari ini",
                    fontSize = 16.sp,
                    color = Color(0xFF8E8E8E)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SummaryCard(
                        title = "Pasien",
                        value = patients.size.toString(),
                        backgroundColor = Color(0xFFDDF6F1)
                    )
                    SummaryCard(
                        title = "Reminder",
                        value = patients.count { it.medicineStatus == "Belum diminum" }.toString(),
                        backgroundColor = Color(0xFFCFF4EC)
                    )
                    SummaryCard(
                        title = "Perhatian",
                        value = patients.count { it.condition != "Baik" && it.condition != "Belum input" }.toString(),
                        backgroundColor = Color(0xFFFFE1E1)
                    )
                }
            }

            item {
                SearchBar()
            }

            item {
                Text(
                    text = "Daftar Pasien",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (isLoading) {
                item {
                    CircularProgressIndicator(color = Color(0xFF63C7B2))
                }
            } else if (errorMessage.isNotEmpty()) {
                item {
                    Text(text = errorMessage, color = Color(0xFFE57373))
                }
            } else if (patients.isEmpty()) {
                item {
                    Text(text = "Belum ada pasien terhubung.", color = Color(0xFF8E8E8E))
                }
            } else {
                items(patients) { patient ->
                    PatientCard(patient = patient)
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
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
