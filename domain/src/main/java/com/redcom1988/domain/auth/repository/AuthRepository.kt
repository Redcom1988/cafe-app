package com.redcom1988.domain.auth.repository

import com.redcom1988.domain.auth.model.AuthToken
import com.redcom1988.domain.auth.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthToken
    suspend fun register(username: String, email: String, password: String, name: String, role: String, phoneNumber: String?): User
    suspend fun getMe(): User
    suspend fun updateProfile(name: String?, username: String?, email: String?, phoneNumber: String?): User
}
