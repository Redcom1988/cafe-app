package com.redcom1988.cafej3.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.redcom1988.cafej3.screens.cart.CartOrderScreen
import com.redcom1988.cafej3.screens.financial.FinancialLedgerScreen
import com.redcom1988.cafej3.screens.menu.MenuScreen
import com.redcom1988.cafej3.screens.profile.ProfileScreen
import com.redcom1988.cafej3.screens.rewards.RewardsScreen

data object MenuTab : Tab {
    @Suppress("unused")
    private fun readResolve(): Any = MenuTab

    override val options: TabOptions
        @Composable get() = TabOptions(
            index = 0u,
            title = "Menu",
            icon = rememberVectorPainter(Icons.Filled.Menu),
        )

    @Composable
    override fun Content() {
        MenuScreen.Content()
    }
}

data object CartOrderTab : Tab {
    @Suppress("unused")
    private fun readResolve(): Any = CartOrderTab

    override val options: TabOptions
        @Composable get() = TabOptions(
            index = 1u,
            title = "My Order",
            icon = rememberVectorPainter(Icons.Filled.ShoppingCart),
        )

    @Composable
    override fun Content() {
        CartOrderScreen.Content()
    }
}

data object RewardsTab : Tab {
    @Suppress("unused")
    private fun readResolve(): Any = RewardsTab

    override val options: TabOptions
        @Composable get() = TabOptions(
            index = 2u,
            title = "Rewards",
            icon = rememberVectorPainter(Icons.Filled.Star),
        )

    @Composable
    override fun Content() {
        RewardsScreen.Content()
    }
}

data object ProfileTab : Tab {
    @Suppress("unused")
    private fun readResolve(): Any = ProfileTab

    override val options: TabOptions
        @Composable get() = TabOptions(
            index = 3u,
            title = "Profile",
            icon = rememberVectorPainter(Icons.Filled.Person),
        )

    @Composable
    override fun Content() {
        ProfileScreen.Content()
    }
}

data object FinancialTab : Tab {
    @Suppress("unused")
    private fun readResolve(): Any = FinancialTab

    override val options: TabOptions
        @Composable get() = TabOptions(
            index = 4u,
            title = "Financial",
            icon = rememberVectorPainter(Icons.Filled.Menu),
        )

    @Composable
    override fun Content() {
        FinancialLedgerScreen.Content()
    }
}

val appTabs = listOf(MenuTab, CartOrderTab, RewardsTab, ProfileTab)
