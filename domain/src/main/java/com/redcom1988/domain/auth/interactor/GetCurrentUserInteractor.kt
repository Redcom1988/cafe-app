package com.redcom1988.domain.auth.interactor

import com.redcom1988.domain.auth.model.User
import com.redcom1988.domain.auth.repository.AuthRepository

class GetCurrentUser(
    private val authRepository: AuthRepository
) {
    suspend fun await(): User {
        return authRepository.getMe()
    }
}
