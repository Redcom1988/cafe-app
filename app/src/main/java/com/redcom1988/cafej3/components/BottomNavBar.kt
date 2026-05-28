package com.redcom1988.cafej3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.redcom1988.cafej3.R
import com.redcom1988.cafej3.screens.home.HomeScreen
import com.redcom1988.cafej3.screens.order.OrderScreen
import com.redcom1988.cafej3.screens.profile.ProfileScreen
import com.redcom1988.cafej3.screens.reward.RewardScreen
import com.redcom1988.cafej3.theme.*

@Composable
fun BottomNavBar(tabNavigator: TabNavigator) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .navigationBarsPadding()
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(40.dp))
                .background(backgroundColorNav)
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
                    selected = tabNavigator.current == HomeScreen,
                    activeColor = activeColor,
                    activeBackgroundColor = activeChoosedColor,
                    inactiveColor = inactiveColor,
                    onClick = { tabNavigator.current = HomeScreen }
                )
                BottomNavItem(
                    title = "MY ORDER",
                    icon = painterResource(id = R.drawable.ic_order),
                    selected = tabNavigator.current == OrderScreen,
                    activeColor = activeColor,
                    activeBackgroundColor = activeChoosedColor,
                    inactiveColor = inactiveColor,
                    onClick = { tabNavigator.current = OrderScreen }
                )
                BottomNavItem(
                    title = "REWARDS",
                    icon = painterResource(id = R.drawable.ic_rewards),
                    selected = tabNavigator.current == RewardScreen,
                    activeColor = activeColor,
                    activeBackgroundColor = activeChoosedColor,
                    inactiveColor = inactiveColor,
                    onClick = { tabNavigator.current = RewardScreen }
                )
                BottomNavItem(
                    title = "PROFILE",
                    icon = painterResource(id = R.drawable.ic_profile),
                    selected = tabNavigator.current == ProfileScreen,
                    activeColor = activeColor,
                    activeBackgroundColor = activeChoosedColor,
                    inactiveColor = inactiveColor,
                    onClick = { tabNavigator.current = ProfileScreen }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    title: String,
    icon: Painter,
    selected: Boolean,
    activeColor: Color,
    activeBackgroundColor: Color,
    inactiveColor: Color,
    onClick: () -> Unit
) {
    val contentColor = if (selected) activeColor else inactiveColor
    val fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
    val fontSize = if (selected) 11.sp else 10.sp

    val itemBackground = if (selected) activeBackgroundColor else Color.Transparent

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .background(itemBackground)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
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
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    }
}