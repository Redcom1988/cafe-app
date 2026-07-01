package com.redcom1988.data.remote.model.points

import com.redcom1988.domain.points.model.Point
import com.redcom1988.domain.points.model.PointType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun PointResponse.toDomain(): Point {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    return Point(
        id = id,
        userId = userId,
        orderId = orderId,
        value = value,
        type = PointType.fromString(type),
        createdAt = LocalDateTime.parse(createdAt, formatter)
    )
}