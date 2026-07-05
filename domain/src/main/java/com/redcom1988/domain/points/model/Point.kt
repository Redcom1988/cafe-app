package com.redcom1988.domain.points.model

import java.time.LocalDateTime

data class Point(
    val id: Int,
    val userId: Int,
    val orderId: Int?,
    val value: Int,
    val type: PointType,
    val createdAt: LocalDateTime
)

enum class PointType {
    EARN,
    REDEEM,
    ADJUSTMENT;

    companion object {
        fun fromString(value: String): PointType = when (value.uppercase()) {
            "EARN" -> EARN
            "REDEEM" -> REDEEM
            "ADJUSTMENT" -> ADJUSTMENT
            else -> EARN
        }
    }
}