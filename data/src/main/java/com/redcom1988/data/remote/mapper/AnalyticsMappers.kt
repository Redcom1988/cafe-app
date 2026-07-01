package com.redcom1988.data.remote.mapper

import com.redcom1988.data.remote.model.analytics.DailySummaryResponse
import com.redcom1988.data.remote.model.analytics.SalesResponse
import com.redcom1988.data.remote.model.analytics.TopItemResponse
import com.redcom1988.domain.analytics.model.DailySummary
import com.redcom1988.domain.analytics.model.SalesSummary
import com.redcom1988.domain.analytics.model.TopItem

fun SalesResponse.toDomain(): SalesSummary = SalesSummary(
    totalRevenue = totalRevenue,
    grossRevenue = grossRevenue,
    totalDiscount = totalDiscount,
    totalOrders = totalOrders,
    totalItemsSold = totalItemsSold,
    averageOrderValue = averageOrderValue
)

fun TopItemResponse.toDomain(): TopItem = TopItem(
    menuItemId = menuItemId,
    name = name,
    totalSold = totalSold,
    revenue = revenue
)

fun DailySummaryResponse.toDomain(): DailySummary = DailySummary(
    date = date,
    totalOrders = totalOrders,
    totalRevenue = totalRevenue,
    activeTables = activeTables,
    pendingOrders = pendingOrders
)
