package com.redcom1988.data.repository

import com.redcom1988.core.network.parseAs
import com.redcom1988.data.remote.Cafej3Api
import com.redcom1988.data.remote.mapper.toDomain
import com.redcom1988.data.remote.model.order.CreateOrderRequest
import com.redcom1988.data.remote.model.order.OrderItemRequest
import com.redcom1988.data.remote.model.order.OrderResponse
import com.redcom1988.data.remote.model.order.UpdateOrderStatusRequest
import com.redcom1988.domain.order.model.Order
import com.redcom1988.domain.order.repository.OrderRepository

class OrderRepositoryImpl(
    private val api: Cafej3Api
) : OrderRepository {

    override suspend fun createOrder(
        tableId: Int?, userId: Int?, customerName: String?, items: List<Pair<Int, Int>>
    ): Order {
        val request = CreateOrderRequest(
            tableId = tableId,
            userId = userId,
            customerName = customerName,
            items = items.map { (menuItemId, quantity) ->
                OrderItemRequest(menuItemId = menuItemId, quantity = quantity)
            }
        )
        val response = api.createOrder(request)
        if (!response.isSuccessful) throw Exception("Failed to create order (HTTP ${response.code})")
        return response.parseAs<OrderResponse>().toDomain()
    }

    override suspend fun getOrders(status: String?): List<Order> {
        val response = api.getOrders(status)
        if (!response.isSuccessful) throw Exception("Failed to fetch orders (HTTP ${response.code})")
        return response.parseAs<List<OrderResponse>>().map { it.toDomain() }
    }

    override suspend fun getPendingOrders(): List<Order> {
        val response = api.getPendingOrders()
        if (!response.isSuccessful) throw Exception("Failed to fetch pending orders (HTTP ${response.code})")
        return response.parseAs<List<OrderResponse>>().map { it.toDomain() }
    }

    override suspend fun getMyOrders(userId: Int): List<Order> {
        val response = api.getMyOrders(userId)
        if (!response.isSuccessful) throw Exception("Failed to fetch my orders (HTTP ${response.code})")
        return response.parseAs<List<OrderResponse>>().map { it.toDomain() }
    }

    override suspend fun getOrderDetail(id: Int): Order {
        val response = api.getOrderDetail(id)
        if (!response.isSuccessful) throw Exception("Order not found (HTTP ${response.code})")
        return response.parseAs<OrderResponse>().toDomain()
    }

    override suspend fun updateOrderStatus(id: Int, status: String) {
        val response = api.updateOrderStatus(id, UpdateOrderStatusRequest(status))
        if (!response.isSuccessful) throw Exception("Failed to update order status (HTTP ${response.code})")
    }
}
