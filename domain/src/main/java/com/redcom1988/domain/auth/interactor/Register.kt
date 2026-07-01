package com.redcom1988.domain.auth.interactor

import com.redcom1988.domain.auth.model.User
import com.redcom1988.domain.auth.repository.AuthRepository

class Register(
    private val authRepository: AuthRepository
) {
    suspend fun await(
        username: String, email: String, password: String,
        name: String, role: String = "CUSTOMER", phoneNumber: String? = null
    ): User {
        return authRepository.register(username, email, password, name, role, phoneNumber)
    }
}
