package com.redcom1988.data.repository

import com.redcom1988.core.network.parseAs
import com.redcom1988.data.remote.Cafej3Api
import com.redcom1988.data.remote.model.points.PointBalanceResponse
import com.redcom1988.data.remote.model.points.PointResponse
import com.redcom1988.data.remote.model.points.toDomain
import com.redcom1988.domain.points.model.Point
import com.redcom1988.domain.points.repository.PointRepository

class PointRepositoryImpl(
    private val api: Cafej3Api
) : PointRepository {

    override suspend fun getBalance(): Int {
        val response = api.getPointBalance()
        if (!response.isSuccessful) throw Exception("Failed to fetch balance (HTTP ${response.code})")
        return response.parseAs<PointBalanceResponse>().balance
    }

    override suspend fun getHistory(): List<Point> {
        val response = api.getPointHistory()
        if (!response.isSuccessful) throw Exception("Failed to fetch points history (HTTP ${response.code})")
        return response.parseAs<List<PointResponse>>().map { it.toDomain() }
    }
}
