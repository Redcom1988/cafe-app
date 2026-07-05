package com.redcom1988.domain.offer.interactor

import com.redcom1988.domain.offer.repository.OfferRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApplyOffer(
    private val offerRepository: OfferRepository
) {
    suspend fun await(orderId: Int, userOfferId: Int) = withContext(Dispatchers.IO) {
        offerRepository.applyOffer(orderId, userOfferId)
    }
}
