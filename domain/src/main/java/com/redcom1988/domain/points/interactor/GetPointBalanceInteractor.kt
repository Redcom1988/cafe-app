package com.redcom1988.domain.points.interactor

import com.redcom1988.domain.points.repository.PointRepository

class GetPointBalance(
    private val pointRepository: PointRepository
) {
    suspend fun await(userId: Int): Int {
        return pointRepository.getBalance(userId)
    }
}
