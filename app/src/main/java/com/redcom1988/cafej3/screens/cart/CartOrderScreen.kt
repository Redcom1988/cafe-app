package com.redcom1988.cafej3.screens.cart

import com.redcom1988.core.util.formatDateTime
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.redcom1988.cafej3.components.ErrorDisplay
import com.redcom1988.cafej3.components.PrimaryButton
import com.redcom1988.cafej3.screens.guest.LocalIsGuest
import com.redcom1988.cafej3.screens.main.AppTab
import com.redcom1988.cafej3.screens.main.LocalTabSwitcher
import com.redcom1988.domain.order.model.Order
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.delay

data object CartOrderScreen : Screen {

    @Suppress("unused")
    private fun readResolve(): Any = CartOrderScreen

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { CartScreenModel() }
        val state by screenModel.state.collectAsState()
        val items by screenModel.cartManager.items.collectAsState()
        val subtotal by screenModel.cartManager.total.collectAsState()
        val switchTab = LocalTabSwitcher.current
        val isGuest = LocalIsGuest.current
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(items) {
            screenModel.checkAndRemoveUnavailable()
        }

        LaunchedEffect(state.paymentUrl) {
            val url = state.paymentUrl
            if (url != null) {
                screenModel.clearPaymentUrl()
                navigator.push(PaymentWebViewScreen(url, state.pendingOrderId ?: 0))
            }
        }

        val lifecycle = LocalLifecycleOwner.current.lifecycle
        DisposableEffect(lifecycle) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    screenModel.loadPendingOrders()
                    screenModel.loadUserOffers()
                }
            }
            lifecycle.addObserver(observer)
            onDispose { lifecycle.removeObserver(observer) }
        }

        screenModel.tableNumber()
        val selectedOrder = state.pendingOrders.getOrNull(state.selectedOrderIndex)

        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        // Back button removed for guests to lock them into order tracking
                    },
                    title = {
                        Text("My Order", fontWeight = FontWeight.Bold)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                when {
                    state.orderSuccess && state.lastOrder != null -> {
                        OrderSuccessContent(
                            order = state.lastOrder!!,
                            onContinue = {
                                screenModel.dismissOrderSuccess()
                            }
                        )
                    }
                    else -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            TabRow(
                                selectedTabIndex = state.selectedCartTab,
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.primary
                            ) {
                                Tab(
                                    selected = state.selectedCartTab == 0,
                                    onClick = { screenModel.setCartTab(0) },
                                    text = {
                                        Text(
                                            "New Order",
                                            fontWeight = if (state.selectedCartTab == 0) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                )
                                Tab(
                                    selected = state.selectedCartTab == 1,
                                    onClick = { screenModel.setCartTab(1) },
                                    text = {
                                        Text(
                                            "Orders",
                                            fontWeight = if (state.selectedCartTab == 1) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                )
                            }
                            when (state.selectedCartTab) {
                                0 -> {
                                    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 16.dp)) {
                                        if (isGuest && state.pendingOrders.isNotEmpty()) {
                                            OngoingOrderRestrictedContent(onGoToOrders = { screenModel.setCartTab(1) })
                                        } else if (items.isNotEmpty()) {
                                            CartContent(
                                                items = items,
                                                total = subtotal,
                                                state = state,
                                                screenModel = screenModel,
                                                isGuest = isGuest
                                            )
                                        } else {
                                            EmptyCartContent(
                                                onBrowseMenu = { switchTab(AppTab.Menu) }
                                            )
                                        }
                                    }
                                }
                                1 -> {
                                    if (state.pendingOrders.isNotEmpty() && selectedOrder != null) {
                                        Column(modifier = Modifier.fillMaxSize()) {
                                            if (state.pendingOrders.size > 1) {
                                                OrderSelector(
                                                    orders = state.pendingOrders,
                                                    selectedIndex = state.selectedOrderIndex,
                                                    onSelect = { screenModel.selectOrder(it) }
                                                )
                                            }
                                            ActiveOrderContent(
                                                order = selectedOrder,
                                                isRefreshing = state.isRefreshing,
                                                onPay = { screenModel.payExistingOrder(selectedOrder) },
                                                onRefresh = { screenModel.refreshSelectedOrder() },
                                                onNewOrder = {
                                                    screenModel.clearActiveOrder()
                                                    switchTab(AppTab.Menu)
                                                },
                                                onCancel = { screenModel.cancelOrder(selectedOrder.id) }
                                            )
                                        }
                                    } else if (state.isLoading) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) { 
                                            CircularProgressIndicator(
                                                color = MaterialTheme.colorScheme.primary,
                                            )
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                "No active orders",
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun OrderSelector(
        orders: List<Order>,
        selectedIndex: Int,
        onSelect: (Int) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            orders.forEachIndexed { index, order ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { onSelect(index) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = order.orderCode,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }

    @Composable
    private fun CartContent(
        items: List<com.redcom1988.domain.cart.CartItem>,
        total: String,
        state: CartUiState,
        screenModel: CartScreenModel,
        isGuest: Boolean
    ) {
        val cartNavigator = LocalNavigator.currentOrThrow
        Column(modifier = Modifier.fillMaxSize()) {
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

            PriceRow("Subtotal", "Rp$total")

            val discount = state.selectedUserOfferId?.let { id ->
                state.userOffers.find { it.id == id }?.offer
            }?.let { offer ->
                val totalNum = total.replace(",", "").toIntOrNull() ?: 0
                val rawDiscount = if (offer.discountType == "PERCENTAGE") {
                    val pct = totalNum * offer.discountRate / 100
                    offer.maxDiscount?.let { minOf(pct, it) } ?: pct
                } else {
                    offer.discountRate
                }
                minOf(rawDiscount, totalNum)
            } ?: 0

            if (discount > 0) {
                PriceRow("Discount", "-Rp$discount")
                Spacer(modifier = Modifier.height(4.dp))
                PriceRow("Total", "Rp${total.replace(",", "").toIntOrNull()?.minus(discount)?.let { NumberFormat.getNumberInstance(Locale.US).format(it) } ?: total}", bold = true)
            } else {
                PriceRow("Total", "Rp$total", bold = true)
            }

            if (!isGuest) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Offer",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (state.selectedUserOfferId != null) "Change" else "Select",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable {
                            cartNavigator.push(OfferSelectionScreen)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                val selectedOffer = state.selectedUserOfferId?.let { id ->
                    state.userOffers.find { it.id == id }
                }
                if (selectedOffer != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedOffer.offer?.name ?: "Offer #${selectedOffer.offerId}",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        IconButton(
                            onClick = { screenModel.selectOffer(null) },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove offer",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { cartNavigator.push(OfferSelectionScreen) },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = "Select an offer",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            if (state.error != null) {
                ErrorDisplay(
                    message = state.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            PrimaryButton(
                text = if (state.isLoading) "Processing..." else "Pay",
                onClick = { screenModel.initiatePayment() },
                enabled = !state.isLoading,
                modifier = Modifier.padding(top = 16.dp)
            )

            if (state.removedItems.isNotEmpty()) {
                Text(
                    text = "Removed from cart (no longer available): ${state.removedItems.joinToString(", ")}",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

    @Composable
    private fun OngoingOrderRestrictedContent(onGoToOrders: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ongoing Order",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You already have an active order. Please finish it or check its status in the Orders tab.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                PrimaryButton(
                    text = "View Orders",
                    onClick = onGoToOrders
                )
            }
        }
    }

    @Composable
    private fun EmptyCartContent(onBrowseMenu: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                Text(
                    text = "Your cart is empty",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Try adding some items to your cart through the menu",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                PrimaryButton(
                    text = "Browse Menu",
                    onClick = onBrowseMenu
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ActiveOrderContent(
        order: Order,
        isRefreshing: Boolean,
        onRefresh: () -> Unit,
        onNewOrder: (() -> Unit)? = null,
        onCancel: () -> Unit = {},
        onPay: () -> Unit = {},
        showOrderCode: Boolean = true
    ) {
        val isTerminal = order.status == "DONE" || order.status == "CANCELLED"
        val canCancel = order.status == "PENDING" && order.payment?.status == "UNPAID"
        val canPay = order.payment?.status == "UNPAID" && order.payment?.paymentUrl != null

        LaunchedEffect(order.id, isTerminal) {
            if (isTerminal) return@LaunchedEffect
            while (true) {
                delay(30_000L)
                onRefresh()
            }
        }

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    if (showOrderCode) {
                        Text(
                            text = "Order ${order.orderCode}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    val statusColor = when (order.status) {
                        "CANCELLED" -> MaterialTheme.colorScheme.error
                        "DONE" -> Color(0xFF2E7D32)
                        else -> MaterialTheme.colorScheme.primary
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = order.status,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = statusColor
                        )
                        if (order.payment != null) {
                            val payLabel = when (order.payment?.status) {
                                "PAID" -> "Paid"
                                "UNPAID" -> "Unpaid"
                                "EXPIRED" -> "Expired"
                                else -> order.payment?.status ?: "UNKNOWN"
                            }
                            val payColor = when (order.payment?.status) {
                                "PAID" -> Color(0xFF2E7D32)
                                "UNPAID" -> Color(0xFFC62828)
                                "EXPIRED" -> Color(0xFF9E9E9E)
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                            StatusChip(payLabel, payColor)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    val cancelReason = order.cancellationReason
                    if (order.status == "CANCELLED" && cancelReason != null) {
                        Text(
                            text = cancelReason,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    val createdAt = formatDateTime(order.createdAt)
                    if (createdAt != null) {
                        Text(
                            text = createdAt,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (order.items.isNotEmpty()) {
                    item {
                        Text(
                            text = "Items",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(order.items) { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "x${item.quantity}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.width(32.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.menuItemName ?: "Item #${item.menuItemId}",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                                if (item.unitStatuses.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        val done = item.unitStatuses.count { it == "DONE" }
                                        val preparing = item.unitStatuses.count { it == "PREPARING" }
                                        val pending = item.unitStatuses.count { it == "PENDING" }
                                        val cancelled = item.unitStatuses.count { it == "CANCELLED" }
                                        if (done > 0) StatusChip("$done Done", Color(0xFF2E7D32))
                                        if (preparing > 0) StatusChip("$preparing Prep", MaterialTheme.colorScheme.primary)
                                        if (pending > 0) StatusChip("$pending Pending", MaterialTheme.colorScheme.onSurfaceVariant)
                                        if (cancelled > 0) StatusChip("$cancelled Cancelled", MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                            Text(
                                text = "Rp${item.subtotal}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    PriceRow("Subtotal", "Rp${order.totalPrice}")
                    if (order.totalPrice != order.finalPrice) {
                        val discount = (order.totalPrice.toIntOrNull() ?: 0) - (order.finalPrice.toIntOrNull() ?: 0)
                        if (discount > 0) PriceRow("Discount", "-Rp$discount")
                    }
                    PriceRow("Total", "Rp${order.finalPrice}", bold = true)
                    Spacer(modifier = Modifier.height(24.dp))

                    if (isTerminal && onNewOrder != null) {
                        PrimaryButton(
                            text = "Start New Order",
                            onClick = onNewOrder
                        )
                    } else if (!isTerminal) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (canPay) {
                                PrimaryButton(
                                    text = "Pay Now",
                                    onClick = onPay,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (canCancel) {
                                PrimaryButton(
                                    text = "Cancel Order",
                                    onClick = onCancel,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun OrderSuccessContent(order: Order, onContinue: () -> Unit) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Order Placed!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Order Code: ${order.orderCode}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Status: ${order.status}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Total: Rp${order.finalPrice}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                val isGuest = LocalIsGuest.current
                PrimaryButton(
                    text = if (isGuest) "Track Order" else "Back to Menu",
                    onClick = onContinue
                )
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

@Composable
fun StatusChip(text: String, color: Color) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = color,
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}
