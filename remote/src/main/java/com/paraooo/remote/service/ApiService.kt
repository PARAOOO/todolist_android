package com.paraooo.remote.service

import com.paraooo.remote.BuildConfig
import com.paraooo.remote.dto.request.LoginRequestDto
import com.paraooo.remote.dto.request.SendVerificationCodeRequestDto
import com.paraooo.remote.dto.request.SignUpRequestDto
import com.paraooo.remote.dto.request.VerifyCodeRequestDto
import com.paraooo.remote.dto.response.LoginResponseDto
import com.paraooo.remote.dto.response.RefreshTokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("api/auth/send-verification-code")
    suspend fun sendVerificationCode(@Body request: SendVerificationCodeRequestDto): Response<Unit>

    @POST("api/auth/verify-code")
    suspend fun verifyCode(@Body request: VerifyCodeRequestDto): Response<Unit> // 성공 여부만 확인

    @POST("api/users/signup")
    suspend fun signUp(@Body request: SignUpRequestDto): Response<Unit> // 성공 여부만 확인

    @POST("api/users/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Header("Authorization") refreshToken: String): Response<RefreshTokenResponseDto>

//    @GET("api/sync/pull")
//    suspend fun pullData(
//        @Query("lastSyncTimestamp") lastSyncTimestamp: String
//    ): SyncPullResponseDto
//
//    @POST("api/sync/push")
//    suspend fun pushData(
//        @Body request: SyncPushRequestDto
//    ): Response<Unit> // 성공 여부만 확인
    companion object {
//        val BASE_URL = BuildConfig.API_BASE_URL
        val BASE_URL = "http://10.0.2.2:8080/"
    }
}