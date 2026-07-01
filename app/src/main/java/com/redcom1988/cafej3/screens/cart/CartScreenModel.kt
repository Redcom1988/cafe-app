package com.redcom1988.cafej3.screens.cart

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.cart.CartManager
import com.redcom1988.domain.menu.interactor.GetMenuItems
import com.redcom1988.domain.menu.model.MenuItem
import com.redcom1988.domain.order.interactor.CreateOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartUiState(
    val isLoading: Boolean = false,
    val orderSuccess: Boolean = false,
    val removedItems: List<String> = emptyList(),
    val error: String? = null
)

class CartScreenModel(
    private val createOrder: CreateOrder = inject(),
    private val getMenuItems: GetMenuItems = inject(),
    val cartManager: CartManager = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(CartUiState())
    val state: StateFlow<CartUiState> = _state.asStateFlow()

    fun checkAndRemoveUnavailable() {
        val currentItems = cartManager.items.value
        if (currentItems.isEmpty()) return
        screenModelScope.launch {
            try {
                val allItems = getMenuItems.await()
                val availableIds = allItems.filter { it.isAvailable }.map { it.id }.toSet()
                val toRemove = currentItems.filter { it.menuItem.id !in availableIds }
                if (toRemove.isNotEmpty()) {
                    val names = toRemove.map { it.menuItem.name }
                    toRemove.forEach { cartManager.removeItem(it.menuItem.id) }
                    _state.value = _state.value.copy(removedItems = names)
                }
            } catch (_: Exception) { }
        }
    }

    fun dismissRemovedMessage() {
        _state.value = _state.value.copy(removedItems = emptyList())
    }

    fun placeOrder(tableId: Int? = null, userId: Int? = null) {
        screenModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                createOrder.await(
                    tableId = tableId,
                    userId = userId,
                    items = cartManager.items.value.map { it.menuItem.id to it.quantity }
                )
                cartManager.clear()
                _state.value = CartUiState(orderSuccess = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
