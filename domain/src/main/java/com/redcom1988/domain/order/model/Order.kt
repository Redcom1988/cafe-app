package com.redcom1988.domain.order.model

data class Order(
    val id: Int,
    val orderCode: String,
    val customerName: String? = null,
    val status: String,
    val totalPrice: String,
    val finalPrice: String,
    val items: List<OrderItem>,
    val createdAt: String? = null
)
