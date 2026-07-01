package com.redcom1988.data.remote.model.order

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    val tableId: Int? = null,
    val userId: Int? = null,
    val customerName: String? = null,
    val items: List<OrderItemRequest>
)

@Serializable
data class OrderItemRequest(
    val menuItemId: Int,
    val quantity: Int,
    val note: String? = null
)
