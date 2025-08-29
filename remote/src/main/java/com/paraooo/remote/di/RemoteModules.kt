package com.paraooo.remote.di

import com.paraooo.remote.service.SyncService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://10.0.2.2:8080/"
val networkModule = module {
    single {
        OkHttpClient.Builder()
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get()) // Koin이 OkHttpClient를 찾아서 주입
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(SyncService::class.java)
    }
}

val remoteModules = module {
    includes(networkModule)
}