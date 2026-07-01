package com.redcom1988.domain.offer.interactor

import com.redcom1988.domain.offer.repository.OfferRepository

class ApplyOffer(
    private val offerRepository: OfferRepository
) {
    suspend fun await(orderId: Int, userOfferId: Int) {
        offerRepository.applyOffer(orderId, userOfferId)
    }
}
