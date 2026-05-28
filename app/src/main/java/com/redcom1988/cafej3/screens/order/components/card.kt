package com.redcom1988.cafej3.screens.order.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.redcom1988.cafej3.theme.*
import com.redcom1988.cafej3.model.MenuItem
import com.redcom1988.cafej3.model.OrderItem
import com.redcom1988.cafej3.model.toRupiahFormat

@Composable
fun MenuCard(
    item: MenuItem,
    onAddToCart: (MenuItem) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(White)
            .clickable { onAddToCart(item) }
            .padding(12.dp)
    ) {
        Box {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(White)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = item.price.toRupiahFormat(),
                    color = Orange2,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = item.title,
            color = DarkBrown,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.category,
            color = SoftBrown,
            fontSize = 13.sp
        )
    }
}

@Composable
fun OrderItemCard(
    item: OrderItem,
    onIncreaseQty: (OrderItem) -> Unit,
    onDecreaseQty: (OrderItem) -> Unit,
    onRemove: (OrderItem) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.menuItem.image),
            contentDescription = null,
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.menuItem.title,
                fontWeight = FontWeight.Bold,
                color = DarkBrown,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                QtyButton(
                    icon = Icons.Outlined.Remove,
                    onClick = { onDecreaseQty(item) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = item.qty.toString(), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                QtyButton(
                    icon = Icons.Outlined.Add,
                    onClick = { onIncreaseQty(item) }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = "Hapus",
                    tint = SoftBrown,
                    modifier = Modifier.clickable { onRemove(item) }
                )
            }
        }
        Text(
            text = (item.menuItem.price * item.qty).toRupiahFormat(),
            color = Orange2,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun QtyButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Cream)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = DarkBrown,
            modifier = Modifier.size(16.dp)
        )
    }
}
