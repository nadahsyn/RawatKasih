package com.projekakhir.rawatkasih.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.projekakhir.rawatkasih.RawatKasihHeader
import com.projekakhir.rawatkasih.ui.theme.CardMint
import com.projekakhir.rawatkasih.ui.theme.PrimaryMint
import com.projekakhir.rawatkasih.ui.theme.Surface
import com.projekakhir.rawatkasih.ui.theme.TextSecondary
import com.projekakhir.rawatkasih.viewmodel.HealthViewModel

@Composable
fun HealthScreen(
    userId: Long,
    onBack: () -> Unit,
    onEditHealthProfile: (Long) -> Unit,
    onOpenHistory: (Long) -> Unit,
    successMessage: String?,
    onMessageShown: () -> Unit,
    viewModel: HealthViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val profile by viewModel.healthProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadHealthProfile(userId)
    }

    LaunchedEffect(successMessage) {
        successMessage?.let {
            snackbarHostState.showSnackbar(it)
            onMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { RawatKasihHeader() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    modifier = Modifier.size(24.dp).clickable { onBack() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Kesehatan", fontSize = 23.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                if (isLoading) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = PrimaryMint)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Memuat data kesehatan...")
                    }
                } else {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = CardMint)) {
                                Icon(Icons.Default.Favorite, null, tint = PrimaryMint, modifier = Modifier.padding(12.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Profil Kesehatan", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        HealthInfoRow(label = "Tinggi Badan", value = "${profile?.height ?: "-"} cm")
                        Spacer(modifier = Modifier.height(12.dp))
                        HealthInfoRow(label = "Berat Badan", value = "${profile?.weight ?: "-"} kg")
                        Spacer(modifier = Modifier.height(12.dp))
                        HealthInfoRow(label = "Golongan Darah", value = profile?.bloodType ?: "-")
                        Spacer(modifier = Modifier.height(12.dp))
                        HealthInfoRow(label = "Alergi", value = profile?.allergy ?: "-")
                        Spacer(modifier = Modifier.height(12.dp))
                        HealthInfoRow(label = "Riwayat Penyakit", value = profile?.medicalHistory ?: "-")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { onEditHealthProfile(userId) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(2.dp, PrimaryMint)
            ) {
                Text(text = "Edit Profil Kesehatan", color = PrimaryMint, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth().clickable { onOpenHistory(userId) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = CardMint)) {
                        Icon(Icons.Default.History, null, tint = PrimaryMint, modifier = Modifier.padding(12.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Catatan Kesehatan", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Lihat riwayat kondisi yang telah dicatat", color = TextSecondary, fontSize = 13.sp)
                    }
                    Text(text = ">", fontSize = 20.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun HealthInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = TextSecondary)
        Text(text = value, fontWeight = FontWeight.Medium)
    }
}
