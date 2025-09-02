package com.paraooo.remote.datasource

import com.paraooo.remote.dto.request.SyncRequestDto
import com.paraooo.remote.dto.response.SyncPullResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Query

interface SyncRemoteDataSource {

    suspend fun syncPush(request: SyncRequestDto)

    suspend fun syncPull(lastSyncTimestamp: String): SyncPullResponseDto

}