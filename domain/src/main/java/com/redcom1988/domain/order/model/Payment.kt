package com.redcom1988.domain.order.model

data class Payment(
    val id: Long,
    val status: String,
    val paymentUrl: String? = null,
    val amount: String? = null,
    val expiresAt: String? = null,
    val paidAt: String? = null,
    val createdAt: String? = null
)
