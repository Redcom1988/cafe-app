package com.redcom1988.cafej3.screens.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.redcom1988.cafej3.components.ErrorDisplay
import com.redcom1988.cafej3.components.PrimaryButton
import com.redcom1988.cafej3.screens.home.components.CategoryTabs
import com.redcom1988.cafej3.screens.home.components.HeroSection
import com.redcom1988.cafej3.screens.home.components.MenuItemCard
import com.redcom1988.cafej3.screens.guest.LocalIsGuest
import com.redcom1988.cafej3.screens.login.LoginScreen
import com.redcom1988.cafej3.screens.scan.ScanQrScreen
import com.redcom1988.core.util.inject
import com.redcom1988.domain.cart.CartManager

data object MenuScreen : Screen {

    @Suppress("unused")
    private fun readResolve(): Any = MenuScreen

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { MenuScreenModel() }
        val state by screenModel.state.collectAsState()
        val cartManager = inject<CartManager>()
        val cartItems by cartManager.items.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val isGuest = LocalIsGuest.current
        val hasActiveGuestOrder = screenModel.hasTrackingToken()
        val canOrder = !isGuest || !hasActiveGuestOrder
        val tableLabel = screenModel.tableNumber()
        fun cartQuantity(itemId: Int): Int = cartItems.find { it.menuItem.id == itemId }?.quantity ?: 0

        LaunchedEffect(Unit) {
            screenModel.checkTable()
        }

        if (!state.hasTable) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Menu", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            // Back button removed for guests to lock them into order tracking
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                    )
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { padding ->
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Text(
                            text = "No Table Selected",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Please scan a QR code or enter your table number to start ordering.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        PrimaryButton(
                            text = "Select Table",
                            onClick = { navigator.push(ScanQrScreen) }
                        )
                    }
                }
            }
            return
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        // Back button removed for guests to lock them into order tracking
                    },
                    title = {
                        Column {
                            Text("Menu", fontWeight = FontWeight.Bold)
                            if (tableLabel.isNotBlank()) {
                                Text(
                                    text = "Table $tableLabel",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            if (isGuest) {
                                navigator.replaceAll(LoginScreen)
                            } else {
                                navigator.push(ScanQrScreen)
                            }
                        }) {
                            Icon(
                                imageVector = if (isGuest) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Edit,
                                contentDescription = if (isGuest) "Back to Login" else "Change Table"
                            )
                        }
                        IconButton(onClick = { screenModel.toggleSearch() }) {
                            Icon(
                                if (state.isSearchActive) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            if (state.isLoading && state.categories.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                    if (state.isSearchActive) {
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = screenModel::onSearchQueryChange,
                            placeholder = { Text("Search menu...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { screenModel.toggleSearch() }) {
                                    Icon(Icons.Default.Close, contentDescription = "Close search")
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                    val listState = rememberLazyListState()

                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { screenModel.refresh() },
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    ) {
                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            item {
                                HeroSection()
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            if (state.categories.isNotEmpty()) {
                                item {
                                    CategoryTabs(
                                        categories = state.categories,
                                        selectedId = state.selectedCategoryId ?: -1,
                                        onCategorySelected = { category ->
                                            screenModel.selectCategory(category.id)
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            if (state.isLoading && state.categories.isNotEmpty()) {
                                item {
                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            }

                            items(state.items, key = { it.id }) { item ->
                                MenuItemCard(
                                    item = item,
                                    quantity = cartQuantity(item.id),
                                    onAddToCart = { if (canOrder) cartManager.addItem(item) },
                                    onIncrement = { if (canOrder) cartManager.addItem(item) },
                                    onDecrement = { cartManager.updateQuantity(item.id, -1) },
                                    canOrder = canOrder
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            if (state.error != null && state.items.isEmpty()) {
                                item {
                                    ErrorDisplay(
                                        message = state.error,
                                        onRetry = { screenModel.load() },
                                        modifier = Modifier.padding(top = 32.dp)
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
