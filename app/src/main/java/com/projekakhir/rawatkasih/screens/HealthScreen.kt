package com.projekakhir.rawatkasih.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projekakhir.rawatkasih.data.HealthProfile
import com.projekakhir.rawatkasih.data.RawatKasihRepository
import com.projekakhir.rawatkasih.ui.theme.CardMint
import com.projekakhir.rawatkasih.ui.theme.PrimaryMint
import com.projekakhir.rawatkasih.ui.theme.Surface
import com.projekakhir.rawatkasih.ui.theme.TextSecondary

@Composable
fun HealthScreen(
    userId: Long,
    onBack: () -> Unit,
    onEditHealthProfile: () -> Unit,
    onOpenHistory: () -> Unit,
    successMessage: String?,
    onMessageShown: () -> Unit
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    var profile by remember {
        mutableStateOf<HealthProfile?>(null)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {

        profile =
            RawatKasihRepository.loadHealthProfile(
                userId
            )

        isLoading = false
    }

    LaunchedEffect(successMessage) {

        successMessage?.let {

            snackbarHostState.showSnackbar(it)

            onMessageShown()
        }
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
                    text = "Kesehatan",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Surface
                )
            ) {

                if (isLoading) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CircularProgressIndicator(
                            color = PrimaryMint
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "Memuat data kesehatan..."
                        )
                    }

                } else {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Card(
                                shape = CircleShape,
                                colors = CardDefaults.cardColors(
                                    containerColor = CardMint
                                )
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = PrimaryMint,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "Profil Kesehatan",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        HealthInfoRow(
                            label = "Tinggi Badan",
                            value = "${profile?.height ?: "-"} cm"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        HealthInfoRow(
                            label = "Berat Badan",
                            value = "${profile?.weight ?: "-"} kg"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        HealthInfoRow(
                            label = "Golongan Darah",
                            value = profile?.bloodType ?: "-"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        HealthInfoRow(
                            label = "Alergi",
                            value = profile?.allergy ?: "-"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        HealthInfoRow(
                            label = "Riwayat Penyakit",
                            value = profile?.medicalHistory ?: "-"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onEditHealthProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                    2.dp,
                    PrimaryMint
                )
            ) {

                Text(
                    text = "Edit Profil Kesehatan",
                    color = PrimaryMint,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onOpenHistory()
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Surface
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = CardMint
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = PrimaryMint,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Catatan Kesehatan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Lihat riwayat kondisi yang telah dicatat",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = ">",
                        fontSize = 20.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
@Composable
private fun HealthInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            color = TextSecondary
        )

        Text(
            text = value,
            fontWeight = FontWeight.Medium
        )
    }
}