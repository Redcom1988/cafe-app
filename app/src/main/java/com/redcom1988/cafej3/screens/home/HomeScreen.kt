package com.redcom1988.cafej3.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.redcom1988.cafej3.screens.home.components.*
import com.redcom1988.cafej3.components.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.redcom1988.cafej3.viewmodel.MenuViewModel
import com.redcom1988.cafej3.theme.*

object HomeScreen : Tab {

    private fun readResolve(): Any = HomeScreen

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.ShoppingCart)
            return remember { TabOptions(index = 0u, title = "MENU", icon = icon) }
        }
    @Composable
    override fun Content() {
        val viewModel = viewModel<MenuViewModel>()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
                item {
                    HeroSection()
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    CategoryTabs(
                        categories = viewModel.categories,
                        selectedCategory = viewModel.selectedCategory.value,
                        onCategorySelected = { viewModel.selectCategory(it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(viewModel.filteredItems, key = { it.id }) { menuItem ->
                    FoodCard(
                        item = menuItem,
                        onDelete = { viewModel.deleteItem(menuItem.id) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                            Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }