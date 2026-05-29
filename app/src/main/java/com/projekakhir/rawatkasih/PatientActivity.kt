package com.projekakhir.rawatkasih

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.projekakhir.rawatkasih.ui.theme.RawatKasihTheme

class PatientActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RawatKasihTheme {
                Text("Silakan login untuk membuka dashboard pasien.")
            }
        }
    }
}
