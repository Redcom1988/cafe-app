package com.redcom1988.data.repository

import com.redcom1988.core.network.parseAs
import com.redcom1988.data.remote.Cafej3Api
import com.redcom1988.data.remote.mapper.toDomain
import com.redcom1988.data.remote.model.offer.ApplyOfferRequest
import com.redcom1988.data.remote.model.offer.OfferResponse
import com.redcom1988.data.remote.model.offer.RedeemOfferRequest
import com.redcom1988.data.remote.model.offer.UserOfferResponse
import com.redcom1988.domain.offer.model.Offer
import com.redcom1988.domain.offer.model.UserOffer
import com.redcom1988.domain.offer.repository.OfferRepository

class OfferRepositoryImpl(
    private val api: Cafej3Api
) : OfferRepository {

    override suspend fun getOffers(): List<Offer> {
        val response = api.getOffers()
        if (!response.isSuccessful) throw Exception("Failed to fetch offers (HTTP ${response.code})")
        return response.parseAs<List<OfferResponse>>().map { it.toDomain() }
    }

    override suspend fun redeemOffer(userId: Int, offerId: Int): UserOffer {
        val response = api.redeemOffer(RedeemOfferRequest(userId, offerId))
        if (!response.isSuccessful) throw Exception("Failed to redeem offer (HTTP ${response.code})")
        return response.parseAs<UserOfferResponse>().toDomain()
    }

    override suspend fun getUserOffers(userId: Int): List<UserOffer> {
        val response = api.getUserOffers(userId)
        if (!response.isSuccessful) throw Exception("Failed to fetch user offers (HTTP ${response.code})")
        return response.parseAs<List<UserOfferResponse>>().map { it.toDomain() }
    }

    override suspend fun applyOffer(orderId: Int, userOfferId: Int) {
        val response = api.applyOffer(orderId, ApplyOfferRequest(userOfferId))
        if (!response.isSuccessful) throw Exception("Failed to apply offer (HTTP ${response.code})")
    }
}
