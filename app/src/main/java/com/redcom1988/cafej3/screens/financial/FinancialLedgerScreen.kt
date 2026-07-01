package com.redcom1988.cafej3.screens.financial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.material3.MaterialTheme

data object FinancialLedgerScreen : Screen {

    @Suppress("unused")
    private fun readResolve(): Any = FinancialLedgerScreen

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
    val screenModel = rememberScreenModel { FinancialLedgerScreenModel() }
    val state by screenModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financial Ledger", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        if (state.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Keuangan · Tracking daily artisan operations",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            state.salesSummary?.let { sales ->
                StatCard(
                    icon = Icons.Filled.AccountBalance,
                    label = "Total Revenue",
                    value = "Rp${sales.totalRevenue}",
                    caption = "${sales.totalOrders} orders"
                )

                Spacer(modifier = Modifier.height(8.dp))

                StatCard(
                    icon = Icons.Filled.Wallet,
                    label = "Gross Revenue",
                    value = "Rp${sales.grossRevenue}",
                    caption = "${sales.totalItemsSold} items sold"
                )

                Spacer(modifier = Modifier.height(8.dp))

                StatCard(
                    icon = Icons.Filled.ShoppingCart,
                    label = "Active Expenses",
                    value = "-Rp${sales.totalDiscount}",
                    caption = "Total discount given"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Top Items", fontWeight = FontWeight.Bold)

            state.topItems.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = item.name, modifier = Modifier.weight(1f))
                    Text(
                        text = "${item.totalSold}x sold",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        }
    }
    }
    }
}

@Composable
fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    caption: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = caption, color = Color(0xFF2E7D32), fontSize = 12.sp)
            }
        }
    }
}
