package com.redcom1988.domain.order.model

data class OrderItem(
    val id: Int,
    val menuItemId: Int,
    val quantity: Int,
    val itemPriceAtOrder: String?,
    val subtotal: String,
    val note: String? = null,
    val menuItemName: String? = null,
    val unitStatuses: List<String> = emptyList()
)
