package com.redcom1988.cafej3.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.redcom1988.cafej3.components.AppTab
import com.redcom1988.cafej3.components.BottomNavBar
import com.redcom1988.cafej3.components.bottomNavItems
import com.redcom1988.cafej3.screens.cart.CartOrderScreen
import com.redcom1988.cafej3.screens.financial.FinancialLedgerScreen
import com.redcom1988.cafej3.screens.menu.MenuScreen
import com.redcom1988.cafej3.screens.profile.ProfileScreen
import com.redcom1988.cafej3.screens.rewards.RewardsScreen

val LocalTabSwitcher = compositionLocalOf<(AppTab) -> Unit> { {} }

data object MainScreen : Screen {

    @Suppress("unused")
    private fun readResolve(): Any = MainScreen

    @Composable
    override fun Content() {
        val items = remember { bottomNavItems() }
        val tabSaver = remember { Saver<AppTab, Int>(save = { it.ordinal }, restore = { AppTab.entries[it] }) }
        var currentTab by rememberSaveable(stateSaver = tabSaver) { mutableStateOf(AppTab.Menu) }
        val density = LocalDensity.current

        Scaffold(
            bottomBar = {
                BottomNavBar(
                    items = items,
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it }
                )
            }
        ) { padding ->
            val bottomBarHeight = with(density) {
                (padding.calculateBottomPadding().toPx() - WindowInsets.systemBars.getBottom(density))
                    .toDp()
                    .coerceAtLeast(0.dp)
            }
            Box(modifier = Modifier.padding(bottom = bottomBarHeight)) {
                CompositionLocalProvider(LocalTabSwitcher provides { tab -> currentTab = tab }) {
                    when (currentTab) {
                        AppTab.Menu -> MenuScreen.Content()
                        AppTab.CartOrder -> CartOrderScreen.Content()
                        AppTab.Rewards -> RewardsScreen.Content()
                        AppTab.Profile -> ProfileScreen.Content()
                        AppTab.Financial -> FinancialLedgerScreen.Content()
                    }
                }
            }
        }
    }
}
