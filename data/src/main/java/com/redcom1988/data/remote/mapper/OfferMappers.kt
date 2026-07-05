package com.redcom1988.data.remote.mapper

import com.redcom1988.data.remote.model.offer.OfferResponse
import com.redcom1988.data.remote.model.offer.UserOfferResponse
import com.redcom1988.domain.offer.model.Offer
import com.redcom1988.domain.offer.model.UserOffer

fun OfferResponse.toDomain(): Offer = Offer(
    id = offerId,
    name = name,
    isAvailable = isAvailable,
    cost = cost,
    minSpending = minSpending,
    discountType = discountType,
    discountRate = discountRate,
    maxDiscount = maxDiscount
)

fun UserOfferResponse.toDomain(): UserOffer = UserOffer(
    id = userOfferId,
    offerId = offerId,
    userId = userId,
    status = status,
    expiresAt = expiresAt,
    offer = offer?.toDomain()
)
