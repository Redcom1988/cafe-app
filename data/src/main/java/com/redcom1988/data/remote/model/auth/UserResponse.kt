package com.redcom1988.data.remote.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val userId: Int,
    val username: String,
    val email: String,
    val name: String,
    val role: String,
    val phoneNumber: String? = null,
    val createdAt: String? = null
)
