package com.paraooo.remote.datasourceimpl

import android.util.Log
import com.paraooo.domain.util.DataEmptyException
import com.paraooo.domain.util.NetworkException
import com.paraooo.domain.util.TLException
import com.paraooo.remote.datasource.SyncRemoteDataSource
import com.paraooo.remote.dto.request.SyncRequestDto
import com.paraooo.remote.dto.response.SyncPullResponseDto
import com.paraooo.remote.service.SyncService
import com.paraooo.remote.util.TAG
import com.paraooo.remote.util.handleHttpError

class SyncRemoteDataSourceImpl(
    private val syncService: SyncService
): SyncRemoteDataSource {
    override suspend fun syncPush(request: SyncRequestDto) {
        try {
            val response = syncService.syncPush(request)

            if(!response.isSuccessful) {
                throw handleHttpError(response.code())
            }
        } catch (e: Exception) {
            Log.e(TAG, "syncPush: ${e.message}", e)
            if (e is TLException) throw e
            throw NetworkException("Failed to syncPush: ${e.message}")
        }
    }

    override suspend fun syncPull(lastSyncTimestamp: String): SyncPullResponseDto {
        try {
            val response = syncService.syncPull(lastSyncTimestamp)

            if(response.isSuccessful) {
                return response.body() ?: throw DataEmptyException("SyncPull response body is null")
            } else {
                throw handleHttpError(response.code())
            }
        } catch (e: Exception) {
            Log.e(TAG, "syncPull: ${e.message}", e)
            if (e is TLException) throw e
            throw NetworkException("Failed to syncPull: ${e.message}")
        }
    }
}