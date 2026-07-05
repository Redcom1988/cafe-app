package com.redcom1988.data.remote.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null
)
