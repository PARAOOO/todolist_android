package com.paraooo.data.repository

import com.auth0.jwt.JWT
import com.paraooo.data.platform.logout.LogoutEffectProvider
import com.paraooo.domain.repository.AuthRepository
import com.paraooo.local.datasource.TokenLocalDataSource
import com.paraooo.remote.datasource.AuthRemoteDataSource
import com.paraooo.remote.dto.request.LoginRequestDto
import com.paraooo.remote.dto.request.SendVerificationCodeRequestDto
import com.paraooo.remote.dto.request.SignUpRequestDto
import com.paraooo.remote.dto.request.VerifyCodeRequestDto
import kotlinx.coroutines.flow.firstOrNull


class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val logoutEffectProvider: LogoutEffectProvider,
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

    override suspend fun login(email: String, password: String): Pair<String, String> {
        val response = authRemoteDataSource.login(LoginRequestDto(email, password))

        tokenLocalDataSource.storeTokens(response.accessToken, response.refreshToken)

        return response.accessToken to response.refreshToken
    }

    override suspend fun refreshToken(refreshToken: String): Pair<String, String> {
        val response = authRemoteDataSource.refreshToken(refreshToken)

        return response.accessToken to response.refreshToken
    }

    override suspend fun isLoggedIn(): Boolean {
        val refreshToken = tokenLocalDataSource.getRefreshToken().firstOrNull()

        if (refreshToken == null) {
            return false
        }

        try {
            val jwt = JWT.decode(refreshToken)
            val exp = jwt.expiresAt?.time ?: return false
            val now = System.currentTimeMillis()
            return now < exp
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun logout() {
        tokenLocalDataSource.clearTokens()

        logoutEffectProvider.triggerLogout()
    }
}