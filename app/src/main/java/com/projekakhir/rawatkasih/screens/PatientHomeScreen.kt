package com.projekakhir.rawatkasih.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import com.projekakhir.rawatkasih.R
import androidx.compose.material3.CardDefaults
import com.projekakhir.rawatkasih.ui.theme.CardMint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.projekakhir.rawatkasih.ui.theme.PrimaryMint
import com.projekakhir.rawatkasih.ui.theme.Surface
import com.projekakhir.rawatkasih.ui.theme.TextSecondary
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.projekakhir.rawatkasih.ui.theme.TextPrimary
import androidx.compose.foundation.lazy.LazyColumn
@Composable
fun PatientHomeScreen() {
    var kondisi by remember { mutableStateOf("Baik") }
    var mood by remember { mutableStateOf("Senang") }
    var obatPagi by remember { mutableStateOf(true) }
    var cekTensi by remember { mutableStateOf(false) }
    var jalanSantai by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 56.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardMint
                )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ava),
                        contentDescription = "Foto Profil",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Column {
                        Text(
                            text = "Siti Muzdalifah (Oma Siti)",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "71 Tahun",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Perempuan",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Surface
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = CardMint
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Medication,
                                contentDescription = "Obat",
                                tint = PrimaryMint,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .padding(top = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Reminder Obat",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "08.00 WIB",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "Amlodipine 5mg",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "1 tablet",
                                        fontSize = 13.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "❝",
                            fontSize = 18.sp,
                            color = PrimaryMint
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Jangan lupa minum obat ya",
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic,
                            color = TextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(17.dp))
                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryMint
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Sudah Minum"
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Sudah Minum",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Surface
                )
            ) {
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
                                contentDescription = "Kondisi",
                                tint = PrimaryMint,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kondisi Hari Ini",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Bagaimana kondisi tubuh hari ini?",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf("Baik", "Pusing", "Lemas").forEach { item ->
                            Button(
                                onClick = {
                                    kondisi = item
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(35.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
                                        if (kondisi == item) PrimaryMint
                                        else CardMint
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    text = item,
                                    color = if (kondisi == item)
                                        Surface
                                    else TextPrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Bagaimana mood hari ini?",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf("Senang", "Biasa", "Sedih").forEach { item ->
                            Button(
                                onClick = {
                                    mood = item
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(35.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
                                        if (mood == item) PrimaryMint
                                        else CardMint
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    text = item,
                                    color = if (mood == item)
                                        Surface
                                    else TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
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
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Jadwal",
                            tint = PrimaryMint,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Jadwal Kesehatan",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "08.00 WIB",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Minum obat pagi",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            obatPagi = !obatPagi
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (obatPagi)
                                    Icons.Default.CheckCircle
                                else
                                    Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Checklist",
                            tint =
                                if (obatPagi)
                                    PrimaryMint
                                else
                                    TextSecondary
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "13.00 WIB",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Cek tekanan darah",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            cekTensi = !cekTensi
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (cekTensi)
                                    Icons.Default.CheckCircle
                                else
                                    Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Checklist",
                            tint =
                                if (cekTensi)
                                    PrimaryMint
                                else
                                    TextSecondary
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "17.00 WIB",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Jalan santai",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            jalanSantai = !jalanSantai
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (jalanSantai)
                                    Icons.Default.CheckCircle
                                else
                                    Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Checklist",
                            tint =
                                if (jalanSantai)
                                    PrimaryMint
                                else
                                    TextSecondary
                        )
                    }
                }
            }
        }
    }
}
