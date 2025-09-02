package com.paraooo.local.datasourceimpl

import com.paraooo.domain.util.TokenStorageException
import com.paraooo.local.datasource.TokenLocalDataSource
import com.paraooo.local.util.TokenManager
import kotlinx.coroutines.flow.Flow

class TokenLocalDataSourceImpl(
    private val tokenManager: TokenManager
): TokenLocalDataSource {
    override suspend fun storeTokens(accessToken: String, refreshToken: String) {
        try{
            tokenManager.saveTokens(accessToken, refreshToken)
        } catch (e: Exception) {
            throw TokenStorageException()
        }
    }

    override suspend fun getAccessToken(): Flow<String?> {
        try {
            return tokenManager.getAccessToken()
        } catch (e: Exception) {
            throw TokenStorageException()
        }
    }

    override suspend fun getRefreshToken(): Flow<String?> {
        try {
            return tokenManager.getRefreshToken()
        } catch (e: Exception) {
            throw TokenStorageException()
        }
    }

    override suspend fun clearTokens() {
        try {
            tokenManager.clearTokens()
        } catch (e: Exception) {
            throw TokenStorageException()
        }
    }
}