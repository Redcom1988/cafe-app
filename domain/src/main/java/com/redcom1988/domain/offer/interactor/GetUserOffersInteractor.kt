package com.redcom1988.domain.offer.interactor

import com.redcom1988.domain.offer.model.UserOffer
import com.redcom1988.domain.offer.repository.OfferRepository

class GetUserOffers(
    private val offerRepository: OfferRepository
) {
    suspend fun await(userId: Int): List<UserOffer> {
        return offerRepository.getUserOffers(userId)
    }
}
