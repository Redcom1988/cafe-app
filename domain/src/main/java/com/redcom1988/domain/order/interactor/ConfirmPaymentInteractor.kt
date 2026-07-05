package com.redcom1988.domain.order.interactor

import com.redcom1988.domain.order.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConfirmPayment(
    private val orderRepository: OrderRepository
) {
    suspend fun await(orderId: Int) = withContext(Dispatchers.IO) {
        orderRepository.confirmPayment(orderId)
    }
}
