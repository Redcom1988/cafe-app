package com.redcom1988.cafej3.screens.reward.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.theme.*

@Composable
fun RewardBalanceCard(currentPoints: Int) {
    val nextRewardTarget = ((currentPoints / 500) + 1) * 500
    val pointsNeeded = nextRewardTarget - currentPoints
    val progress = (currentPoints % 500).toFloat() / 500f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(brush = Brush.horizontalGradient(listOf(Orange2, PrimaryOrange)))
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = "Current Balance", color = White, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = currentPoints.formatPoints(), color = White, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Points", color = White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                }
            }
            Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = White, modifier = Modifier.size(28.dp))
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Gold Member", color = White, fontSize = 12.sp)
            Text(text = "$pointsNeeded to next reward", color = White, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = White,
            trackColor = Color(0xFF9A3A00)
        )
    }
}