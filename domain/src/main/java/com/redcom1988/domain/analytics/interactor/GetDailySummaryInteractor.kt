package com.redcom1988.domain.analytics.interactor

import com.redcom1988.domain.analytics.model.DailySummary
import com.redcom1988.domain.analytics.repository.AnalyticsRepository

class GetDailySummary(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend fun await(): DailySummary {
        return analyticsRepository.getDailySummary()
    }
}
