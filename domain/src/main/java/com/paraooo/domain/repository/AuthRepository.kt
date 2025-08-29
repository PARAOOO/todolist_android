package com.paraooo.domain.repository

interface AuthRepository {
    suspend fun sendVerificationCode(email: String)

    suspend fun verifyCode(email: String, code: String)

    suspend fun signUp(email: String, password: String)

    suspend fun login(email: String, password: String): Pair<String, String>

    suspend fun refreshToken(refreshToken: String): Pair<String, String>
}