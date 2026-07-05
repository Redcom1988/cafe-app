package com.redcom1988.domain.auth.interactor

import com.redcom1988.domain.auth.model.User
import com.redcom1988.domain.auth.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCurrentUser(
    private val authRepository: AuthRepository
) {
    suspend fun await(): User = withContext(Dispatchers.IO) {
        authRepository.getMe()
    }
}
