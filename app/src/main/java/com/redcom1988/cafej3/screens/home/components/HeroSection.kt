package com.redcom1988.cafej3.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.theme.*

@Composable
fun HeroSection() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Orange2,
                        DarkBrown
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
            )
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                text = "SIGNATURE EXPERIENCE",
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Crafted with\nPassion",
                color = androidx.compose.ui.graphics.Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                lineHeight = 38.sp
            )
        }
    }
}