package com.redcom1988.cafej3.screens.orderhistory

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.order.interactor.GetMyOrders
import com.redcom1988.domain.order.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OrderHistoryUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

class OrderHistoryScreenModel(
    private val getMyOrders: GetMyOrders = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(OrderHistoryUiState())
    val state: StateFlow<OrderHistoryUiState> = _state.asStateFlow()

    init {
        load()
    }

    private fun List<Order>.completedOnly(): List<Order> =
        filter { it.status == "DONE" || it.status == "CANCELLED" }

    fun refresh() {
        screenModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true, error = null)
            try {
                val orders = getMyOrders.await().completedOnly()
                _state.value = _state.value.copy(orders = orders, isRefreshing = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isRefreshing = false,
                    error = e.message ?: "Failed to load orders"
                )
            }
        }
    }

    fun load() {
        screenModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val orders = getMyOrders.await().completedOnly()
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
