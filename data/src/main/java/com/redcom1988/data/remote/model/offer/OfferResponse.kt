package com.redcom1988.data.remote.model.offer

import kotlinx.serialization.Serializable

@Serializable
data class OfferResponse(
    val offerId: Int,
    val name: String,
    val isAvailable: Boolean,
    val cost: Int,
    val minSpending: Int,
    val discountType: String,
    val discountRate: Int,
    val maxDiscount: Int? = null
)

@Serializable
data class RedeemOfferRequest(
    val offerId: Int
)

@Serializable
data class UserOfferResponse(
    val userOfferId: Int,
    val offerId: Int,
    val userId: Int,
    val status: String,
    val expiresAt: String? = null,
    val offer: OfferResponse? = null
)

@Serializable
data class ApplyOfferRequest(
    val userOfferId: Int
)
