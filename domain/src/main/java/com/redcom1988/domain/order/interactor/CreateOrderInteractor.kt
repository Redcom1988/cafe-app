package com.redcom1988.domain.order.interactor

import com.redcom1988.domain.order.model.Order
import com.redcom1988.domain.order.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateOrder(
    private val orderRepository: OrderRepository
) {
    suspend fun await(
        tableId: Int? = null,
        customerName: String? = null,
        items: List<Pair<Int, Int>>
    ): Order = withContext(Dispatchers.IO) {
        orderRepository.createOrder(tableId, customerName, items)
    }
}
