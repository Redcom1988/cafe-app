package com.redcom1988.cafej3.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.redcom1988.cafej3.components.AppTab
import com.redcom1988.cafej3.components.PrimaryButton
import com.redcom1988.cafej3.screens.main.LocalTabSwitcher

data object CartOrderScreen : Screen {

    @Suppress("unused")
    private fun readResolve(): Any = CartOrderScreen

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { CartScreenModel() }
        val state by screenModel.state.collectAsState()
        val items by screenModel.cartManager.items.collectAsState()
        val subtotal by screenModel.cartManager.subtotal.collectAsState()
        val tax by screenModel.cartManager.tax.collectAsState()
        val total by screenModel.cartManager.total.collectAsState()
        val switchTab = LocalTabSwitcher.current

        LaunchedEffect(items) {
            screenModel.checkAndRemoveUnavailable()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("My Order", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (items.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Your cart is empty",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Start by adding items from the menu",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    OutlinedButton(
                                        onClick = { switchTab(AppTab.Menu) },
                                        modifier = Modifier.wrapContentWidth()
                                    ) {
                                        Text("Browse Menu")
                                    }
                                }
                            }
                        } else {
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(items) { item ->
                                    CartItemRow(
                                        name = item.menuItem.name,
                                        price = item.menuItem.price,
                                        quantity = item.quantity,
                                        onIncrement = {
                                            screenModel.cartManager.updateQuantity(item.menuItem.id, 1)
                                        },
                                        onDecrement = {
                                            screenModel.cartManager.updateQuantity(item.menuItem.id, -1)
                                        },
                                        onRemove = { screenModel.cartManager.removeItem(item.menuItem.id) }
                                    )
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            PriceRow("Subtotal", "Rp$subtotal")
                            PriceRow("Tax (8.5%)", "Rp$tax")
                            PriceRow("Total", "Rp$total", bold = true)

                            if (state.error != null) {
                                Text(
                                    text = state.error ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            PrimaryButton(
                                text = if (state.isLoading) "Placing Order..." else "Order",
                                onClick = { screenModel.placeOrder() },
                                enabled = !state.isLoading,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }

                        if (state.removedItems.isNotEmpty()) {
                            Text(
                                text = "Removed from cart (no longer available): ${state.removedItems.joinToString(", ")}",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        if (state.orderSuccess) {
                            Text(
                                text = "Order placed successfully!",
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CartItemRow(
        name: String,
        price: String,
        quantity: Int,
        onIncrement: () -> Unit,
        onDecrement: () -> Unit,
        onRemove: () -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = name, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "Rp$price",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecrement, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Filled.Remove, contentDescription = "Remove")
                    }
                    Text(
                        text = "$quantity",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(onClick = onIncrement, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                    IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun PriceRow(label: String, value: String, bold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        Text(text = value, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
    }
}
