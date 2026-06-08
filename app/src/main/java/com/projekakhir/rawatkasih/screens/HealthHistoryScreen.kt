package com.projekakhir.rawatkasih.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.projekakhir.rawatkasih.ui.theme.Background
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Bloodtype
import com.projekakhir.rawatkasih.ui.theme.Surface
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.projekakhir.rawatkasih.RawatKasihHeader
import com.projekakhir.rawatkasih.ui.theme.PrimaryMint
import com.projekakhir.rawatkasih.ui.theme.TextSecondary
import com.projekakhir.rawatkasih.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HealthHistoryScreen(
    userId: Long,
    onBack: () -> Unit,
    viewModel: HealthViewModel = viewModel()
) {
    val histories by viewModel.history.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadHistory(userId)
    }

    Scaffold(
        containerColor = Background,
        topBar = { RawatKasihHeader() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    modifier = Modifier.clickable { onBack() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Riwayat Kesehatan",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryMint)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(histories) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = formatDate(item.date),
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                HealthHistoryItem(
                                    icon = Icons.Default.Favorite,
                                    label = "Kondisi",
                                    value = item.condition
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                HealthHistoryItem(
                                    icon = Icons.Default.Psychology,
                                    label = "Mood",
                                    value = item.mood
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                HealthHistoryItem(
                                    icon = Icons.Default.Bloodtype,
                                    label = "Tekanan Darah",
                                    value = item.bloodPressure ?: "-"
                                )
                                if (!item.notes.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    HorizontalDivider()
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Catatan",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = item.notes)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HealthHistoryItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryMint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = label, color = TextSecondary, fontSize = 12.sp)
            Text(text = value, fontWeight = FontWeight.SemiBold)
        }
    }
}

private fun formatDate(date: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate!!)
    } catch (e: Exception) {
        date
    }
}
