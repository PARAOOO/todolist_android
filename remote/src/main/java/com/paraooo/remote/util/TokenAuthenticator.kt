package com.paraooo.remote.util

import android.util.Log
import com.paraooo.local.util.TokenManager
import com.paraooo.remote.service.AuthService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenManager: TokenManager,
    private val authService: AuthService
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            tokenManager.getRefreshToken().first()
        }

        if (refreshToken == null) {
            Log.d(TAG, "authenticate: refreshToken == null")
            return null
        }

        return try {

            val tokenResponse = authService.refreshTokenForAuthenticator(
                "Bearer ${refreshToken}"
            ).execute()

            if (tokenResponse.isSuccessful && tokenResponse.body() != null) {
                val newTokens = tokenResponse.body()!!
                Log.d(TAG, "authenticate: newTokens: ${newTokens}")

                runBlocking {
                    tokenManager.saveTokens(newTokens.accessToken, newTokens.refreshToken)
                }

                return response.request.newBuilder()
                    .header("authorization", "Bearer ${newTokens.accessToken}")
                    .build()

            } else {
                runBlocking { tokenManager.clearTokens() }
                Log.d(TAG, "authenticate: not (tokenResponse.isSuccessful && tokenResponse.body() != null)")
                null
            }
        } catch (e: Exception) {
            runBlocking { tokenManager.clearTokens() }
            Log.e(TAG, "authenticate: ${e}", )
            null
        }
    }
}