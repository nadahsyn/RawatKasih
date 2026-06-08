package com.projekakhir.rawatkasih.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.projekakhir.rawatkasih.RawatKasihApplication
import com.projekakhir.rawatkasih.ui.theme.CardMint
import com.projekakhir.rawatkasih.ui.theme.PrimaryMint
import com.projekakhir.rawatkasih.ui.theme.Surface
import com.projekakhir.rawatkasih.viewmodel.ProfileViewModel
import com.projekakhir.rawatkasih.viewmodel.ViewModelFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.projekakhir.rawatkasih.RawatKasihHeader

@Composable
fun EditProfileScreen(
    userId: Long,
    onBack: () -> Unit,
    onLogout: () -> Unit = {},
    onProfileUpdated: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val factory = remember {
        val app = context.applicationContext as RawatKasihApplication
        ViewModelFactory(app.repository)
    }
    val viewModel: ProfileViewModel = viewModel(factory = factory)

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        uri?.let {

            val bytes = context.contentResolver
                .openInputStream(it)
                ?.readBytes()

            if (bytes != null) {
                viewModel.uploadProfileImage(
                    userId = userId,
                    bytes = bytes
                )
            }
        }
    }

    val user by viewModel.user.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val error by viewModel.error.collectAsState()

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }

    LaunchedEffect(user) {
        user?.let {
            name = it.name
            age = it.age?.toString() ?: ""
            phone = it.phone ?: ""
            gender = it.gender ?: ""
        }
    }

    LaunchedEffect(user) {
        println("USER IMAGE = ${user?.profileImage}")
    }

    Scaffold(
        topBar = { RawatKasihHeader() }
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
                Text(text = "Edit Profil", fontSize = 23.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Card(
                        shape = CircleShape,
                        modifier = Modifier.border(
                            1.dp,
                            PrimaryMint.copy(alpha = 0.3f),
                            CircleShape
                        ),
                        colors = CardDefaults.cardColors(containerColor = CardMint)
                    ) {

                        if (!user?.profileImage.isNullOrEmpty()) {

                            AsyncImage(
                                model = user?.profileImage,
                                contentDescription = "Foto Profil",
                                modifier = Modifier.size(90.dp)
                            )

                        } else {

                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(18.dp),
                                tint = PrimaryMint
                            )
                        }
                    }
                    FloatingActionButton(
                        onClick = {
                            imagePicker.launch("image/*") },
                        modifier = Modifier.size(38.dp),
                        containerColor = PrimaryMint
                    ) {
                        Icon(Icons.Default.PhotoCamera, "Ganti Foto", tint = Surface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name, onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(), label = { Text("Nama Lengkap") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = age, onValueChange = { age = it },
                modifier = Modifier.fillMaxWidth(), label = { Text("Umur") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Jenis Kelamin", fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = gender == "Laki-laki", onClick = { gender = "Laki-laki" })
                Text("Laki-laki")
                Spacer(modifier = Modifier.width(12.dp))
                RadioButton(selected = gender == "Perempuan", onClick = { gender = "Perempuan" })
                Text("Perempuan")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone, onValueChange = { phone = it },
                modifier = Modifier.fillMaxWidth(), label = { Text("Nomor Telepon") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(if (isPressed) 0.96f else 1f, label = "")

            Button(
                onClick = {
                    viewModel.updateProfile(userId, name, phone, age.toIntOrNull(), gender) { _ ->
                        onProfileUpdated()
                    }
                },
                enabled = !isSaving,
                interactionSource = interactionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .graphicsLayer { scaleX = scale; scaleY = scale },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryMint)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Surface
                    )
                } else {
                    Text(text = "Simpan Perubahan", color = Surface, fontWeight = FontWeight.SemiBold)
                }
            }

            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}