package com.paraooo.local.datasource

import androidx.datastore.preferences.core.edit
import com.paraooo.local.util.UserPreferencesKeys
import com.paraooo.local.util.userPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

interface SyncTimestampLocalDataSource {

    suspend fun getLastSyncTimestamp(): LocalDateTime

    suspend fun saveLastSyncTimestamp(timestamp: LocalDateTime)
}