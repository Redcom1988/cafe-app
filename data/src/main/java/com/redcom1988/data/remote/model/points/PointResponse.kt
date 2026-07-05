package com.redcom1988.data.remote.model.points

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PointResponse(
    @SerialName("pointsTransactionId") val id: Int,
    val userId: Int,
    val orderId: Int? = null,
    val value: Int,
    val type: String,
    val createdAt: String
)

@Serializable
data class PointBalanceResponse(
    val userId: Int,
    val balance: Int
)