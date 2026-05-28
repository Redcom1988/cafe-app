package com.redcom1988.cafej3.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.redcom1988.cafej3.theme.*

data class OrderHistory(
    val id: String,
    val items: String,
    val total: String,
    val date: String,
    val status: String
)

object OrderHistoryScreen : Screen {

    private val dummyOrders = listOf(
        OrderHistory("ORD-001", "Pizza Truffle x1, Tiramisu x2", "Rp 165.000", "12 May 2025", "Completed"),
        OrderHistory("ORD-002", "Pasta Carbonara x2", "Rp 150.000", "20 Apr 2025", "Completed"),
        OrderHistory("ORD-003", "Bruschetta x1, Calamari x1", "Rp 100.000", "1 Apr 2025", "Cancelled"),
        OrderHistory("ORD-004", "Risotto Funghi x1", "Rp 80.000", "15 Mar 2025", "Completed"),
    )

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = DarkBrown
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Order History",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkBrown
                )
            }

            LazyColumn(
                modifier = Modifier.padding(horizontal = 22.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                items(dummyOrders) { order ->
                    OrderHistoryCard(order = order)
                }
            }
        }
    }
}

@Composable
private fun OrderHistoryCard(order: OrderHistory) {
    val statusColor = if (order.status == "Completed") PrimaryOrange else SoftBrown

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(White)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = order.id, fontWeight = FontWeight.Bold, color = DarkBrown, fontSize = 13.sp)
            Text(text = order.status, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = order.items, color = SoftBrown, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Background)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = order.date, color = SoftBrown, fontSize = 11.sp)
            Text(text = order.total, fontWeight = FontWeight.SemiBold, color = DarkBrown, fontSize = 13.sp)
        }
    }
}