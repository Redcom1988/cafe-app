package com.redcom1988.domain.offer.repository

import com.redcom1988.domain.offer.model.Offer
import com.redcom1988.domain.offer.model.UserOffer
interface OfferRepository {
    suspend fun getOffers(): List<Offer>
    suspend fun redeemOffer(offerId: Int): UserOffer
    suspend fun getUserOffers(): List<UserOffer>
    suspend fun applyOffer(orderId: Int, userOfferId: Int)
}
