package com.paraooo.local.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_tokens")

class TokenManager(
    private val context: Context,
    private val cryptoManager: CryptoManager
) {
    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = cryptoManager.encrypt(accessToken)
            prefs[KEY_REFRESH_TOKEN] = cryptoManager.encrypt(refreshToken)
        }
    }

    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            val encryptedToken = prefs[KEY_ACCESS_TOKEN]
            encryptedToken?.let { cryptoManager.decrypt(it) }
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            val encryptedToken = prefs[KEY_REFRESH_TOKEN]
            encryptedToken?.let { cryptoManager.decrypt(it) }
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
}