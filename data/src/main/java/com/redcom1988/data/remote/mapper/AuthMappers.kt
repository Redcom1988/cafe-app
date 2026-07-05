package com.redcom1988.data.remote.mapper

import com.redcom1988.data.remote.model.auth.LoginResponse
import com.redcom1988.data.remote.model.auth.UserResponse
import com.redcom1988.domain.auth.model.AuthToken
import com.redcom1988.domain.auth.model.User

fun LoginResponse.toDomain(): AuthToken = AuthToken(
    accessToken = accessToken,
    user = user.toDomain()
)

fun UserResponse.toDomain(): User = User(
    id = userId,
    username = username,
    email = email,
    name = name,
    role = role,
    phoneNumber = phoneNumber
)
