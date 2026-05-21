package com.redcom1988.cafej3.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.redcom1988.cafej3.theme.*

@Composable
fun CategoryTabs() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        TabItem(
            text = "Menu",
            selected = true
        )

        TabItem(
            text = "Appetizers"
        )

        TabItem(
            text = "Main Course"
        )
    }
}

@Composable
fun TabItem(
    text: String,
    selected: Boolean = false
) {

    Box(
        modifier = Modifier
            .background(
                color = if (selected) PrimaryOrange else Cream,
                shape = CircleShape
            )
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {

        Text(
            text = text,
            color = if (selected) Color.White else Color.Black
        )
    }
}