package com.redcom1988.domain.points.interactor

import com.redcom1988.domain.points.repository.PointRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPointBalance(
    private val pointRepository: PointRepository
) {
    suspend fun await(): Int = withContext(Dispatchers.IO) {
        pointRepository.getBalance()
    }
}
