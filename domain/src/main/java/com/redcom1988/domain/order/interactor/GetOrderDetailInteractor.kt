package com.redcom1988.domain.order.interactor

import com.redcom1988.domain.order.model.Order
import com.redcom1988.domain.order.repository.OrderRepository

class GetOrderDetail(
    private val orderRepository: OrderRepository
) {
    suspend fun await(id: Int): Order {
        return orderRepository.getOrderDetail(id)
    }
}
