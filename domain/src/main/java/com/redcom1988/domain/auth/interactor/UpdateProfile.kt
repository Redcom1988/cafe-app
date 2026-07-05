package com.redcom1988.domain.auth.interactor

import com.redcom1988.domain.auth.model.User
import com.redcom1988.domain.auth.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateProfile(
    private val authRepository: AuthRepository
) {
    suspend fun await(name: String, username: String, email: String, phoneNumber: String?): User = withContext(Dispatchers.IO) {
        authRepository.updateProfile(name, username, email, phoneNumber)
    }
}
