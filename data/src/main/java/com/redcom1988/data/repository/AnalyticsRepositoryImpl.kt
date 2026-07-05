package com.redcom1988.data.repository

import com.redcom1988.core.network.parseAs
import com.redcom1988.data.remote.Cafej3Api
import com.redcom1988.data.remote.mapper.toDomain
import com.redcom1988.data.remote.model.analytics.DailySummaryResponse
import com.redcom1988.data.remote.model.analytics.SalesResponse
import com.redcom1988.data.remote.model.analytics.TopItemResponse
import com.redcom1988.domain.analytics.model.DailySummary
import com.redcom1988.domain.analytics.model.SalesSummary
import com.redcom1988.domain.analytics.model.TopItem
import com.redcom1988.domain.analytics.repository.AnalyticsRepository

class AnalyticsRepositoryImpl(
    private val api: Cafej3Api
) : AnalyticsRepository {

    override suspend fun getSales(from: String?, to: String?): SalesSummary {
        val response = api.getSales(from, to)
        if (!response.isSuccessful) throw Exception("Failed to fetch sales (HTTP ${response.code})")
        return response.parseAs<SalesResponse>().toDomain()
    }

    override suspend fun getTopItems(limit: Int?): List<TopItem> {
        val response = api.getTopItems(limit)
        if (!response.isSuccessful) throw Exception("Failed to fetch top items (HTTP ${response.code})")
        return response.parseAs<List<TopItemResponse>>().map { it.toDomain() }
    }

    override suspend fun getDailySummary(): DailySummary {
        val response = api.getDailySummary()
        if (!response.isSuccessful) throw Exception("Failed to fetch daily summary (HTTP ${response.code})")
        return response.parseAs<DailySummaryResponse>().toDomain()
    }

    override suspend fun syncWarehouse() {
        val response = api.syncWarehouse()
        if (!response.isSuccessful) throw Exception("Failed to sync warehouse (HTTP ${response.code})")
    }
}
