package com.redcom1988.cafej3.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import coil3.compose.AsyncImage
import androidx.compose.material3.MaterialTheme
import com.redcom1988.cafej3.components.PrimaryButton
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

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cafe Jasjisjus", fontWeight = FontWeight.Bold) },
                    actions = {
                        IconButton(onClick = { screenModel.toggleSearch() }) {
                            Icon(
                                if (state.isSearchActive) Icons.Filled.Close else Icons.Filled.Search,
                                contentDescription = if (state.isSearchActive) "Close search" else "Search",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                if (state.isSearchActive) {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { screenModel.onSearchQueryChange(it) },
                        placeholder = { Text("Search menu items...") },
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        item {
                            LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                                item {
                                    FilterChip(
                                        selected = state.selectedCategoryId == null,
                                        onClick = { screenModel.selectCategory(null) },
                                        label = { Text("All") },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                }
                                items(state.categories) { category ->
                                    FilterChip(
                                        selected = state.selectedCategoryId == category.id,
                                        onClick = { screenModel.selectCategory(category.id) },
                                        label = { Text(category.name) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                }
                            }
                        }

                items(state.items) { item ->
                    MenuItemCard(
                        name = item.name,
                        price = item.price,
                        imageUrl = item.imageUrl,
                        category = item.category.name,
                        isAvailable = item.isAvailable,
                        onAddToCart = { cartManager.addItem(item) },
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MenuItemCard(
    modifier: Modifier = Modifier,
    name: String,
    price: String,
    imageUrl: String?,
    category: String,
    isAvailable: Boolean = true,
    onAddToCart: () -> Unit = {},
) {
    val alpha = if (isAvailable) 1f else 0.5f
    Card(
        modifier = modifier.fillMaxWidth().then(Modifier.alpha(alpha)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(MaterialTheme.colorScheme.surfaceVariant)) {
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            if (isAvailable) MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isAvailable) "Rp$price" else "Sold Out",
                        color = if (isAvailable) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = category,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 12.sp
                )
            }
            Box(modifier = Modifier.padding(12.dp)) {
                PrimaryButton(
                    text = "Add to Cart",
                    onClick = onAddToCart,
                    enabled = isAvailable
                )
            }
        }
    }
}