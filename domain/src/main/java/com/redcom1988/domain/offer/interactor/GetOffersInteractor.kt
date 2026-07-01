package com.redcom1988.domain.offer.interactor

import com.redcom1988.domain.offer.model.Offer
import com.redcom1988.domain.offer.repository.OfferRepository

class GetOffers(
    private val offerRepository: OfferRepository
) {
    suspend fun await(): List<Offer> {
        return offerRepository.getOffers()
    }
}
