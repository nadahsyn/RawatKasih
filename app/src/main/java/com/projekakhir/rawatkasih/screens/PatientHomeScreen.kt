package com.projekakhir.rawatkasih.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projekakhir.rawatkasih.R
import com.projekakhir.rawatkasih.data.AuthResult
import com.projekakhir.rawatkasih.data.MedicineLog
import com.projekakhir.rawatkasih.data.MedicineSchedule
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import com.projekakhir.rawatkasih.ui.theme.CardMint
import com.projekakhir.rawatkasih.ui.theme.InputBorder
import com.projekakhir.rawatkasih.ui.theme.PrimaryMint
import com.projekakhir.rawatkasih.ui.theme.Surface
import com.projekakhir.rawatkasih.ui.theme.TextPrimary
import com.projekakhir.rawatkasih.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect

@Composable
fun PatientHomeScreen(
    initialSession: AuthResult,
    onEditProfile: () -> Unit,
    onOpenHealth: () -> Unit,
    successMessage: String?,
    onMessageShown: () -> Unit
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val user = initialSession.user
    var schedules by remember { mutableStateOf(initialSession.schedules) }
    var logs by remember { mutableStateOf(initialSession.logs) }
    var isSubmitting by remember { mutableStateOf(false) }
    var kondisi by remember { mutableStateOf(initialSession.condition?.condition ?: "") }
    var mood by remember { mutableStateOf(initialSession.condition?.mood ?: "") }
    var bloodPressure by remember { mutableStateOf(initialSession.condition?.bloodPressure ?: "") }
    var notes by remember {
        mutableStateOf(
            initialSession.condition?.notes ?: ""
        )
    }
    var isSavingCondition by remember { mutableStateOf(false) }
    var conditionSaved by remember {
        mutableStateOf(initialSession.condition != null)
    }
    var message by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(successMessage) {

        if (successMessage != null) {

            snackbarHostState.showSnackbar(
                successMessage
            )

            onMessageShown()
        }
    }
    LaunchedEffect(message) {

        if (message.isNotEmpty()) {

            snackbarHostState.showSnackbar(message)

            message = ""
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 56.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardMint)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.ava),
                                contentDescription = "Foto Profil",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 3.dp,
                                        color = Surface,
                                        shape = CircleShape
                                    )
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = user.name,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(7.dp))

                                Text(
                                    text = user.age?.let { "$it Tahun" }
                                        ?: "Profil belum lengkap",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                Text(
                                    text = user.gender
                                        ?: "Lengkapi data diri Anda",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            OutlinedButton(
                                onClick = onEditProfile,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(
                                    2.dp,
                                    PrimaryMint
                                )
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = PrimaryMint,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = "Edit Profil",
                                    color = PrimaryMint,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            OutlinedButton(
                                onClick = onOpenHealth,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(
                                    2.dp,
                                    PrimaryMint
                                )
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = PrimaryMint,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = "Kesehatan",
                                    color = PrimaryMint,
                                    fontWeight = FontWeight.SemiBold
                                )
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
                    onTaken = { schedule ->

                        if (isSubmitting) return@ReminderCard

                        scope.launch {
                            try {

                                isSubmitting = true

                                RawatKasihRepository.markMedicineTaken(schedule)

                                logs = RawatKasihRepository.loadPatientMedicineLogs(user.id)

                                android.util.Log.d(
                                    "REMINDER",
                                    "Jumlah log = ${logs.size}"
                                )

                                message = "Hebat! Jangan lupa tetap jaga kesehatan"

                            } catch (e: Exception) {

                                message = "Ups, status obat belum bisa disimpan. Coba lagi ya"

                            } finally {

                                isSubmitting = false

                            }
                        }
                    }
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
                            Card(
                                shape = CircleShape,
                                colors = CardDefaults.cardColors(containerColor = CardMint)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Kondisi",
                                    tint = PrimaryMint,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Kondisi Hari Ini",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Bagaimana kondisi tubuh hari ini?",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ChoiceRow(
                            options = listOf("Baik", "Pusing", "Lemas"),
                            selected = kondisi,
                            onSelected = { kondisi = it }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Bagaimana mood hari ini?",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ChoiceRow(
                            options = listOf("Senang", "Biasa", "Sedih"),
                            selected = mood,
                            onSelected = { mood = it }
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedTextField(
                            value = bloodPressure,
                            onValueChange = { bloodPressure = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Tekanan darah") },
                            placeholder = { Text("Contoh: 120/80") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryMint,
                                unfocusedBorderColor = InputBorder,
                                focusedLabelColor = PrimaryMint
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = notes,
                            onValueChange = {
                                notes = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text("Catatan Tambahan (Opsional)")
                            },
                            placeholder = {
                                Text("Tuliskan keluhan atau kondisi lain")
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryMint,
                                unfocusedBorderColor = InputBorder,
                                focusedLabelColor = PrimaryMint
                            ),
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        val conditionInteraction = remember { MutableInteractionSource() }

                        val conditionPressed by conditionInteraction.collectIsPressedAsState()

                        val conditionScale by animateFloatAsState(
                            targetValue = if (conditionPressed) 0.96f else 1f,
                            label = "conditionButtonScale"
                        )
                        Button(
                            onClick = {
                                val patientId = user.id ?: return@Button

                                isSavingCondition = true

                                scope.launch {
                                    try {
                                        RawatKasihRepository.saveDailyCondition(
                                            patientId = patientId,
                                            condition = kondisi,
                                            mood = mood,
                                            bloodPressure = bloodPressure,
                                            notes = notes
                                        )

                                        conditionSaved = true

                                        message = "Terima kasih sudah mengisi kondisi hari ini!"

                                    } catch (e: Exception) {

                                        message = "Ups, kondisi hari ini belum bisa disimpan. Coba lagi ya."

                                    } finally {

                                        isSavingCondition = false
                                    }
                                }
                            },
                            interactionSource = conditionInteraction,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .graphicsLayer {
                                    scaleX = conditionScale
                                    scaleY = conditionScale
                                },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryMint,
                                disabledContainerColor = InputBorder
                            ),
                            shape = RoundedCornerShape(16.dp),
                            enabled =
                                !conditionSaved &&
                                        !isSavingCondition &&
                                        user.id != null &&
                                        kondisi.isNotBlank() &&
                                        mood.isNotBlank() &&
                                        bloodPressure.isNotBlank()
                        ) {
                            when {

                                isSavingCondition -> {
                                    CircularProgressIndicator(
                                        color = Surface,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                conditionSaved -> {

                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = "Kondisi Tersimpan",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                else -> {
                                    Text(
                                        "Simpan Kondisi",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
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

@Composable
private fun ReminderCard(
    schedules: List<MedicineSchedule>,
    logs: List<MedicineLog>,
    isSubmitting: Boolean,
    onTaken: (MedicineSchedule) -> Unit
) {
    val nextSchedule = schedules.firstOrNull { schedule ->
        logs.none { it.scheduleId == schedule.id }
    }
    val interactionSource = remember { MutableInteractionSource() }

    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        label = "buttonScale"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = CardMint)
                ) {
                    Icon(
                        imageVector = Icons.Default.Medication,
                        contentDescription = "Obat",
                        tint = PrimaryMint,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Reminder Obat",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (nextSchedule == null) {
                        Text(
                            text = "Belum ada jadwal obat.",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = nextSchedule.scheduleTime,
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = nextSchedule.medicineName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = nextSchedule.dosage ?: "-",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = { nextSchedule?.let(onTaken) },

                interactionSource = interactionSource,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryMint,
                    disabledContainerColor = InputBorder
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = nextSchedule != null && !isSubmitting
            ) {
                if (isSubmitting) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Surface
                    )

                } else {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Sudah Minum"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Sudah Minum",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun ChoiceRow(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEach { item ->
            Button(
                onClick = { onSelected(item) },
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == item) PrimaryMint else CardMint
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = item,
                    color = if (selected == item) Surface else TextPrimary,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun ScheduleList(
    schedules: List<MedicineSchedule>,
    logs: List<MedicineLog>
) {
    Column(modifier = Modifier.padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = CardMint)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Jadwal",
                    tint = PrimaryMint,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Jadwal Kesehatan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (schedules.isEmpty()) {
            Text(
                text = "Belum ada jadwal hari ini.",
                color = TextSecondary,
                fontSize = 14.sp
            )
        } else {
            schedules.forEachIndexed { index, schedule ->
                ScheduleRow(
                    schedule = schedule,
                    isDone = logs.any { it.scheduleId == schedule.id }
                )
                if (index < schedules.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                }
            }
        }
    }
}

@Composable
private fun ScheduleRow(schedule: MedicineSchedule, isDone: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = schedule.scheduleTime,
                fontSize = 13.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${schedule.medicineName} ${schedule.dosage ?: ""}".trim(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {}) {
            Icon(
                imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = "Status checklist",
                tint = if (isDone) PrimaryMint else TextSecondary
            )
        }
    }
}
