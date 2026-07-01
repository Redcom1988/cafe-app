package com.redcom1988.domain.offer.model

data class UserOffer(
    val id: Int,
    val offerId: Int,
    val userId: Int,
    val status: String,
    val expiresAt: String? = null,
    val offer: Offer? = null
)
