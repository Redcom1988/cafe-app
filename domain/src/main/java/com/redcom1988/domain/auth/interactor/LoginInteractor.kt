package com.redcom1988.domain.auth.interactor

import com.redcom1988.core.network.NetworkPreference
import com.redcom1988.domain.auth.model.AuthToken
import com.redcom1988.domain.auth.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Login(
    private val authRepository: AuthRepository,
    private val networkPreference: NetworkPreference
) {
    suspend fun await(email: String, password: String): AuthToken = withContext(Dispatchers.IO) {
        val authToken = authRepository.login(email, password)
        networkPreference.accessToken().set(authToken.accessToken)
        authToken
    }
}
