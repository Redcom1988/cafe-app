package com.redcom1988.domain.offer.model

data class Offer(
    val id: Int,
    val name: String,
    val isAvailable: Boolean,
    val cost: Int,
    val minSpending: Int,
    val discountType: String,
    val discountRate: Int,
    val maxDiscount: Int? = null
)
