package com.redcom1988.domain.order.repository

import com.redcom1988.domain.order.model.Order
interface OrderRepository {
    suspend fun createOrder(tableId: Int?, userId: Int?, customerName: String?, items: List<Pair<Int, Int>>): Order
    suspend fun getOrders(status: String? = null): List<Order>
    suspend fun getPendingOrders(): List<Order>
    suspend fun getMyOrders(userId: Int): List<Order>
    suspend fun getOrderDetail(id: Int): Order
    suspend fun updateOrderStatus(id: Int, status: String)
}
