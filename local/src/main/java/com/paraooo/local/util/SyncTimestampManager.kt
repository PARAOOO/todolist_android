package com.paraooo.local.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class SyncTimestampManager(private val context: Context) {
    companion object {
        private val INITIAL_TIMESTAMP = LocalDateTime.of(1970, 1, 1, 0, 0, 0)
    }

    suspend fun getLastSyncTimestamp(): LocalDateTime {
        return context.userPreferencesDataStore.data
            .map { preferences ->
                val timestampString = preferences[UserPreferencesKeys.KEY_LAST_SYNC_TIMESTAMP]

                if (timestampString != null) {
                    LocalDateTime.parse(timestampString)
                } else {
                    INITIAL_TIMESTAMP
                }
            }.first()
    }

    suspend fun saveLastSyncTimestamp(timestamp: LocalDateTime) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.KEY_LAST_SYNC_TIMESTAMP] = timestamp.toString()
        }
    }
}