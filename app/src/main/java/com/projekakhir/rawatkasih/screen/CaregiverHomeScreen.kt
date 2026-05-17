package com.projekakhir.rawatkasih.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projekakhir.rawatkasih.components.PatientCard
import com.projekakhir.rawatkasih.components.SearchBar
import com.projekakhir.rawatkasih.components.SummaryCard
import com.projekakhir.rawatkasih.model.Patient

@Composable
fun CaregiverHomeScreen() {

    val patientList = listOf(
        Patient(
            "Oma Siti",
            72,
            "Stabil",
            "120/80",
            "Sudah diminum",
            Color(0xFF63C7B2)
        ),
        Patient(
            "Pak Budi",
            68,
            "Perhatian",
            "145/90",
            "Belum diminum",
            Color(0xFFFFB74D)
        ),
        Patient(
            "Nenek Rina",
            75,
            "Darurat",
            "170/100",
            "Belum diminum",
            Color(0xFFE57373)
        )
    )

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
                            Icons.Default.Notifications,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF8FFFD)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FFFD))
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Halo, Suster Dina 👋",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Pantau kondisi pasien hari ini",
                    color = Color(0xFF8E8E8E),
                    fontSize = 16.sp
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SummaryCard(
                        title = "Pasien",
                        value = "12",
                        backgroundColor = Color(0xFFDDF6F1)
                    )

                    SummaryCard(
                        title = "Reminder",
                        value = "5",
                        backgroundColor = Color(0xFFCFF4EC)
                    )

                    SummaryCard(
                        title = "Darurat",
                        value = "2",
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
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            items(patientList) { patient ->
                PatientCard(patient)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

