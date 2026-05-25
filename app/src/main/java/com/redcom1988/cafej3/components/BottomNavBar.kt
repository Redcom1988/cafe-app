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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.redcom1988.cafej3.R
import com.redcom1988.cafej3.screens.home.*
import com.redcom1988.cafej3.screens.order.OrderScreen
import com.redcom1988.cafej3.screens.profile.ProfileScreen
import com.redcom1988.cafej3.screens.reward.RewardScreen

@Composable
fun BottomNavBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {

    // COLOR PALETTE
    val backgroundColor = Color(0xFFFFFFFF)
    val activeColor = Color(0xFFDD5A00)
    val inactiveColor = Color(0xFFA8A29E)
    val navigator = LocalNavigator.currentOrThrow

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 10.dp)
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
                    navigator.push(HomeScreen)
                }
            )

            BottomNavItem(
                title = "MY ORDER",
                icon = painterResource(id = R.drawable.ic_order),
                selected = selectedItem == "MY ORDER",
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                onClick = {
                    navigator.push(OrderScreen)
                }
            )

            BottomNavItem(
                title = "REWARDS",
                icon = painterResource(id = R.drawable.ic_rewards),
                selected = selectedItem == "REWARDS",
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                onClick = {
                    navigator.push(RewardScreen)
                }
            )

            BottomNavItem(
                title = "PROFILE",
                icon = painterResource(id = R.drawable.ic_profile),
                selected = selectedItem == "PROFILE",
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                onClick = {
                    navigator.push(ProfileScreen)
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
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = icon,
            contentDescription = title,
            tint = contentColor,
            modifier = Modifier.size(22.dp)
        )

        Text(
            text = title,
            color = contentColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}