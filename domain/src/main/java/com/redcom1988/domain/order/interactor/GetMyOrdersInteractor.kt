package com.redcom1988.domain.order.interactor

import com.redcom1988.domain.order.model.Order
import com.redcom1988.domain.order.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMyOrders(
    private val orderRepository: OrderRepository
) {
    suspend fun await(): List<Order> = withContext(Dispatchers.IO) {
        orderRepository.getMyOrders()
    }
}
