package com.projekakhir.rawatkasih

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.projekakhir.rawatkasih.screens.PatientHomeScreen

class PatientActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PatientHomeScreen()
        }
    }
}