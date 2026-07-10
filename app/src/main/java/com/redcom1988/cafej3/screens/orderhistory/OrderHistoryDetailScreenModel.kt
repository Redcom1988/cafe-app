package com.redcom1988.cafej3.screens.orderhistory

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.util.inject
import com.redcom1988.domain.order.interactor.GetOrderDetail
import com.redcom1988.domain.order.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OrderHistoryDetailUiState(
    val order: Order? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class OrderHistoryDetailScreenModel(
    private val getOrderDetail: GetOrderDetail = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(OrderHistoryDetailUiState())
    val state: StateFlow<OrderHistoryDetailUiState> = _state.asStateFlow()

    fun loadOrder(orderId: Int) {
        screenModelScope.launch {
            _state.value = OrderHistoryDetailUiState(isLoading = true)
            try {
                val order = getOrderDetail.await(orderId)
                _state.value = OrderHistoryDetailUiState(order = order)
            } catch (e: Exception) {
                _state.value = OrderHistoryDetailUiState(error = e.message ?: "Failed to load order")
            }
        }
    }
}