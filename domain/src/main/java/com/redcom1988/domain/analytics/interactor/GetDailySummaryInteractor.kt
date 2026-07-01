package com.redcom1988.domain.analytics.interactor

import com.redcom1988.domain.analytics.model.DailySummary
import com.redcom1988.domain.analytics.repository.AnalyticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetDailySummary(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend fun await(): DailySummary = withContext(Dispatchers.IO) {
        analyticsRepository.getDailySummary()
    }
}
