package com.example.project2_cse535.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PageTitleHeader(name: String) {
    Text(
        modifier = Modifier.padding(24.dp),
        text = name,
        style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)
    )
}