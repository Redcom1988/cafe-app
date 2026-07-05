package com.redcom1988.cafej3.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.redcom1988.cafej3.screens.guest.LocalIsGuest

val LocalTabSwitcher = compositionLocalOf<(AppTab) -> Unit> { {} }

enum class AppTab { Menu, CartOrder, Rewards, Profile, Financial }

private val tabMap = mapOf(
    AppTab.Menu to MenuTab,
    AppTab.CartOrder to CartOrderTab,
    AppTab.Rewards to RewardsTab,
    AppTab.Profile to ProfileTab,
    AppTab.Financial to FinancialTab,
)

private val fullTabs = listOf(MenuTab, CartOrderTab, RewardsTab, ProfileTab)
private val guestTabs = listOf(MenuTab, CartOrderTab)

data class MainScreen(val isGuest: Boolean = false) : Screen {

    @Suppress("unused")
    private fun readResolve(): Any = MainScreen(isGuest)

    @Composable
    override fun Content() {
        val tabs = if (isGuest) guestTabs else fullTabs
        val navigator = LocalNavigator.currentOrThrow

        TabNavigator(tab = MenuTab) { tabNavigator ->
            CompositionLocalProvider(LocalNavigator provides navigator) {
                val density = LocalDensity.current

                Scaffold(
                    bottomBar = {
                        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                            tabs.forEach { tab ->
                                val selected = tabNavigator.current.key == tab.key
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { tabNavigator.current = tab },
                                    icon = { tab.options.icon?.let { Icon(painter = it, contentDescription = tab.options.title) } },
                                    label = {
                                        Text(
                                            tab.options.title,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    ),
                                )
                            }
                        }
                    }
                ) { padding ->
                    val bottomBarHeight = with(density) {
                        (padding.calculateBottomPadding().toPx() - WindowInsets.systemBars.getBottom(density))
                            .toDp()
                            .coerceAtLeast(0.dp)
                    }
                    Box(modifier = Modifier.padding(bottom = bottomBarHeight)) {
                        CompositionLocalProvider(
                            LocalIsGuest provides isGuest,
                            LocalTabSwitcher provides { tab ->
                                tabMap[tab]?.let { tabNavigator.current = it }
                            }
                        ) {
                            AnimatedContent(
                                targetState = tabNavigator.current,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "tabContent",
                            ) {
                                tabNavigator.saveableState(key = "currentTab", it) {
                                    it.Content()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
