package com.redcom1988.domain.analytics.interactor

import com.redcom1988.domain.analytics.model.SalesSummary
import com.redcom1988.domain.analytics.repository.AnalyticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSales(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend fun await(from: String? = null, to: String? = null): SalesSummary = withContext(Dispatchers.IO) {
        analyticsRepository.getSales(from, to)
    }
}
