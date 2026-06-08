package com.projekakhir.rawatkasih.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.projekakhir.rawatkasih.R
import com.projekakhir.rawatkasih.data.MedicineLog
import com.projekakhir.rawatkasih.data.MedicineSchedule
import com.projekakhir.rawatkasih.ui.theme.*
import com.projekakhir.rawatkasih.viewmodel.PatientViewModel
import coil.compose.AsyncImage
import com.projekakhir.rawatkasih.RawatKasihHeader

@Composable
fun PatientHomeScreen(
    userId: Long,
    onEditProfile: (Long) -> Unit,
    onOpenHealth: (Long) -> Unit,
    onLogout: () -> Unit = {},
    successMessage: String?,
    onMessageShown: () -> Unit,
    viewModel: PatientViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val message by viewModel.message.collectAsState()
    val conditionSaved by viewModel.conditionSaved.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadPatientData(userId)
    }

    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            snackbarHostState.showSnackbar(successMessage)
            onMessageShown()
        }
    }

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessage()
        }
    }

    val user = uiState?.user
    val schedules = uiState?.schedules ?: emptyList()
    val logs = uiState?.logs ?: emptyList()

    var kondisi by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf("") }
    var bloodPressure by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Sync state when data is loaded
    LaunchedEffect(uiState) {
        uiState?.condition?.let {
            kondisi = it.condition
            mood = it.mood
            bloodPressure = it.bloodPressure ?: ""
            notes = it.notes ?: ""
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { RawatKasihHeader(onLogout = onLogout) }
    ) { paddingValues ->
        if (user == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryMint)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardMint)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (!user.profileImage.isNullOrEmpty()) {

                                    AsyncImage(
                                        model = user.profileImage,
                                        contentDescription = "Foto Profil",
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(CircleShape)
                                            .border(3.dp, Surface, CircleShape)
                                    )

                                } else {

                                    Image(
                                        painter = painterResource(id = R.drawable.ava),
                                        contentDescription = "Foto Profil",
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(CircleShape)
                                            .border(3.dp, Surface, CircleShape)
                                    )

                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = user.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(7.dp))
                                    Text(
                                        text = user.age?.let { "$it Tahun" } ?: "Profil belum lengkap",
                                        fontSize = 14.sp, color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(
                                        text = user.gender ?: "Lengkapi data diri Anda",
                                        fontSize = 14.sp, color = TextSecondary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onEditProfile(userId) },
                                    modifier = Modifier.weight(1f).height(44.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(2.dp, PrimaryMint)
                                ) {
                                    Icon(Icons.Default.Edit, null, tint = PrimaryMint, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Edit Profil", color = PrimaryMint, fontWeight = FontWeight.SemiBold)
                                }
                                OutlinedButton(
                                    onClick = { onOpenHealth(userId) },
                                    modifier = Modifier.weight(1f).height(44.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(2.dp, PrimaryMint)
                                ) {
                                    Icon(Icons.Default.Favorite, null, tint = PrimaryMint, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Kesehatan", color = PrimaryMint, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }

                item {
                    ReminderCard(
                        schedules = schedules,
                        logs = logs,
                        isSubmitting = isSubmitting,
                        onTaken = { viewModel.markMedicineTaken(it) }
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = CardMint)) {
                                    Icon(Icons.Default.Favorite, "Kondisi", tint = PrimaryMint, modifier = Modifier.padding(12.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Kondisi Hari Ini", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                            Text("Bagaimana kondisi tubuh hari ini?", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(12.dp))
                            ChoiceRow(options = listOf("Baik", "Pusing", "Lemas"), selected = kondisi, onSelected = { kondisi = it })

                            Spacer(modifier = Modifier.height(24.dp))
                            Text("Bagaimana mood hari ini?", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(12.dp))
                            ChoiceRow(options = listOf("Senang", "Biasa", "Sedih"), selected = mood, onSelected = { mood = it })

                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedTextField(
                                value = bloodPressure, onValueChange = { bloodPressure = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Tekanan darah") }, placeholder = { Text("Contoh: 120/80") },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryMint, unfocusedBorderColor = InputBorder, focusedLabelColor = PrimaryMint),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = notes, onValueChange = { notes = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Catatan Tambahan (Opsional)") }, placeholder = { Text("Tuliskan keluhan atau kondisi lain") },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryMint, unfocusedBorderColor = InputBorder, focusedLabelColor = PrimaryMint),
                                minLines = 3
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            val conditionInteraction = remember { MutableInteractionSource() }
                            val conditionPressed by conditionInteraction.collectIsPressedAsState()
                            val conditionScale by animateFloatAsState(if (conditionPressed) 0.96f else 1f, label = "")
                            
                            Button(
                                onClick = {
                                    viewModel.saveDailyCondition(userId, kondisi, mood, bloodPressure, notes)
                                },
                                interactionSource = conditionInteraction,
                                modifier = Modifier.fillMaxWidth().height(50.dp).graphicsLayer { scaleX = conditionScale; scaleY = conditionScale },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryMint, disabledContainerColor = InputBorder),
                                shape = RoundedCornerShape(16.dp),
                                enabled = !conditionSaved && kondisi.isNotBlank() && mood.isNotBlank() && bloodPressure.isNotBlank()
                            ) {
                                if (conditionSaved) {
                                    Icon(Icons.Default.CheckCircle, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Kondisi Tersimpan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                } else {
                                    Text("Simpan Kondisi", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }

                item {
                    ScheduleList(schedules = schedules, logs = logs)
                }
            }
        }
    }
}

@Composable
private fun ReminderCard(
    schedules: List<MedicineSchedule>,
    logs: List<MedicineLog>,
    isSubmitting: Boolean,
    onTaken: (MedicineSchedule) -> Unit
) {
    val nextSchedule = schedules.firstOrNull { schedule -> logs.none { it.scheduleId == schedule.id } }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f, label = "")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = CardMint)) {
                    Icon(Icons.Default.Medication, "Obat", tint = PrimaryMint, modifier = Modifier.padding(12.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Reminder Obat", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    if (nextSchedule == null) {
                        Text("Semua obat hari ini sudah diminum. Luar biasa!", fontSize = 14.sp, color = PrimaryMint, fontWeight = FontWeight.Medium)
                    } else {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(text = nextSchedule.medicineName, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, null, modifier = Modifier.size(14.dp), tint = TextSecondary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Dosis: ${nextSchedule.dosage ?: "-"}", fontSize = 14.sp, color = TextSecondary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DateRange, null, modifier = Modifier.size(14.dp), tint = TextSecondary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Waktu Konsumsi: Pukul ${nextSchedule.scheduleTime}", fontSize = 14.sp, color = TextSecondary)
                            }
                            if (!nextSchedule.note.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp)).padding(12.dp)) {
                                    Column {
                                        Text("Catatan dari Caregiver:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                                        Text(text = nextSchedule.note, fontSize = 13.sp, color = Color(0xFF1B5E20), fontStyle = FontStyle.Italic)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { nextSchedule?.let(onTaken) },
                interactionSource = interactionSource,
                modifier = Modifier.fillMaxWidth().height(50.dp).graphicsLayer { scaleX = scale; scaleY = scale },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryMint, disabledContainerColor = InputBorder),
                shape = RoundedCornerShape(16.dp),
                enabled = nextSchedule != null && !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Surface)
                } else {
                    Icon(Icons.Default.CheckCircle, "Sudah Minum")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sudah Minum", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun ChoiceRow(options: List<String>, selected: String, onSelected: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        options.forEach { item ->
            Button(
                onClick = { onSelected(item) },
                modifier = Modifier.weight(1f).height(38.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (selected == item) PrimaryMint else CardMint),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = item, color = if (selected == item) Surface else TextPrimary, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun ScheduleList(schedules: List<MedicineSchedule>, logs: List<MedicineLog>) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Surface)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(shape = CircleShape, colors = CardDefaults.cardColors(containerColor = CardMint)) {
                    Icon(Icons.Default.DateRange, "Jadwal", tint = PrimaryMint, modifier = Modifier.padding(12.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Jadwal Kesehatan", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
            if (schedules.isEmpty()) {
                Text("Belum ada jadwal hari ini.", color = TextSecondary, fontSize = 14.sp)
            } else {
                schedules.forEachIndexed { index, schedule ->
                    ScheduleRow(schedule = schedule, isDone = logs.any { it.scheduleId == schedule.id })
                    if (index < schedules.lastIndex) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleRow(schedule: MedicineSchedule, isDone: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Waktu: Pukul ${schedule.scheduleTime}", fontSize = 13.sp, color = if (isDone) PrimaryMint else TextSecondary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = schedule.medicineName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (isDone) TextSecondary else TextPrimary)
            Text("Dosis: ${schedule.dosage ?: "-"}", fontSize = 14.sp, color = TextSecondary)
            if (!schedule.note.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text("Catatan: ${schedule.note}", fontSize = 13.sp, color = Color(0xFF388E3C), fontStyle = FontStyle.Italic)
            }
        }
        Icon(if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked, null, tint = if (isDone) PrimaryMint else TextSecondary, modifier = Modifier.size(28.dp))
    }
}
