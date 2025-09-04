package com.paraooo.local.datasourceimpl

import com.paraooo.local.datasource.SyncTimestampLocalDataSource
import com.paraooo.local.util.SyncTimestampManager
import java.time.LocalDateTime

class SyncTimestampLocalDataSourceImpl(
    private val syncTimestampManager: SyncTimestampManager
): SyncTimestampLocalDataSource {

    override suspend fun getLastSyncTimestamp(): LocalDateTime {
        return syncTimestampManager.getLastSyncTimestamp()
    }

    override suspend fun saveLastSyncTimestamp(timestamp: LocalDateTime) {
        syncTimestampManager.saveLastSyncTimestamp(timestamp)
    }
}