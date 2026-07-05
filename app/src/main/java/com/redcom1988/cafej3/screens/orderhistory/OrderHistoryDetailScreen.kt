package com.redcom1988.cafej3.screens.orderhistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.redcom1988.cafej3.components.ErrorDisplay
import com.redcom1988.cafej3.screens.cart.CartOrderScreen


data class OrderHistoryDetailScreen(val orderId: Int) : Screen {

    @Suppress("unused")
    private fun readResolve(): Any = OrderHistoryDetailScreen(orderId)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { OrderHistoryDetailScreenModel() }
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.loadOrder(orderId)
        }

        val orderCode = state.order?.orderCode ?: "Order Detail"

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(orderCode, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    state.error != null -> {
                        ErrorDisplay(
                            message = state.error,
                            onRetry = { screenModel.loadOrder(orderId) },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    state.order != null -> {
                        CartOrderScreen.ActiveOrderContent(
                            order = state.order!!,
                            isRefreshing = false,
                            onRefresh = { screenModel.loadOrder(orderId) },
                            onNewOrder = null,
                            showOrderCode = false
                        )
                    }
                }
            }
        }
    }
}
