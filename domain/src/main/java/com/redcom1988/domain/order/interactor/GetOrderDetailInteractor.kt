package com.redcom1988.domain.order.interactor

import com.redcom1988.domain.order.model.Order
import com.redcom1988.domain.order.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetOrderDetail(
    private val orderRepository: OrderRepository
) {
    suspend fun await(id: Int): Order = withContext(Dispatchers.IO) {
        orderRepository.getOrderDetail(id)
    }

    suspend fun trackGuest(id: Int, trackingToken: String): Order = withContext(Dispatchers.IO) {
        orderRepository.getGuestOrderDetail(id, trackingToken)
    }
}
