package com.redcom1988.domain.order.interactor

import com.redcom1988.domain.order.model.Order
import com.redcom1988.domain.order.repository.OrderRepository

class GetMyOrders(
    private val orderRepository: OrderRepository
) {
    suspend fun await(userId: Int): List<Order> {
        return orderRepository.getMyOrders(userId)
    }
}
