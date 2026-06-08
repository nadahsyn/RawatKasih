package com.projekakhir.rawatkasih.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.projekakhir.rawatkasih.components.PatientCard
import com.projekakhir.rawatkasih.components.SearchBar
import com.projekakhir.rawatkasih.components.SummaryCard
import com.projekakhir.rawatkasih.data.AppUser
import com.projekakhir.rawatkasih.data.MedicineSchedule
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import com.projekakhir.rawatkasih.data.local.RawatKasihDatabase
import com.projekakhir.rawatkasih.model.Patient
import com.projekakhir.rawatkasih.viewmodel.CaregiverViewModel
import com.projekakhir.rawatkasih.RawatKasihHeader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverHomeScreen(
    user: AppUser,
    onLogout: () -> Unit = {},
    viewModel: CaregiverViewModel = viewModel()
) {
    val patients by viewModel.filteredPatients.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    var selectedPatient by remember { mutableStateOf<Patient?>(null) }

    LaunchedEffect(user.id) {
        viewModel.loadPatients(user.id)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFBFDFF))) {
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF63C7B2), Color(0xFFFBFDFF).copy(alpha = 0f))
                    ),
                    alpha = 0.15f
                )
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                RawatKasihHeader(onLogout = onLogout)
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Halo,",
                                fontSize = 18.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${user.name} 👋",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1E1E)
                            )
                        }
                        Text(
                            text = "Ayo pantau kondisi pasien harian!",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1E1E1E),
                            lineHeight = 38.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryCard(
                            title = "Pasien Aktif",
                            value = patients.size.toString(),
                            backgroundColor = Color(0xFFE3F2FD),
                            icon = Icons.Default.Person,
                            iconColor = Color(0xFF2196F3)
                        )
                        SummaryCard(
                            title = "Butuh Obat",
                            value = patients.count { it.medicineProgress < 1f && it.hasSchedule }.toString(),
                            backgroundColor = Color(0xFFE0F2F1),
                            icon = Icons.Default.MedicalServices,
                            iconColor = Color(0xFF009688)
                        )
                        SummaryCard(
                            title = "Perhatian",
                            value = patients.count { it.condition.lowercase() == "darurat" || it.condition.lowercase() == "lemas" }.toString(),
                            backgroundColor = Color(0xFFFFF3E0),
                            icon = Icons.Default.Warning,
                            iconColor = Color(0xFFFB8C00)
                        )
                    }
                }

                item {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { viewModel.onSearchQueryChange(it) }
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (searchQuery.isEmpty()) "Daftar Pasien" else "Hasil Pencarian",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E1E1E)
                        )
                        TextButton(
                            onClick = { viewModel.loadPatients(user.id) },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF63C7B2))
                        ) {
                            Text("Segarkan", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF63C7B2))
                        }
                    }
                } else if (errorMessage.isNotEmpty()) {
                    item {
                        Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(16.dp))
                    }
                } else if (patients.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
                        ) {
                            Column(
                                modifier = Modifier.padding(40.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Person, null, modifier = Modifier.size(64.dp), tint = Color(0xFFEEEEEE))
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = if (searchQuery.isEmpty()) "Belum ada pasien terhubung" else "Pasien tidak ditemukan",
                                    color = Color.Gray,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                } else {
                    items(patients) { patient ->
                        PatientCard(
                            patient = patient,
                            onDetailClick = { selectedPatient = patient }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        selectedPatient?.let { patient ->
            if (patient.hasSchedule) {
                PatientMonitoringDialog(
                    patient = patient,
                    onDismiss = { selectedPatient = null },
                    onRefresh = { viewModel.loadPatients(user.id) }
                )
            } else {
                MedicineScheduleFormDialog(
                    patientId = patient.id,
                    patientName = patient.name,
                    onDismiss = { selectedPatient = null },
                    onSuccess = {
                        selectedPatient = null
                        viewModel.loadPatients(user.id)
                    },
                    onSave = { schedule, onSuccess ->
                        viewModel.saveMedicineSchedule(schedule, user.id, onSuccess)
                    }
                )
            }
        }
    }
}

@Composable
fun MedicineScheduleFormDialog(
    patientId: Long,
    patientName: String,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    onSave: (MedicineSchedule, () -> Unit) -> Unit
) {
    var medicineName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.background(Color(0xFFF5F5F5), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(20.dp))
                    }
                    Text(
                        text = "Atur Jadwal Obat",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Box(modifier = Modifier.size(48.dp))
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp).background(Color(0xFF63C7B2), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Berikan instruksi untuk", fontSize = 12.sp, color = Color(0xFF065F46))
                            Text(patientName, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF065F46))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = medicineName,
                    onValueChange = { medicineName = it },
                    label = { Text("Nama Obat") },
                    placeholder = { Text("Contoh: Paracetamol") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF63C7B2),
                        focusedLabelColor = Color(0xFF63C7B2),
                        unfocusedBorderColor = Color(0xFFEEEEEE)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it },
                        label = { Text("Dosis") },
                        placeholder = { Text("1 Tab") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color(0xFFEEEEEE))
                    )
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Waktu") },
                        placeholder = { Text("08:00") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color(0xFFEEEEEE))
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Catatan Khusus") },
                    placeholder = { Text("Misal: Sesudah makan") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color(0xFFEEEEEE))
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (medicineName.isNotBlank() && time.isNotBlank()) {
                            isSaving = true
                            onSave(
                                MedicineSchedule(
                                    patientId = patientId,
                                    medicineName = medicineName,
                                    dosage = dosage,
                                    scheduleTime = time,
                                    note = note
                                )
                            ) {
                                isSaving = false
                                onSuccess()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                    enabled = !isSaving
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Simpan Jadwal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PatientMonitoringDialog(
    patient: Patient,
    onDismiss: () -> Unit,
    onRefresh: () -> Unit
) {
    var schedules by remember { mutableStateOf<List<MedicineSchedule>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showAddMedicine by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val db = remember { RawatKasihDatabase.getDatabase(context) }
    val repository = remember { RawatKasihRepository(db.dao()) }

    LaunchedEffect(patient.id) {
        schedules = repository.loadMedicineSchedules(patient.id)
        isLoading = false
    }

    if (showAddMedicine) {
        MedicineScheduleFormDialog(
            patientId = patient.id,
            patientName = patient.name,
            onDismiss = { showAddMedicine = false },
            onSuccess = {
                showAddMedicine = false
                onRefresh()
            },
            onSave = { schedule, onSuccess ->
                scope.launch {
                    repository.saveMedicineSchedule(schedule)
                    onSuccess()
                }
            }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFBFDFF)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Column {
                        Text(
                            text = "Monitoring Harian",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF63C7B2),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = patient.name,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1E1E1E)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd).background(Color(0xFFF5F5F5), CircleShape).size(40.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(20.dp))
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(32.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(patient.statusColor, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Status: ${patient.condition}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp,
                                        color = Color(0xFF1E1E1E)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    InfoBox(label = "Mood", value = patient.mood, icon = "😊", modifier = Modifier.weight(1f))
                                    InfoBox(label = "Tensi", value = patient.bloodPressure, icon = "🩸", modifier = Modifier.weight(1f))
                                }
                                
                                if (patient.notes != "-" && patient.notes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFFFF9C4).copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                                            .padding(20.dp)
                                    ) {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Info, null, modifier = Modifier.size(16.dp), tint = Color(0xFFFBC02D))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Catatan Pasien", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF827717))
                                            }
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = patient.notes,
                                                fontSize = 15.sp,
                                                color = Color(0xFF424242),
                                                lineHeight = 22.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Progress Obat", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1E1E))
                                    Text("${patient.takenMedicine} dari ${patient.totalMedicine} Obat Selesai", fontSize = 14.sp, color = Color.Gray)
                                }
                                IconButton(
                                    onClick = { showAddMedicine = true },
                                    modifier = Modifier.background(Color(0xFF1E1E1E), RoundedCornerShape(14.dp)).size(44.dp)
                                ) {
                                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(24.dp))
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            LinearProgressIndicator(
                                progress = { patient.medicineProgress },
                                modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                                color = Color(0xFF63C7B2),
                                trackColor = Color(0xFFF0F0F0)
                            )
                        }
                    }

                    if (isLoading) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color(0xFF63C7B2))
                            }
                        }
                    } else {
                        items(schedules) { schedule ->
                            MedicineDetailItem(schedule = schedule, isTaken = patient.medicineStatus == "Lengkap")
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(40.dp)) }
                }
            }
        }
    }
}

@Composable
fun InfoBox(label: String, value: String, icon: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color(0xFFF8F9FA), RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        Text(text = "$icon $label", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1E1E))
    }
}

@Composable
fun MedicineDetailItem(schedule: MedicineSchedule, isTaken: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(28.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(52.dp).background(if (isTaken) Color(0xFFE0FDF4) else Color(0xFFF8F9FA), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.MedicalServices, 
                        null, 
                        tint = if (isTaken) Color(0xFF63C7B2) else Color(0xFFBDBDBD),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = schedule.medicineName, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF1E1E1E))
                    Text(text = "Pukul ${schedule.scheduleTime} WIB", color = Color(0xFF63C7B2), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Surface(
                    color = if (isTaken) Color(0xFFE0FDF4) else Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (isTaken) "Selesai" else "Belum",
                        color = if (isTaken) Color(0xFF047857) else Color(0xFF757575),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color(0xFFF8F9FA), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Dosis", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = schedule.dosage ?: "1 Tablet", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1E1E))
            }
            
            if (!schedule.note.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFFF8F9FA), RoundedCornerShape(14.dp)).padding(12.dp)
                ) {
                    Text(text = "Catatan Caregiver:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Gray)
                    Text(
                        text = schedule.note, 
                        fontSize = 13.sp, 
                        color = Color(0xFF455A64), 
                        fontStyle = FontStyle.Italic,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
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
