package com.redcom1988.data.remote.model.analytics

import kotlinx.serialization.Serializable

@Serializable
data class SalesResponse(
    val totalRevenue: String,
    val grossRevenue: String,
    val totalDiscount: String,
    val totalOrders: Int,
    val totalItemsSold: Int,
    val averageOrderValue: String
)

@Serializable
data class TopItemResponse(
    val menuItemId: Int? = null,
    val name: String,
    val totalSold: Int,
    val revenue: String? = null
)

@Serializable
data class DailySummaryResponse(
    val date: String,
    val totalOrders: Int,
    val totalRevenue: String,
    val activeTables: Int,
    val pendingOrders: Int
)
