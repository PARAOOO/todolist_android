package com.paraooo.remote.util

import com.paraooo.local.datasource.TokenLocalDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenLocalDataSource: TokenLocalDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // runBlocking: Interceptor는 동기적으로 동작해야 하므로,
        // 비동기 Flow에서 값을 가져오기 위해 잠시 현재 스레드를 블로킹.
        val accessToken = runBlocking {
            tokenLocalDataSource.getAccessToken().firstOrNull()
        }

        val request = if (accessToken != null) {
            // 토큰이 있으면, 헤더에 추가하여 새로운 요청을 만듦
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            // 토큰이 없으면, 원래 요청을 그대로 사용
            chain.request()
        }

        return chain.proceed(request)
    }
}