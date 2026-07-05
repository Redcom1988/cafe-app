package com.redcom1988.domain.cart

import com.redcom1988.core.preference.PreferenceStore
import com.redcom1988.domain.menu.model.MenuCategory
import com.redcom1988.domain.menu.model.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int = 1
)

class CartManager(
    private val preferenceStore: PreferenceStore? = null
) {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    val subtotal: StateFlow<String> = _items.map { list ->
        list.sumOf {
            (it.menuItem.price.toLongOrNull() ?: 0L) * it.quantity
        }.toString()
    }.stateIn(scope, SharingStarted.WhileSubscribed(5000), "0")

    val total: StateFlow<String> = subtotal

    init {
        loadPersisted()
    }

    fun addItem(menuItem: MenuItem) {
        val current = _items.value.toMutableList()
        val existing = current.indexOfFirst { it.menuItem.id == menuItem.id }
        if (existing >= 0) {
            current[existing] = current[existing].copy(quantity = current[existing].quantity + 1)
        } else {
            current.add(CartItem(menuItem))
        }
        _items.value = current
        persist()
    }

    fun updateQuantity(menuItemId: Int, delta: Int) {
        val current = _items.value.toMutableList()
        val idx = current.indexOfFirst { it.menuItem.id == menuItemId }
        if (idx < 0) return
        val newQty = current[idx].quantity + delta
        if (newQty <= 0) {
            current.removeAt(idx)
        } else {
            current[idx] = current[idx].copy(quantity = newQty)
        }
        _items.value = current
        persist()
    }

    fun removeItem(menuItemId: Int) {
        _items.value = _items.value.filter { it.menuItem.id != menuItemId }
        persist()
    }

    fun clear() {
        _items.value = emptyList()
        persist()
    }

    private fun persist() {
        if (preferenceStore == null) return
        val data = _items.value.joinToString("|") { item ->
            val mi = item.menuItem
            "${mi.id}^${mi.name}^${mi.price}^${mi.imageUrl ?: ""}^${mi.categoryId}^${mi.category.name}^${item.quantity}"
        }
        preferenceStore.getString("cart_items").set(data)
    }

    private fun loadPersisted() {
        if (preferenceStore == null) return
        val data = preferenceStore.getString("cart_items").get()
        if (data.isBlank()) return
        try {
            val items = data.split("|").mapNotNull { segment ->
                val parts = segment.split("^")
                if (parts.size < 7) return@mapNotNull null
                val id = parts[0].toIntOrNull() ?: return@mapNotNull null
                val name = parts[1]
                val price = parts[2]
                val imageUrl = parts[3].ifBlank { null }
                val catId = parts[4].toIntOrNull() ?: return@mapNotNull null
                val catName = parts[5]
                val qty = parts[6].toIntOrNull() ?: return@mapNotNull null
                CartItem(
                    menuItem = MenuItem(
                        id = id,
                        categoryId = catId,
                        name = name,
                        imageUrl = imageUrl,
                        price = price,
                        category = MenuCategory(id = catId, name = catName)
                    ),
                    quantity = qty
                )
            }
            _items.value = items
        } catch (_: Exception) { }
    }
}
