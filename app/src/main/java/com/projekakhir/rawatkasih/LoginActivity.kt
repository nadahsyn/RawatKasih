package com.projekakhir.rawatkasih

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.projekakhir.rawatkasih.ui.theme.RawatKasihTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RawatKasihTheme {
                var showRegister by remember { mutableStateOf(false) }

                if (showRegister) {
                    RegisterScreen(
                        onNavigateBack = { showRegister = false }
                    )
                } else {
                    LoginScreen(
                        onNavigateToRegister = { showRegister = true }
                    )
                }
            }
        }
    }
}