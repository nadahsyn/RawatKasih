package com.projekakhir.rawatkasih

import androidx.compose.material.icons.filled.Favorite
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projekakhir.rawatkasih.ui.theme.*

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    fun validateAndLogin() {
        emailError = ""
        passwordError = ""
        var valid = true

        if (email.isEmpty()) {
            emailError = "Email tidak boleh kosong"
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Format email tidak valid"
            valid = false
        }

        if (password.isEmpty()) {
            passwordError = "Password tidak boleh kosong"
            valid = false
        } else if (password.length < 6) {
            passwordError = "Password minimal 6 karakter"
            valid = false
        }

        if (valid) {
            isLoading = true
            // TODO: ganti dengan API call
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp)
            .padding(top = 56.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ===== LOGO =====
        Surface(
            modifier = Modifier.size(90.dp),
            shape = CircleShape,
            color = Secondary,
            tonalElevation = 0.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Logo",
                    tint = Primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "RawatKasih",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Selamat Datang!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Masuk untuk melanjutkan memantau kesehatan pasien",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ===== EMAIL =====
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; emailError = "" },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = null, tint = Primary)
            },
            isError = emailError.isNotEmpty(),
            supportingText = {
                if (emailError.isNotEmpty()) Text(emailError, color = MaterialTheme.colorScheme.error)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = InputBorder,
                focusedLabelColor = Primary,
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ===== PASSWORD =====
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; passwordError = "" },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = null, tint = Primary)
            },
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(
                        text = if (passwordVisible) "Sembunyikan" else "Tampilkan",
                        fontSize = 11.sp,
                        color = Primary
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError.isNotEmpty(),
            supportingText = {
                if (passwordError.isNotEmpty()) Text(passwordError, color = MaterialTheme.colorScheme.error)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = InputBorder,
                focusedLabelColor = Primary,
            ),
            singleLine = true
        )

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            TextButton(onClick = { }) {
                Text("Lupa password?", color = Primary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ===== LOGIN BUTTON =====
        Button(
            onClick = { validateAndLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            enabled = !isLoading
        ) {
            Text(
                text = if (isLoading) "Memuat..." else "Masuk",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ===== DIVIDER =====
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = InputBorder)
            Text("  atau  ", color = TextGray, fontSize = 13.sp)
            HorizontalDivider(modifier = Modifier.weight(1f), color = InputBorder)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ===== REGISTER LINK =====
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Belum punya akun? ", color = TextGray, fontSize = 14.sp)
            TextButton(onClick = onNavigateToRegister) {
                Text(
                    "Daftar Sekarang",
                    color = Primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}