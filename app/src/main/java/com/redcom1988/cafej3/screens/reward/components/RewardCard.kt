package com.redcom1988.cafej3.screens.reward.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.model.RewardItem
import com.redcom1988.cafej3.theme.*

@Composable
fun RewardCard(
    item: RewardItem,
    isAffordable: Boolean,
    onRedeem: (RewardItem) -> Unit
) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(White)
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(id = item.image),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "${item.cost.formatPoints()} Points", color = Orange2, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = item.title, color = DarkBrown, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (isAffordable) PrimaryOrange else Cream)
                .clickable(enabled = isAffordable) { onRedeem(item) }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isAffordable) "Redeem Now" else "Not Enough Points",
                color = if (isAffordable) White else SoftBrown,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}