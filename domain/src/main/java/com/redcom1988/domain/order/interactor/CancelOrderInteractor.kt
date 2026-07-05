package com.redcom1988.domain.order.interactor

import com.redcom1988.domain.order.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CancelOrder(
    private val orderRepository: OrderRepository
) {
    suspend fun await(orderId: Int, trackingToken: String? = null) = withContext(Dispatchers.IO) {
        orderRepository.cancelOrder(orderId, trackingToken)
    }
}
