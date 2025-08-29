package com.paraooo.remote.service

//import com.paraooo.remote.BuildConfig
import com.paraooo.remote.dto.request.SyncRequestDto
import com.paraooo.remote.dto.response.SyncPullResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SyncService {

    @GET("api/sync/pull")
    suspend fun syncPull(
        @Query("lastSyncTimestamp") lastSyncTimestamp: String
    ): SyncPullResponseDto

    @POST("api/sync")
    suspend fun sync(
        @Body request: SyncRequestDto
    ): Response<Unit>

}