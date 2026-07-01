package com.redcom1988.domain.order.interactor

import com.redcom1988.domain.order.model.Order
import com.redcom1988.domain.order.repository.OrderRepository

class CreateOrder(
    private val orderRepository: OrderRepository
) {
    suspend fun await(
        tableId: Int? = null,
        userId: Int? = null,
        customerName: String? = null,
        items: List<Pair<Int, Int>>
    ): Order {
        return orderRepository.createOrder(tableId, userId, customerName, items)
    }
}
