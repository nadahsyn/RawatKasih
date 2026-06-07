package com.projekakhir.rawatkasih.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.projekakhir.rawatkasih.RawatKasihApplication
import com.projekakhir.rawatkasih.ui.theme.CardMint
import com.projekakhir.rawatkasih.ui.theme.PrimaryMint
import com.projekakhir.rawatkasih.ui.theme.Surface
import com.projekakhir.rawatkasih.viewmodel.HealthViewModel
import com.projekakhir.rawatkasih.viewmodel.ViewModelFactory

@Composable
fun EditHealthProfileScreen(
    userId: Long,
    onBack: () -> Unit,
    onProfileSaved: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val factory = remember {
        val app = context.applicationContext as RawatKasihApplication
        ViewModelFactory(app.repository)
    }
    val viewModel: HealthViewModel = viewModel(factory = factory)

    val profile by viewModel.healthProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var allergy by remember { mutableStateOf("") }
    var medicalHistory by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        viewModel.loadHealthProfile(userId)
    }

    LaunchedEffect(profile) {
        profile?.let {
            height = it.height?.toString() ?: ""
            weight = it.weight?.toString() ?: ""
            bloodType = it.bloodType ?: ""
            allergy = it.allergy ?: ""
            medicalHistory = it.medicalHistory ?: ""
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    error?.let {
        LaunchedEffect(it) {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
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
                Text(text = "Edit Profil Kesehatan", fontSize = 23.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = height, onValueChange = { height = it },
                modifier = Modifier.fillMaxWidth(), label = { Text("Tinggi Badan (cm)") },
                shape = RoundedCornerShape(12.dp), singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = weight, onValueChange = { weight = it },
                modifier = Modifier.fillMaxWidth(), label = { Text("Berat Badan (kg)") },
                shape = RoundedCornerShape(12.dp), singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Golongan Darah", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(15.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("A", "B", "AB", "O").forEach { type ->
                    FilterChip(
                        selected = bloodType == type,
                        onClick = { bloodType = type },
                        modifier = Modifier.width(53.dp).height(42.dp),
                        label = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) { Text(text = type) }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CardMint,
                            selectedLabelColor = PrimaryMint
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = allergy, onValueChange = { allergy = it },
                modifier = Modifier.fillMaxWidth(), label = { Text("Alergi") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = medicalHistory, onValueChange = { medicalHistory = it },
                modifier = Modifier.fillMaxWidth(), label = { Text("Riwayat Penyakit") },
                shape = RoundedCornerShape(12.dp), minLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(if (isPressed) 0.96f else 1f, label = "")

            Button(
                onClick = {
                    viewModel.saveHealthProfile(
                        userId, height.toIntOrNull(), weight.toIntOrNull(),
                        bloodType, allergy, medicalHistory
                    ) {
                        onProfileSaved()
                    }
                },
                enabled = !isLoading,
                interactionSource = interactionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .graphicsLayer { scaleX = scale; scaleY = scale },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryMint)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Surface
                    )
                } else {
                    Text(text = "Simpan Perubahan", color = Surface, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}