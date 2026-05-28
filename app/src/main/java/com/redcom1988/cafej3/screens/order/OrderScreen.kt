package com.redcom1988.cafej3.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.redcom1988.cafej3.components.*
import com.redcom1988.cafej3.screens.order.components.*
import com.redcom1988.cafej3.model.*
import com.redcom1988.cafej3.theme.*
import com.redcom1988.cafej3.data.DummyData

object OrderScreen : Tab {

    private fun readResolve(): Any = OrderScreen

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.ShoppingBag)
            return remember { TabOptions(index = 1u, title = "MY ORDER", icon = icon) }
        }

    @Composable
    override fun Content() {
        val menuList = remember { DummyData.menuItems }
        val orderList = remember { mutableStateListOf<OrderItem>() }

        val subtotal = orderList.sumOf { it.menuItem.price * it.qty }
        val tax = (subtotal * 0.085).toInt()
        val total = subtotal + tax

        val addToCart = { menuItem: MenuItem ->
            val existingItem = orderList.find { it.menuItem.id == menuItem.id }
            if (existingItem != null) {
                val index = orderList.indexOf(existingItem)
                orderList[index] = existingItem.copy(qty = existingItem.qty + 1)
            } else {
                orderList.add(OrderItem(menuItem = menuItem, qty = 1))
            }
        }

        val increaseQty = { item: OrderItem ->
            val index = orderList.indexOf(item)
            orderList[index] = item.copy(qty = item.qty + 1)
        }

        val decreaseQty: (OrderItem) -> Unit = { item ->
            val index = orderList.indexOf(item)
            if (index != -1) {
                if (item.qty > 1) orderList[index] = item.copy(qty = item.qty - 1)
                else orderList.remove(item)
            }
        }

        val removeItem: (OrderItem) -> Unit = { item ->
            orderList.remove(item)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
                item {
                    Header()
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(modifier = Modifier.height(20.dp))
                        SearchSection()
                        Spacer(modifier = Modifier.height(24.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.height(((menuList.size + 1) / 2 * 180).dp),
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            items(menuList) { menu ->
                                MenuCard(
                                    item = menu,
                                    onAddToCart = { selectedMenu -> addToCart(selectedMenu) }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        OrderSummarySection(
                            orderList = orderList,
                            subtotal = subtotal,
                            tax = tax,
                            total = total,
                            onIncreaseQty = increaseQty,
                            onDecreaseQty = decreaseQty,
                            onRemove = removeItem
                        )
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }
    }
}