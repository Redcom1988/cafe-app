package com.redcom1988.data.remote.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val name: String,
    val role: String = "CUSTOMER",
    val phoneNumber: String? = null
)
