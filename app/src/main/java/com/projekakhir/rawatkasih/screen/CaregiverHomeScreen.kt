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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projekakhir.rawatkasih.components.PatientCard
import com.projekakhir.rawatkasih.components.SearchBar
import com.projekakhir.rawatkasih.components.SummaryCard
import com.projekakhir.rawatkasih.model.Patient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverHomeScreen() {

    val patientList = listOf(

        Patient(
            name = "Oma Siti",
            age = 72,
            condition = "Stabil",
            bloodPressure = "120/80",
            medicineStatus = "Sudah diminum",
            statusColor = Color(0xFF63C7B2)
        ),

        Patient(
            name = "Pak Budi",
            age = 68,
            condition = "Perhatian",
            bloodPressure = "145/90",
            medicineStatus = "Belum diminum",
            statusColor = Color(0xFFFFB74D)
        ),

        Patient(
            name = "Nenek Rina",
            age = 75,
            condition = "Darurat",
            bloodPressure = "170/100",
            medicineStatus = "Belum diminum",
            statusColor = Color(0xFFE57373)
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

                    IconButton(
                        onClick = {}
                    ) {

                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notification"
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

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Halo, Suster Dina 👋",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E)
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

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
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(patientList) { patient ->

                PatientCard(
                    patient = patient
                )
            }

            item {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }
    }
}