package com.paraooo.remote.datasourceimpl

import android.util.Log
import com.paraooo.domain.util.DataEmptyException
import com.paraooo.domain.util.NetworkException
import com.paraooo.domain.util.TLException
import com.paraooo.remote.datasource.AuthRemoteDataSource
import com.paraooo.remote.dto.request.LoginRequestDto
import com.paraooo.remote.dto.request.SendVerificationCodeRequestDto
import com.paraooo.remote.dto.request.SignUpRequestDto
import com.paraooo.remote.dto.request.VerifyCodeRequestDto
import com.paraooo.remote.dto.response.LoginResponseDto
import com.paraooo.remote.dto.response.RefreshTokenResponseDto
import com.paraooo.remote.service.AuthService
import com.paraooo.remote.util.TAG
import com.paraooo.remote.util.handleHttpError

class AuthRemoteDataSourceImpl(
    private val authService: AuthService
): AuthRemoteDataSource {
    override suspend fun sendVerificationCode(request: SendVerificationCodeRequestDto) {
        try{
            val response = authService.sendVerificationCode(request)

            if(!response.isSuccessful) {
                throw handleHttpError(response.code())
            }
        } catch(e: Exception) {
            Log.e(TAG, "sendVerificationCode: ${e.message}", e)
            if (e is TLException) throw e
            throw NetworkException("Failed to send verification code: ${e.message}")
        }
    }

    override suspend fun verifyCode(request: VerifyCodeRequestDto) {
        try {
            val response = authService.verifyCode(request)

            if(!response.isSuccessful) {
                throw handleHttpError(response.code())
            }
        } catch (e: Exception) {
            Log.e(TAG, "verifyCode: ${e.message}", e)
            if (e is TLException) throw e
            throw NetworkException("Failed to verify code: ${e.message}")
        }
    }

    override suspend fun signUp(request: SignUpRequestDto) {
        try {
            val response = authService.signUp(request)

            if(!response.isSuccessful) {
                throw handleHttpError(response.code())
            }
        } catch (e: Exception) {
            Log.e(TAG, "signUp: ${e.message}", e)
            if (e is TLException) throw e
            throw NetworkException("Failed to sign up: ${e.message}")
        }
    }

    override suspend fun login(request: LoginRequestDto): LoginResponseDto {
        try {
            val response = authService.login(request)

            if(response.isSuccessful) {
                return response.body() ?: throw DataEmptyException("Login response body is null")
            } else {
                throw handleHttpError(response.code())
            }
        } catch (e: Exception) {
            Log.e(TAG, "login: ${e.message}", e)
            if (e is TLException) throw e
            throw NetworkException("Failed to login: ${e.message}")
        }
    }

    override suspend fun refreshToken(refreshToken: String): RefreshTokenResponseDto {
        try {
            val response = authService.refreshToken(refreshToken)

            if(response.isSuccessful) {
                return response.body() ?: throw DataEmptyException("Refresh token response body is null")
            } else {
                throw handleHttpError(response.code())
            }
        } catch (e: Exception) {
            Log.e(TAG, "refreshToken: ${e.message}", e)
            if (e is TLException) throw e
            throw NetworkException("Failed to refresh token: ${e.message}")
        }
    }
}