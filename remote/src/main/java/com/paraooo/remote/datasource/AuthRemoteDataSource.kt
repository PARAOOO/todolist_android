package com.paraooo.remote.datasource

import com.paraooo.remote.dto.request.LoginRequestDto
import com.paraooo.remote.dto.request.SendVerificationCodeRequestDto
import com.paraooo.remote.dto.request.SignUpRequestDto
import com.paraooo.remote.dto.request.VerifyCodeRequestDto
import com.paraooo.remote.dto.response.LoginResponseDto
import com.paraooo.remote.dto.response.RefreshTokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthRemoteDataSource {

    suspend fun sendVerificationCode(request: SendVerificationCodeRequestDto)

    suspend fun verifyCode(request: VerifyCodeRequestDto)

    suspend fun signUp(request: SignUpRequestDto)

    suspend fun login(request: LoginRequestDto): LoginResponseDto

    suspend fun refreshToken(refreshToken: String): RefreshTokenResponseDto
}