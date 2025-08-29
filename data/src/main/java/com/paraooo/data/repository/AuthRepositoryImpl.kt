package com.paraooo.data.repository

import com.paraooo.domain.repository.AuthRepository
import com.paraooo.remote.datasource.AuthRemoteDataSource
import com.paraooo.remote.dto.request.LoginRequestDto
import com.paraooo.remote.dto.request.SendVerificationCodeRequestDto
import com.paraooo.remote.dto.request.SignUpRequestDto
import com.paraooo.remote.dto.request.VerifyCodeRequestDto

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource
): AuthRepository {
    override suspend fun sendVerificationCode(email: String) {
        authRemoteDataSource.sendVerificationCode(SendVerificationCodeRequestDto(email))
    }

    override suspend fun verifyCode(email: String, code: String) {
        authRemoteDataSource.verifyCode(VerifyCodeRequestDto(email, code))
    }

    override suspend fun signUp(email: String, password: String) {
        authRemoteDataSource.signUp(SignUpRequestDto(email, password))
    }

    override suspend fun login(
        email: String,
        password: String
    ): Pair<String, String> {
        val response = authRemoteDataSource.login(LoginRequestDto(email, password))

        return response.accessToken to response.refreshToken
    }

    override suspend fun refreshToken(refreshToken: String): Pair<String, String> {
        val response = authRemoteDataSource.refreshToken(refreshToken)

        return response.accessToken to response.refreshToken
    }
}