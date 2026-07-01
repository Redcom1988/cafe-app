package com.redcom1988.cafej3.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class AppTab { Menu, CartOrder, Rewards, Financial, Profile }

data class BottomNavItem(
    val label: String,
    val tab: AppTab,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

fun bottomNavItems(): List<BottomNavItem> = listOf(
    BottomNavItem("Menu", AppTab.Menu, Icons.Filled.Menu, Icons.Outlined.Menu),
    BottomNavItem("My Order", AppTab.CartOrder, Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
    BottomNavItem("Rewards", AppTab.Rewards, Icons.Filled.Star, Icons.Outlined.Star),
    BottomNavItem("Profile", AppTab.Profile, Icons.Filled.Person, Icons.Outlined.Person),
)

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        items.forEach { item ->
            val selected = currentTab == item.tab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(text = item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
