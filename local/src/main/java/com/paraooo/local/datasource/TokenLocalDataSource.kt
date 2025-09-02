package com.paraooo.local.datasource

import kotlinx.coroutines.flow.Flow

interface TokenLocalDataSource{

    suspend fun storeTokens(accessToken: String, refreshToken: String)

    suspend fun getAccessToken(): Flow<String?>

    suspend fun getRefreshToken(): Flow<String?>

    suspend fun clearTokens()
}
