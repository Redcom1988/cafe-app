package com.redcom1988.domain.offer.interactor

import com.redcom1988.domain.offer.model.Offer
import com.redcom1988.domain.offer.repository.OfferRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetOffers(
    private val offerRepository: OfferRepository
) {
    suspend fun await(): List<Offer> = withContext(Dispatchers.IO) {
        offerRepository.getOffers()
    }
}
