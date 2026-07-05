package com.redcom1988.domain.order.repository

import com.redcom1988.domain.order.model.Order
interface OrderRepository {
    suspend fun createOrder(tableId: Int?, customerName: String?, items: List<Pair<Int, Int>>): Order
    suspend fun createAuthenticatedOrder(tableId: Int?, customerName: String?, userOfferId: Int? = null, items: List<Pair<Int, Int>>): Order
    suspend fun getOrders(status: String? = null): List<Order>
    suspend fun getPendingOrders(): List<Order>
    suspend fun getMyOrders(): List<Order>
    suspend fun getOrderDetail(id: Int): Order
    suspend fun getGuestOrderDetail(id: Int, trackingToken: String): Order
    suspend fun updateOrderStatus(id: Int, status: String)
    suspend fun confirmPayment(id: Int)
    suspend fun cancelOrder(id: Int, trackingToken: String? = null)
}
