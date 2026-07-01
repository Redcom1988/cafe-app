package com.redcom1988.domain.offer.interactor

import com.redcom1988.domain.offer.model.UserOffer
import com.redcom1988.domain.offer.repository.OfferRepository

class RedeemOffer(
    private val offerRepository: OfferRepository
) {
    suspend fun await(userId: Int, offerId: Int): UserOffer {
        return offerRepository.redeemOffer(userId, offerId)
    }
}
