package com.redcom1988.domain.analytics.interactor

import com.redcom1988.domain.analytics.model.TopItem
import com.redcom1988.domain.analytics.repository.AnalyticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetTopItems(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend fun await(limit: Int? = null): List<TopItem> = withContext(Dispatchers.IO) {
        analyticsRepository.getTopItems(limit)
    }
}
