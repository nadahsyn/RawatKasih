package com.projekakhir.rawatkasih

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private lateinit var btnBack: android.widget.ImageButton
    private lateinit var tilNama: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPhone: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilKonfirmPassword: TextInputLayout
    private lateinit var etNama: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etKonfirmPassword: TextInputEditText
    private lateinit var btnRegister: MaterialButton
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        tilNama = findViewById(R.id.tilNama)
        tilEmail = findViewById(R.id.tilEmail)
        tilPhone = findViewById(R.id.tilPhone)
        tilPassword = findViewById(R.id.tilPassword)
        tilKonfirmPassword = findViewById(R.id.tilKonfirmPassword)
        etNama = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        etKonfirmPassword = findViewById(R.id.etKonfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish() // kembali ke halaman sebelumnya
        }

        tvLogin.setOnClickListener {
            finish() // kembali ke LoginActivity
        }

        btnRegister.setOnClickListener {
            if (validateInput()) {
                performRegister()
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        val nama = etNama.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val konfirmPassword = etKonfirmPassword.text.toString().trim()

        // Reset semua error
        tilNama.error = null
        tilEmail.error = null
        tilPhone.error = null
        tilPassword.error = null
        tilKonfirmPassword.error = null

        if (nama.isEmpty()) {
            tilNama.error = "Nama tidak boleh kosong"
            isValid = false
        } else if (nama.length < 3) {
            tilNama.error = "Nama minimal 3 karakter"
            isValid = false
        }

        if (email.isEmpty()) {
            tilEmail.error = "Email tidak boleh kosong"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Format email tidak valid"
            isValid = false
        }

        if (phone.isEmpty()) {
            tilPhone.error = "Nomor HP tidak boleh kosong"
            isValid = false
        } else if (phone.length < 10) {
            tilPhone.error = "Nomor HP tidak valid"
            isValid = false
        }

        if (password.isEmpty()) {
            tilPassword.error = "Password tidak boleh kosong"
            isValid = false
        } else if (password.length < 6) {
            tilPassword.error = "Password minimal 6 karakter"
            isValid = false
        }

        if (konfirmPassword.isEmpty()) {
            tilKonfirmPassword.error = "Konfirmasi password tidak boleh kosong"
            isValid = false
        } else if (password != konfirmPassword) {
            tilKonfirmPassword.error = "Password tidak cocok"
            isValid = false
        }

        return isValid
    }

    private fun performRegister() {
        setLoadingState(true)

        // TODO: ganti dengan API call ke backend
        btnRegister.postDelayed({
            setLoadingState(false)
            onRegisterSuccess()
        }, 1000)
    }

    private fun setLoadingState(isLoading: Boolean) {
        btnRegister.isEnabled = !isLoading
        btnRegister.text = if (isLoading) "Memuat..." else "Daftar Sekarang"
    }

    private fun onRegisterSuccess() {
        Toast.makeText(this, "Registrasi berhasil! Silakan masuk.", Toast.LENGTH_LONG).show()
        finish() // kembali ke LoginActivity
    }
}