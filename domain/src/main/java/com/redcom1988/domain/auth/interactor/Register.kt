package com.redcom1988.domain.auth.interactor

import com.redcom1988.domain.auth.model.User
import com.redcom1988.domain.auth.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Register(
    private val authRepository: AuthRepository
) {
    suspend fun await(
        username: String, email: String, password: String,
        name: String, phoneNumber: String? = null
    ): User = withContext(Dispatchers.IO) {
        authRepository.register(username, email, password, name, "CUSTOMER", phoneNumber)
    }
}
