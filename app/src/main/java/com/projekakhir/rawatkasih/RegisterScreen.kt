package com.projekakhir.rawatkasih

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.projekakhir.rawatkasih.data.CaregiverOption
import com.projekakhir.rawatkasih.ui.theme.*
import com.projekakhir.rawatkasih.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit = {},
    onRegisterSuccess: () -> Unit = onNavigateBack,
    viewModel: AuthViewModel = viewModel()
) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var konfirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var konfirmPasswordVisible by remember { mutableStateOf(false) }

    val caregiverList by viewModel.caregivers.collectAsState()
    var selectedCaregiver by remember { mutableStateOf<CaregiverOption?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    
    val isLoading by viewModel.isLoading.collectAsState()
    val registerError by viewModel.error.collectAsState()

    var namaError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var konfirmPasswordError by remember { mutableStateOf("") }
    var caregiverError by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadCaregivers()
    }

    fun validateAndRegister() {
        namaError = ""; emailError = ""; phoneError = ""
        passwordError = ""; konfirmPasswordError = ""; caregiverError = ""
        var valid = true

        if (nama.isEmpty()) { namaError = "Nama tidak boleh kosong"; valid = false }
        else if (nama.length < 3) { namaError = "Nama minimal 3 karakter"; valid = false }

        if (email.isEmpty()) { emailError = "Email tidak boleh kosong"; valid = false }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Format email tidak valid"; valid = false
        }

        if (phone.isEmpty()) { phoneError = "Nomor HP tidak boleh kosong"; valid = false }
        else if (phone.length < 10) { phoneError = "Nomor HP tidak valid"; valid = false }

        if (password.isEmpty()) { passwordError = "Password tidak boleh kosong"; valid = false }
        else if (password.length < 6) { passwordError = "Password minimal 6 karakter"; valid = false }

        if (konfirmPassword.isEmpty()) { konfirmPasswordError = "Konfirmasi password tidak boleh kosong"; valid = false }
        else if (password != konfirmPassword) { konfirmPasswordError = "Password tidak cocok"; valid = false }

        if (selectedCaregiver == null) { caregiverError = "Pilih caregiver terlebih dahulu"; valid = false }

        if (valid) {
            viewModel.registerPatient(
                name = nama.trim(),
                email = email.trim(),
                phone = phone.trim(),
                password = password,
                caregiverId = selectedCaregiver!!.id,
                onSuccess = onRegisterSuccess
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp)
            .padding(top = 56.dp, bottom = 32.dp)
    ) {
        Surface(
            onClick = onNavigateBack,
            shape = CircleShape,
            color = Secondary,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Buat Akun", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "Daftarkan diri Anda untuk mulai menggunakan RawatKasih",
            fontSize = 13.sp, color = TextGray, lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it; namaError = "" },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nama Lengkap") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = Primary) },
            isError = namaError.isNotEmpty(),
            supportingText = { if (namaError.isNotEmpty()) Text(namaError, color = MaterialTheme.colorScheme.error) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary, unfocusedBorderColor = InputBorder, focusedLabelColor = Primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; emailError = "" },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = Primary) },
            isError = emailError.isNotEmpty(),
            supportingText = { if (emailError.isNotEmpty()) Text(emailError, color = MaterialTheme.colorScheme.error) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary, unfocusedBorderColor = InputBorder, focusedLabelColor = Primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it; phoneError = "" },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nomor HP") },
            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null, tint = Primary) },
            isError = phoneError.isNotEmpty(),
            supportingText = { if (phoneError.isNotEmpty()) Text(phoneError, color = MaterialTheme.colorScheme.error) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary, unfocusedBorderColor = InputBorder, focusedLabelColor = Primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = !dropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedCaregiver?.name ?: "",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = { Text("Pilih Caregiver") },
                leadingIcon = { Icon(Icons.Filled.Favorite, contentDescription = null, tint = Primary) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                isError = caregiverError.isNotEmpty(),
                supportingText = { if (caregiverError.isNotEmpty()) Text(caregiverError, color = MaterialTheme.colorScheme.error) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary, unfocusedBorderColor = InputBorder, focusedLabelColor = Primary
                )
            )

            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                if (caregiverList.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Memuat caregiver...", color = TextGray) },
                        onClick = { }
                    )
                } else {
                    caregiverList.forEach { caregiver ->
                        DropdownMenuItem(
                            text = { Text(caregiver.name, color = TextPrimary) },
                            onClick = {
                                selectedCaregiver = caregiver
                                caregiverError = ""
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; passwordError = "" },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = Primary) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "Sembunyikan" else "Tampilkan", fontSize = 11.sp, color = Primary)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError.isNotEmpty(),
            supportingText = { if (passwordError.isNotEmpty()) Text(passwordError, color = MaterialTheme.colorScheme.error) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary, unfocusedBorderColor = InputBorder, focusedLabelColor = Primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = konfirmPassword,
            onValueChange = { konfirmPassword = it; konfirmPasswordError = "" },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Konfirmasi Password") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = Primary) },
            trailingIcon = {
                IconButton(onClick = { konfirmPasswordVisible = !konfirmPasswordVisible }) {
                    Text(if (konfirmPasswordVisible) "Sembunyikan" else "Tampilkan", fontSize = 11.sp, color = Primary)
                }
            },
            visualTransformation = if (konfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = konfirmPasswordError.isNotEmpty(),
            supportingText = { if (konfirmPasswordError.isNotEmpty()) Text(konfirmPasswordError, color = MaterialTheme.colorScheme.error) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary, unfocusedBorderColor = InputBorder, focusedLabelColor = Primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(28.dp))

        if (registerError != null) {
            Text(
                text = registerError!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        Button(
            onClick = { validateAndRegister() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Daftar Sekarang",
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Sudah punya akun? ", color = TextGray, fontSize = 14.sp)
            TextButton(onClick = onNavigateBack) {
                Text("Masuk", color = Primary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
