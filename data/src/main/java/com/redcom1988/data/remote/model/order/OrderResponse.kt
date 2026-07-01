package com.redcom1988.data.remote.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(
    val orderId: Int,
    val orderCode: String,
    val customerName: String? = null,
    val status: String,
    val totalPrice: String,
    val finalPrice: String,
    val orderItems: List<OrderItemResponse>,
    val createdAt: String? = null
)

@Serializable
data class OrderItemResponse(
    val orderItemId: Int,
    val menuItemId: Int,
    val quantity: Int,
    val itemPriceAtOrder: String,
    val subtotal: String,
    val note: String? = null
)
