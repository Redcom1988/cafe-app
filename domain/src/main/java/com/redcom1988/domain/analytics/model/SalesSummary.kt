package com.redcom1988.domain.analytics.model

data class SalesSummary(
    val totalRevenue: String,
    val grossRevenue: String,
    val totalDiscount: String,
    val totalOrders: Int,
    val totalItemsSold: Int,
    val averageOrderValue: String
)

data class TopItem(
    val menuItemId: Int? = null,
    val name: String,
    val totalSold: Int,
    val revenue: String? = null
)

data class DailySummary(
    val date: String,
    val totalOrders: Int,
    val totalRevenue: String,
    val activeTables: Int,
    val pendingOrders: Int
)
