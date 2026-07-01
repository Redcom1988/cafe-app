package com.redcom1988.data.remote.model.table

import kotlinx.serialization.Serializable

@Serializable
data class TableResponse(
    val tableId: Int,
    val tableNumber: String,
    val qrCode: String,
    val status: String
)
