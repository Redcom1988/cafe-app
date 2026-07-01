package com.redcom1988.domain.analytics.interactor

import com.redcom1988.domain.analytics.model.TopItem
import com.redcom1988.domain.analytics.repository.AnalyticsRepository

class GetTopItems(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend fun await(limit: Int? = null): List<TopItem> {
        return analyticsRepository.getTopItems(limit)
    }
}
