package com.redcom1988.data.remote.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(
    val orderId: Int,
    val orderCode: String,
    val customerName: String? = null,
    val status: String,
    val payment: PaymentResponse? = null,
    val totalPrice: String,
    val finalPrice: String,
    val orderItems: List<OrderItemResponse>,
    val createdAt: String? = null,
    val trackingToken: String? = null,
    val trackingTokenExpiresAt: String? = null,
    val cancellationReason: String? = null
)

@Serializable
data class MenuItemBrief(
    val name: String
)

@Serializable
data class OrderItemUnitResponse(
    val orderItemUnitId: Int,
    val status: String
)

@Serializable
data class OrderItemResponse(
    val orderItemId: Int,
    val menuItemId: Int = 0,
    val quantity: Int,
    val itemPriceAtOrder: String? = null,
    val subtotal: String,
    val note: String? = null,
    val menuItem: MenuItemBrief? = null,
    val orderItemUnits: List<OrderItemUnitResponse> = emptyList()
)
