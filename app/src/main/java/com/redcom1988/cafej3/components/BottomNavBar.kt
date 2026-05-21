package com.redcom1988.cafej3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.R

@Composable
fun BottomNavBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {

    // COLOR PALETTE
    val backgroundColor = Color(0xFFFFFFFF)
    val activeColor = Color(0xFFDD5A00)
    val inactiveColor = Color(0xFFA8A29E)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 18.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BottomNavItem(
                title = "MENU",
                icon = painterResource(id = R.drawable.ic_menu),
                selected = selectedItem == "MENU",
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                onClick = {
                    onItemSelected("MENU")
                }
            )

            BottomNavItem(
                title = "MY ORDER",
                icon = painterResource(id = R.drawable.ic_order),
                selected = selectedItem == "MY ORDER",
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                onClick = {
                    onItemSelected("MY ORDER")
                }
            )

            BottomNavItem(
                title = "REWARDS",
                icon = painterResource(id = R.drawable.ic_rewards),
                selected = selectedItem == "REWARDS",
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                onClick = {
                    onItemSelected("REWARDS")
                }
            )

            BottomNavItem(
                title = "PROFILE",
                icon = painterResource(id = R.drawable.ic_profile),
                selected = selectedItem == "PROFILE",
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                onClick = {
                    onItemSelected("PROFILE")
                }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    title: String,
    icon: Painter,
    selected: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    onClick: () -> Unit
) {

    val containerColor =
        if (selected)
            activeColor
        else
            Color.Transparent

    val contentColor =
        if (selected)
            Color.White
        else
            inactiveColor

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .background(containerColor)
            .clickable {
                onClick()
            }
            .padding(horizontal = 22.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = icon,
            contentDescription = title,
            tint = contentColor,
            modifier = Modifier.size(28.dp)
        )

        Text(
            text = title,
            color = contentColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}