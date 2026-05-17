package com.projekakhir.rawatkasih.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummaryCard(
    title: String,
    value: String,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .width(110.dp)
            .height(100.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}