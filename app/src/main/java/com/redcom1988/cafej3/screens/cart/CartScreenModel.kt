package com.redcom1988.cafej3.screens.cart

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.redcom1988.core.network.NetworkPreference
import com.redcom1988.core.util.inject
import com.redcom1988.domain.cart.CartManager
import com.redcom1988.domain.menu.interactor.GetMenuItems
import com.redcom1988.domain.offer.interactor.GetUserOffers
import com.redcom1988.domain.offer.model.UserOffer
import com.redcom1988.domain.order.interactor.CancelOrder
import com.redcom1988.domain.order.interactor.ConfirmPayment
import com.redcom1988.domain.order.interactor.CreateAuthenticatedOrder
import com.redcom1988.domain.order.interactor.CreateOrder
import com.redcom1988.domain.order.interactor.GetMyOrders
import com.redcom1988.domain.order.interactor.GetOrderDetail
import com.redcom1988.domain.order.model.Order
import com.redcom1988.domain.table.interactor.TableSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val orderSuccess: Boolean = false,
    val lastOrder: Order? = null,
    val paymentUrl: String? = null,
    val pendingOrderId: Int? = null,
    val pendingOrders: List<Order> = emptyList(),
    val selectedOrderIndex: Int = 0,
    val selectedCartTab: Int = 0,
    val userOffers: List<UserOffer> = emptyList(),
    val selectedUserOfferId: Int? = null,
    val removedItems: List<String> = emptyList(),
    val error: String? = null
)

class CartScreenModel(
    private val cancelOrder: CancelOrder = inject(),
    private val createOrder: CreateOrder = inject(),
    private val createAuthenticatedOrder: CreateAuthenticatedOrder = inject(),
    private val confirmPayment: ConfirmPayment = inject(),
    private val getMenuItems: GetMenuItems = inject(),
    val cartManager: CartManager = inject(),
    private val tableSession: TableSession = inject(),
    private val networkPreference: NetworkPreference = inject(),
    private val getUserOffers: GetUserOffers = inject(),
    private val getOrderDetail: GetOrderDetail = inject(),
    private val getMyOrders: GetMyOrders = inject()
) : ScreenModel {

    private val _state = MutableStateFlow(CartUiState())
    val state: StateFlow<CartUiState> = _state.asStateFlow()

    init {
        loadPendingOrders()
        loadUserOffers()
    }

    fun loadPendingOrders() {
        screenModelScope.launch {
            val currentIdx = _state.value.selectedOrderIndex
            val currentOrders = _state.value.pendingOrders
            val currentId = if (currentIdx in currentOrders.indices) currentOrders[currentIdx].id else null
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val isLoggedIn = networkPreference.accessToken().get().isNotBlank()
                if (isLoggedIn) {
                    val allOrders = getMyOrders.await()
                    val pending = allOrders.filter { it.status != "DONE" && it.status != "CANCELLED" }
                    val selectedIndex = if (currentId != null) {
                        pending.indexOfFirst { it.id == currentId }.coerceAtLeast(0)
                    } else {
                        val storedId = tableSession.getActiveOrderId()
                        if (storedId > 0) pending.indexOfFirst { it.id == storedId }.coerceAtLeast(0) else 0
                    }
                    _state.value = _state.value.copy(
                        pendingOrders = pending,
                        selectedOrderIndex = selectedIndex,
                        isLoading = false
                    )
                } else {
                    // Guest: load single active order
                    if (tableSession.hasActiveOrder()) {
                        val orderId = tableSession.getActiveOrderId()
                        val order = if (tableSession.hasTrackingToken()) {
                            getOrderDetail.trackGuest(orderId, tableSession.getTrackingToken())
                        } else {
                            getOrderDetail.await(orderId)
                        }
                        _state.value = _state.value.copy(
                            pendingOrders = if (order.status != "DONE" && order.status != "CANCELLED") listOf(order) else emptyList(),
                            selectedOrderIndex = 0,
                            isLoading = false
                        )
                    } else {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            } catch (_: Exception) {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    fun selectOrder(index: Int) {
        _state.value = _state.value.copy(selectedOrderIndex = index)
    }

    fun setCartTab(index: Int) {
        _state.value = _state.value.copy(selectedCartTab = index)
        if (index == 1) loadPendingOrders()
    }

    fun refreshSelectedOrder() {
        val orders = _state.value.pendingOrders
        val idx = _state.value.selectedOrderIndex
        if (idx in orders.indices) {
            screenModelScope.launch {
                _state.value = _state.value.copy(isRefreshing = true)
                try {
                    val order = if (tableSession.hasTrackingToken()) {
                        getOrderDetail.trackGuest(orders[idx].id, tableSession.getTrackingToken())
                    } else {
                        getOrderDetail.await(orders[idx].id)
                    }
                    val updated = orders.toMutableList().also { it[idx] = order }
                    val pending = updated.filter { it.status != "DONE" && it.status != "CANCELLED" }
                    val newIndex = (pending.indexOfFirst { it.id == order.id }).coerceAtLeast(0)
                    _state.value = _state.value.copy(pendingOrders = pending, selectedOrderIndex = newIndex, isRefreshing = false)
                } catch (e: Exception) {
                    _state.value = _state.value.copy(isRefreshing = false, error = e.message ?: "Failed to refresh order")
                }
            }
        } else {
            loadPendingOrders()
        }
    }

    fun clearActiveOrder() {
        tableSession.clearActiveOrder()
        _state.value = _state.value.copy(pendingOrders = emptyList(), selectedOrderIndex = 0)
    }

    fun loadUserOffers() {
        screenModelScope.launch {
            try {
                val offers = getUserOffers.await()
                val available = offers.filter { it.status == "AVAILABLE" }
                val pendingId = OfferSelectionStore.pendingUserOfferId
                val selectedId = _state.value.selectedUserOfferId
                val finalSelectedId = if (pendingId != null) {
                    OfferSelectionStore.pendingUserOfferId = null
                    if (available.any { it.id == pendingId }) pendingId else null
                } else {
                    if (selectedId != null && available.any { it.id == selectedId }) selectedId else null
                }
                _state.value = _state.value.copy(
                    userOffers = available,
                    selectedUserOfferId = finalSelectedId
                )
            } catch (_: Exception) { }
        }
    }

    fun selectOffer(userOfferId: Int?) {
        _state.value = _state.value.copy(selectedUserOfferId = userOfferId)
    }

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

    fun initiatePayment() {
        screenModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val isLoggedIn = networkPreference.accessToken().get().isNotBlank()
                val tableId = tableSession.getTableId().takeIf { it > 0 }
                val items = cartManager.items.value.map { it.menuItem.id to it.quantity }

                val selectedOfferId = _state.value.selectedUserOfferId

                val order = if (isLoggedIn) {
                    createAuthenticatedOrder.await(tableId = tableId, userOfferId = selectedOfferId, items = items)
                } else {
                    createOrder.await(tableId = tableId, items = items)
                }

                val token = order.trackingToken
                cartManager.clear()
                tableSession.setActiveOrderId(order.id)
                _state.value = _state.value.copy(
                    isLoading = false,
                    paymentUrl = order.payment?.paymentUrl,
                    pendingOrderId = order.id,
                    lastOrder = order
                )

                if (token != null) {
                    tableSession.setTrackingToken(token)
                }
                loadPendingOrders()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun cancelOrder(orderId: Int) {
        screenModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val trackingToken = if (tableSession.hasTrackingToken()) {
                    tableSession.getTrackingToken()
                } else null
                cancelOrder.await(orderId, trackingToken)
                tableSession.clearActiveOrder()
                loadPendingOrders()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to cancel order"
                )
            }
        }
    }

    fun confirmPayment(orderId: Int) {
        screenModelScope.launch {
            try {
                confirmPayment.await(orderId)
                cartManager.clear()
                tableSession.setActiveOrderId(orderId)
                _state.value = _state.value.copy(
                    paymentUrl = null,
                    pendingOrderId = null,
                    orderSuccess = true
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to confirm payment"
                )
            }
        }
    }

    fun clearPaymentUrl() {
        _state.value = _state.value.copy(paymentUrl = null)
    }

    fun payExistingOrder(order: Order) {
        if (order.payment?.paymentUrl != null) {
            _state.value = _state.value.copy(
                paymentUrl = order.payment?.paymentUrl,
                pendingOrderId = order.id
            )
        }
    }

    fun tableNumber(): String = tableSession.getTableNumber()

    fun dismissOrderSuccess() {
        _state.value = _state.value.copy(orderSuccess = false, lastOrder = null, isLoading = true)
        loadPendingOrders()
    }
}
