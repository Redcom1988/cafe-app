package com.redcom1988.domain.points.repository

import com.redcom1988.domain.points.model.Point
interface PointRepository {
    suspend fun getBalance(): Int
    suspend fun getHistory(): List<Point>
}
