package com.redcom1988.cafej3.screens.reward.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.model.TopUser
import com.redcom1988.cafej3.theme.*

@Composable
fun LeaderboardCard(users: List<TopUser>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(White)
            .padding(vertical = 4.dp)
    ) {
        users.forEachIndexed { index, user ->
            LeaderboardItem(user)
            if (index != users.lastIndex) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Cream))
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun LeaderboardItem(user: TopUser) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = user.rank.toString(),
            color = if (user.rank == 1) Orange2 else Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Image(
            painter = painterResource(id = user.image),
            contentDescription = null,
            modifier = Modifier.size(38.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = user.name, color = DarkBrown, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = user.status, color = Color.Gray, fontSize = 12.sp)
        }
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = user.points.formatPoints(), color = Orange2, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = "pts", color = Orange2, fontSize = 11.sp)
        }
    }
}