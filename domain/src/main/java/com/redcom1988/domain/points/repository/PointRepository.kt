package com.redcom1988.domain.points.repository

import com.redcom1988.domain.points.model.Point
interface PointRepository {
    suspend fun getBalance(userId: Int): Int
    suspend fun getHistory(userId: Int): List<Point>
}
