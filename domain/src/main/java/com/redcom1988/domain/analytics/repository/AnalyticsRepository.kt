package com.redcom1988.domain.analytics.repository

import com.redcom1988.domain.analytics.model.DailySummary
import com.redcom1988.domain.analytics.model.SalesSummary
import com.redcom1988.domain.analytics.model.TopItem
interface AnalyticsRepository {
    suspend fun getSales(from: String? = null, to: String? = null): SalesSummary
    suspend fun getTopItems(limit: Int? = null): List<TopItem>
    suspend fun getDailySummary(): DailySummary
    suspend fun syncWarehouse()
}
