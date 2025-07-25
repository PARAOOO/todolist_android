package com.paraooo.todolist.di

import android.app.Application
import com.paraooo.data.di.dataModules
import com.paraooo.local.di.localModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.Module

class KoinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinApplication)
            modules(
                listOf(
                    localModules,
                    dataModules,
                    presentationModules
                )
            )
        }
    }
}