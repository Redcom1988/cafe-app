package com.redcom1988.data.remote.model.order

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    val paymentId: Long,
    val status: String,
    val paymentUrl: String? = null,
    val amount: String? = null,
    val expiresAt: String? = null,
    val paidAt: String? = null,
    val createdAt: String? = null
)
