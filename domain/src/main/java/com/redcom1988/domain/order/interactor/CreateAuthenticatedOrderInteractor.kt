package com.redcom1988.domain.order.interactor

import com.redcom1988.domain.order.model.Order
import com.redcom1988.domain.order.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateAuthenticatedOrder(
    private val orderRepository: OrderRepository
) {
    suspend fun await(
        tableId: Int? = null,
        customerName: String? = null,
        userOfferId: Int? = null,
        items: List<Pair<Int, Int>>
    ): Order = withContext(Dispatchers.IO) {
        orderRepository.createAuthenticatedOrder(tableId, customerName, userOfferId, items)
    }
}
