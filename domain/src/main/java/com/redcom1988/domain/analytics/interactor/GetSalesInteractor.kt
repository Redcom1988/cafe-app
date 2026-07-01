package com.redcom1988.domain.analytics.interactor

import com.redcom1988.domain.analytics.model.SalesSummary
import com.redcom1988.domain.analytics.repository.AnalyticsRepository

class GetSales(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend fun await(from: String? = null, to: String? = null): SalesSummary {
        return analyticsRepository.getSales(from, to)
    }
}
