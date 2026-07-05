package com.redcom1988.domain.offer.interactor

import com.redcom1988.domain.offer.model.UserOffer
import com.redcom1988.domain.offer.repository.OfferRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RedeemOffer(
    private val offerRepository: OfferRepository
) {
    suspend fun await(offerId: Int): UserOffer = withContext(Dispatchers.IO) {
        offerRepository.redeemOffer(offerId)
    }
}
