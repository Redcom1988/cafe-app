package com.redcom1988.cafej3.screens.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.redcom1988.cafej3.components.*
import com.redcom1988.cafej3.theme.*
import com.redcom1988.cafej3.screens.*
import com.redcom1988.cafej3.R
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState

// ================= DATA =================

data class MenuItem(
    val title: String,
    val category: String,
    val price: String,
    val image: Int
)

data class OrderItem(
    val title: String,
    val qty: Int,
    val price: String,
    val image: Int
)

object OrderScreen : Screen {

    private fun readResolve(): Any = OrderScreen

    // ================= SCREEN =================
    @Composable
    override fun Content() {
        val selectedItem = remember { mutableStateOf("MY ORDER") }

        val menuList = listOf(
            MenuItem("Salmon Salad", "GLUTEN FREE", "Rp.45K", R.drawable.food1),
            MenuItem("Pancake", "SIGNATURE", "Rp.35K", R.drawable.food2),
            MenuItem("Pizza", "VEGETARIAN", "Rp.55K", R.drawable.food3),
            MenuItem("Cake", "DESSERT", "Rp.30K", R.drawable.food4),
            MenuItem("Coffee", "COCKTAIL", "Rp.25K", R.drawable.food5),
            MenuItem("Orange Juice", "NON-ALCOHOLIC", "Rp.20K", R.drawable.food6),
        )

        val orderList = remember {
            mutableStateListOf(
                OrderItem("Pizza", 1, "Rp.55K", R.drawable.food3),
                OrderItem("Coffee", 2, "Rp.25K", R.drawable.food5)
            )
        }

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
                    .background(Background)
                    .padding(horizontal = 20.dp)
            ) {

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    HeaderSection()
                    Spacer(modifier = Modifier.height(20.dp))
                    SearchSection()
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.height(820.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        items(menuList.size) { index ->
                            MenuCard(menuList[index])
                        }
                        item { AddMenuCard() }
                        item { MoreMenuCard() }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    OrderCard(orderList)
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }
    }

    // ================= HEADER =================
    @Composable
    fun HeaderSection() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "✕",
                    color = Orange3,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "XXXX",
                    color = DarkBrown,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }

    // ================= SEARCH =================
    @Composable
    fun SearchSection() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Cream)
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = SoftBrown
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Search menu items...",
                    color = SoftBrown,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CategoryChip("All", true)
                CategoryChip("Main", false)
                CategoryChip("Drink", false)
                CategoryChip("Dessert", false)
            }
        }
    }

    // ================= CATEGORY =================
    @Composable
    fun CategoryChip(title: String, selected: Boolean) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(if (selected) PrimaryOrange else Cream)
                .clickable { }
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = if (selected) White else SoftBrown,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    // ================= MENU CARD =================
    @Composable
    fun MenuCard(item: MenuItem) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(White)
                .padding(12.dp)
        ) {
            Box {
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = null,
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
                        text = item.price,
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

    // ================= ADD CARD =================
    @Composable
    fun AddMenuCard() {
        Box(
            modifier = Modifier
                .height(120.dp)
                .border(2.dp, PinkSoft2, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                tint = SoftBrown,
                modifier = Modifier.size(40.dp)
            )
        }
    }

    // ================= MORE CARD =================
    @Composable
    fun MoreMenuCard() {
        Box(
            modifier = Modifier
                .height(120.dp)
                .border(2.dp, PinkSoft2, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "•••", color = SoftBrown, fontSize = 26.sp)
        }
    }

    // ================= ORDER CARD =================
    @Composable
    fun OrderCard(orderList: List<OrderItem>) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "XXXX",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = DarkBrown
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Table #14 • Server: Marcus V.",
                            color = SoftBrown,
                            fontSize = 15.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(PinkSoft2)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Dine In",
                            color = Orange2,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Cream)
                Spacer(modifier = Modifier.height(20.dp))

                orderList.forEach {
                    OrderItemCard(it)
                    Spacer(modifier = Modifier.height(18.dp))
                }
                NotesSection()
                Spacer(modifier = Modifier.height(24.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background)
                    .padding(24.dp)
            ) {
                PriceRow("Subtotal", "Rp.xxx")
                Spacer(modifier = Modifier.height(14.dp))
                PriceRow("Tax (8.5%)", "Rp.xxx")
                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = Cream)
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBrown
                    )
                    Text(
                        text = "Rp.xxx",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Orange2
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Cream)
                            .padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Split",
                            color = DarkBrown,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1.2f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(PrimaryOrange)
                            .padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Order",
                            color = White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    }

    // ================= ORDER ITEM =================
    @Composable
    fun OrderItemCard(item: OrderItem) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = null,
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    color = DarkBrown,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    QtyButton(Icons.Outlined.Remove)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = item.qty.toString(), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(10.dp))
                    QtyButton(Icons.Outlined.Add)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = null,
                        tint = SoftBrown
                    )
                }
            }
            Text(
                text = item.price,
                color = Orange2,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }

    // ================= QTY BUTTON =================
    @Composable
    fun QtyButton(icon: androidx.compose.ui.graphics.vector.ImageVector) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Cream),
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

    // ================= NOTES =================
    @Composable
    fun NotesSection() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Background)
                .padding(16.dp)
        ) {
            Text(
                text = "ORDER NOTES",
                color = Orange2,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "HIDUP JOKOWI!!",
                color = SoftBrown,
                fontSize = 16.sp
            )
        }
    }

    // ================= PRICE ROW =================
    @Composable
    fun PriceRow(title: String, value: String) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = SoftBrown, fontSize = 18.sp)
            Text(text = value, color = SoftBrown, fontSize = 18.sp)
        }
    }
}