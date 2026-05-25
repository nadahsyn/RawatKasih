package com.projekakhir.rawatkasih

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.projekakhir.rawatkasih.screen.CaregiverHomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RegisterScreen()
        }
    }
}