package com.redcom1988.data.repository

import com.redcom1988.core.network.parseAs
import com.redcom1988.data.remote.Cafej3Api
import com.redcom1988.data.remote.mapper.toDomain
import com.redcom1988.data.remote.model.auth.LoginRequest
import com.redcom1988.data.remote.model.auth.LoginResponse
import com.redcom1988.data.remote.model.auth.RegisterRequest
import com.redcom1988.data.remote.model.auth.UpdateProfileRequest
import com.redcom1988.data.remote.model.auth.UserResponse
import com.redcom1988.domain.auth.model.AuthToken
import com.redcom1988.domain.auth.model.User
import com.redcom1988.domain.auth.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: Cafej3Api
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthToken {
        val response = api.login(LoginRequest(email, password))
        if (!response.isSuccessful) throw Exception("Login failed (HTTP ${response.code})")
        return response.parseAs<LoginResponse>().toDomain()
    }

    override suspend fun register(
        username: String, email: String, password: String,
        name: String, role: String, phoneNumber: String?
    ): User {
        val response = api.register(
            RegisterRequest(username, email, password, name, role, phoneNumber)
        )
        if (!response.isSuccessful) throw Exception("Registration failed (HTTP ${response.code})")
        return response.parseAs<UserResponse>().toDomain()
    }

    override suspend fun getMe(): User {
        val response = api.me()
        if (!response.isSuccessful) throw Exception("Failed to get profile (HTTP ${response.code})")
        return response.parseAs<UserResponse>().toDomain()
    }

    override suspend fun updateProfile(name: String?, username: String?, email: String?, phoneNumber: String?): User {
        val response = api.updateProfile(UpdateProfileRequest(name, username, email, phoneNumber))
        if (!response.isSuccessful) throw Exception("Failed to update profile (HTTP ${response.code})")
        return response.parseAs<UserResponse>().toDomain()
    }
}
