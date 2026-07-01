package com.redcom1988.domain.auth.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val name: String,
    val role: String,
    val phoneNumber: String? = null
)
