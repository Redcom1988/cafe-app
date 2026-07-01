package com.redcom1988.domain.offer.repository

import com.redcom1988.domain.offer.model.Offer
import com.redcom1988.domain.offer.model.UserOffer
interface OfferRepository {
    suspend fun getOffers(): List<Offer>
    suspend fun redeemOffer(userId: Int, offerId: Int): UserOffer
    suspend fun getUserOffers(userId: Int): List<UserOffer>
    suspend fun applyOffer(orderId: Int, userOfferId: Int)
}
