package com.redcom1988.cafej3.screens.order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.model.OrderItem
import com.redcom1988.cafej3.model.toRupiahFormat
import com.redcom1988.cafej3.theme.*

@Composable
fun OrderSummarySection(
    orderList: List<OrderItem>,
    subtotal: Double,
    tax: Int,
    total: Double,
    onIncreaseQty: (OrderItem) -> Unit,
    onDecreaseQty: (OrderItem) -> Unit,
    onRemove: (OrderItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(White)
            .padding(20.dp)
    ) {

        Text(
            text = "Your Order",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = DarkBrown
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (orderList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No items yet. Tap a menu to add!",
                    color = SoftBrown,
                    fontSize = 15.sp
                )
            }
        } else {
            // List order items
            orderList.forEach { item ->
                OrderItemCard(
                    item = item,
                    onIncreaseQty = onIncreaseQty,
                    onDecreaseQty = onDecreaseQty,
                    onRemove = onRemove
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            HorizontalDivider(color = Background, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Subtotal
            SummaryRow(label = "Subtotal", value = subtotal.toRupiahFormat())
            Spacer(modifier = Modifier.height(8.dp))

            // Tax
            SummaryRow(label = "Tax (8.5%)", value = tax.toDouble().toRupiahFormat())
            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(color = Background, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Total", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkBrown)
                Text(text = total.toRupiahFormat(), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PrimaryOrange)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "Place Order",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
    }
}


@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = SoftBrown, fontSize = 15.sp)
        Text(text = value, color = DarkBrown, fontSize = 15.sp)
    }
}