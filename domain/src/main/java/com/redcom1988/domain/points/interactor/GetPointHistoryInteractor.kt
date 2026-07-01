package com.redcom1988.domain.points.interactor

import com.redcom1988.domain.points.model.Point
import com.redcom1988.domain.points.repository.PointRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPointHistory(
    private val pointRepository: PointRepository
) {
    suspend fun await(userId: Int): List<Point> = withContext(Dispatchers.IO) {
        pointRepository.getHistory(userId)
    }
}
