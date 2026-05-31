package com.projekakhir.rawatkasih.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.projekakhir.rawatkasih.ui.theme.CardMint
import com.projekakhir.rawatkasih.ui.theme.PrimaryMint
import com.projekakhir.rawatkasih.ui.theme.Surface
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import kotlinx.coroutines.launch

@Composable
fun EditHealthProfileScreen(
    userId: Long,
    onBack: () -> Unit,
    onProfileSaved: () -> Unit
) {

    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var allergy by remember { mutableStateOf("") }
    var medicalHistory by remember { mutableStateOf("") }
    val isFormValid =
        height.isNotBlank() &&
                weight.isNotBlank() &&
                bloodType.isNotBlank() &&
                allergy.isNotBlank() &&
                medicalHistory.isNotBlank()
    val scope = rememberCoroutineScope()
    var isSaving by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        val profile =
            RawatKasihRepository.loadHealthProfile(userId)

        profile?.let {

            height = it.height?.toString() ?: ""

            weight = it.weight?.toString() ?: ""

            bloodType = it.bloodType ?: ""

            allergy = it.allergy ?: ""

            medicalHistory = it.medicalHistory ?: ""
        }
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
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
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onBack()
                        }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Edit Profil Kesehatan",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Tinggi Badan (cm)") },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Berat Badan (kg)") },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Golongan Darah",
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                listOf("A", "B", "AB", "O").forEach { type ->

                    FilterChip(
                        selected = bloodType == type,
                        onClick = {
                            bloodType = type
                        },
                        modifier = Modifier
                            .width(53.dp)
                            .height(42.dp),
                        label = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = type,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Surface,
                            labelColor = MaterialTheme.colorScheme.onSurface,

                            selectedContainerColor = CardMint,
                            selectedLabelColor = PrimaryMint
                        ),
                        border = BorderStroke(
                            width = if (bloodType == type) 2.dp else 1.dp,
                            color = if (bloodType == type)
                                PrimaryMint
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = allergy,
                onValueChange = { allergy = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Alergi") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = medicalHistory,
                onValueChange = { medicalHistory = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Riwayat Penyakit") },
                shape = RoundedCornerShape(12.dp),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            val interactionSource = remember {
                MutableInteractionSource()
            }

            val isPressed by interactionSource.collectIsPressedAsState()

            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.96f else 1f,
                label = "saveHealthScale"
            )

            Button(
                onClick = {

                    scope.launch {

                        isSaving = true

                        try {

                            RawatKasihRepository.saveHealthProfile(
                                patientId = userId,
                                height = height.toIntOrNull(),
                                weight = weight.toIntOrNull(),
                                bloodType = bloodType.ifBlank { null },
                                allergy = allergy.ifBlank { null },
                                medicalHistory = medicalHistory.ifBlank { null }
                            )
                            onProfileSaved()

                        } catch (e: Exception) {

                            e.printStackTrace()

                            println("ERROR SAVE HEALTH: ${e.message}")

                            snackbarHostState.showSnackbar(
                                message = e.message ?: "Terjadi kesalahan"
                            )
                        } finally {

                            isSaving = false

                        }
                    }
                },
                enabled = isFormValid && !isSaving,
                interactionSource = interactionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryMint,
                    disabledContainerColor =
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            ) {

            if (isSaving) {

                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Surface
                )

            } else {

                Text(
                    text = "Simpan Perubahan",
                    color = Surface,
                    fontWeight = FontWeight.SemiBold
                )
            }
            }
        }
    }
}