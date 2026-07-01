package com.redcom1988.cafej3.screens.orderhistory

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.auth.interactor.GetCurrentUser
import com.redcom1988.domain.order.interactor.GetMyOrders
import com.redcom1988.domain.order.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OrderHistoryUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class OrderHistoryScreenModel(
    private val getCurrentUser: GetCurrentUser = inject(),
    private val getMyOrders: GetMyOrders = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(OrderHistoryUiState())
    val state: StateFlow<OrderHistoryUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        screenModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val user = getCurrentUser.await()
                val orders = getMyOrders.await(user.id)
                _state.value = _state.value.copy(orders = orders, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load orders"
                )
            }
        }
    }
}
