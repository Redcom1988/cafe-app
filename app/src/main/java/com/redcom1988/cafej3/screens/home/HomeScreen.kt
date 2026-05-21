package com.redcom1988.cafej3.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.redcom1988.cafej3.screens.home.components.*
import com.redcom1988.cafej3.components.*

object HomeScreen : Screen {

    private fun readResolve(): Any = HomeScreen

    @Composable
    override fun Content() {
        val selectedItem = remember { mutableStateOf("MENU") }

        Scaffold(
            bottomBar = {
                BottomNavBar(
                    selectedItem = selectedItem.value,
                    onItemSelected = { selectedItem.value = it }
                )
            }
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(com.redcom1988.cafej3.theme.Background)
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                item {
                    HeroSection()
                }

                item {
                    CategoryTabs()
                }

                items(5) {

                    FoodCard()
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}