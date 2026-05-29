package com.projekakhir.rawatkasih.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projekakhir.rawatkasih.data.AppUser
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import com.projekakhir.rawatkasih.ui.theme.CardMint
import com.projekakhir.rawatkasih.ui.theme.PrimaryMint
import com.projekakhir.rawatkasih.ui.theme.Surface
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun EditProfileScreen(
    user: AppUser,
    onBack: () -> Unit,
    onProfileUpdated: (AppUser) -> Unit
) {

    var name by remember {
        mutableStateOf(user.name)
    }

    var age by remember {
        mutableStateOf(
            user.age?.toString() ?: ""
        )
    }

    val focusManager = LocalFocusManager.current

    var phone by remember {
        mutableStateOf(
            user.phone ?: ""
        )
    }

    var gender by remember {
        mutableStateOf(
            user.gender ?: ""
        )
    }
    val context = LocalContext.current

    var isSaving by remember {
        mutableStateOf(false)
    }
    Scaffold { paddingValues ->
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
                    text = "Edit Profil",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {

                    Card(
                        shape = CircleShape,
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = PrimaryMint.copy(alpha = 0.3f),
                            shape = CircleShape
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = CardMint
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .padding(18.dp),
                            tint = PrimaryMint
                        )
                    }

                    FloatingActionButton(
                        onClick = {

                        },
                        modifier = Modifier.size(38.dp),
                        containerColor = PrimaryMint
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Ganti Foto",
                            tint = Surface
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nama Lengkap") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Umur") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Jenis Kelamin",
                fontWeight = FontWeight.SemiBold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = gender == "Laki-laki",
                    onClick = {
                        gender = "Laki-laki"
                    }
                )

                Text("Laki-laki")

                Spacer(modifier = Modifier.width(12.dp))

                RadioButton(
                    selected = gender == "Perempuan",
                    onClick = {
                        gender = "Perempuan"
                    }
                )

                Text("Perempuan")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nomor Telepon") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            val interactionSource = remember {
                MutableInteractionSource()
            }

            val isPressed by interactionSource.collectIsPressedAsState()

            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.96f else 1f,
                label = "saveButtonScale"
            )
            val scope = rememberCoroutineScope()

            Button(
                onClick = {

                    scope.launch {
                        isSaving = true

                        try {

                            RawatKasihRepository.updateProfile(
                                userId = user.id ?: return@launch,
                                name = name,
                                phone = phone,
                                age = age.toIntOrNull(),
                                gender = gender.ifBlank { null }
                            )

                            val updatedUser =
                                RawatKasihRepository.loadUserById(
                                    user.id
                                )

                            focusManager.clearFocus()

                            onProfileUpdated(updatedUser)

                        } catch (e: Exception) {

                            e.printStackTrace()

                        } finally {

                            isSaving = false

                        }
                    }
                },

                enabled = !isSaving,
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
                    containerColor = PrimaryMint
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