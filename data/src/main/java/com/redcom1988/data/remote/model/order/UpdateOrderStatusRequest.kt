package com.redcom1988.data.remote.model.order

import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderStatusRequest(
    val status: String
)
