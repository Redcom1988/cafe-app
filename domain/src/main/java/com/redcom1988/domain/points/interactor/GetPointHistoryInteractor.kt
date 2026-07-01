package com.redcom1988.domain.points.interactor

import com.redcom1988.domain.points.model.Point
import com.redcom1988.domain.points.repository.PointRepository

class GetPointHistory(
    private val pointRepository: PointRepository
) {
    suspend fun await(userId: Int): List<Point> {
        return pointRepository.getHistory(userId)
    }
}
