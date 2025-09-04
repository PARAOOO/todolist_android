package com.paraooo.remote.di

import com.paraooo.remote.datasource.AuthRemoteDataSource
import com.paraooo.remote.datasource.SyncRemoteDataSource
import com.paraooo.remote.datasourceimpl.AuthRemoteDataSourceImpl
import com.paraooo.remote.datasourceimpl.SyncRemoteDataSourceImpl
import com.paraooo.remote.service.AuthService
import com.paraooo.remote.service.SyncService
import com.paraooo.remote.util.AuthInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sin

private const val BASE_URL = "http://10.0.2.2:8080/"
val networkModule = module {
    single {
        OkHttpClient.Builder()
            .build()
    }

//    single {
//        Retrofit.Builder()
//            .client(get()) // Koin이 OkHttpClient를 찾아서 주입
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

    single(qualifier = AuthQualifiers.UNAUTHENTICATED) {
        OkHttpClient.Builder()
            .build()
    }

    single(qualifier = AuthQualifiers.UNAUTHENTICATED) {
        Retrofit.Builder()
            .client(get(qualifier = AuthQualifiers.UNAUTHENTICATED))
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



    single(qualifier = AuthQualifiers.AUTHENTICATED) {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(get()))
            .build()
    }

    single(qualifier = AuthQualifiers.AUTHENTICATED) {
        Retrofit.Builder()
            .client(get(qualifier = AuthQualifiers.AUTHENTICATED))
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>(qualifier = AuthQualifiers.AUTHENTICATED).create(SyncService::class.java)
    }
    single {
        get<Retrofit>(qualifier = AuthQualifiers.UNAUTHENTICATED).create(AuthService::class.java)
    }
}

val datasourceModule = module {
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get()) }
    single<SyncRemoteDataSource> { SyncRemoteDataSourceImpl(get()) }
}

val remoteModules = module {
    includes(networkModule, datasourceModule)
}