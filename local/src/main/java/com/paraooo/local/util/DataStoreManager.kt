package com.paraooo.local.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.time.LocalDateTime

internal val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

object UserPreferencesKeys {
    val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
    val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val KEY_LAST_SYNC_TIMESTAMP = stringPreferencesKey("last_sync_timestamp")
}