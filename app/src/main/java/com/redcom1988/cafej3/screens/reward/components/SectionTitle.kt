package com.redcom1988.cafej3.screens.reward.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.theme.*

@Composable
fun SectionTitle(title: String, action: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, color = SoftBrown, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = action, color = Orange2, fontSize = 13.sp)
    }
}