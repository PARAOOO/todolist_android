package com.paraooo.todolist.di

import androidx.room.Room
import com.paraooo.data.datasource.TodoLocalDataSource
import com.paraooo.data.local.database.TodoDatabase
import com.paraooo.data.local.migrations.MIGRATION_1_2
import com.paraooo.data.local.migrations.MIGRATION_2_5
import com.paraooo.data.platform.alarm.AlarmScheduler
import com.paraooo.data.platform.alarm.NotificationHelper
import com.paraooo.data.platform.alarm.NotificationIntentProvider
import com.paraooo.data.repository.TodoRepositoryImpl
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.todolist.ui.features.create.CreateViewModel
import com.paraooo.todolist.ui.features.edit.EditViewModel
import com.paraooo.todolist.ui.features.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            TodoDatabase::class.java,
            "todo-database"
        ).fallbackToDestructiveMigration()
        .addMigrations(
            MIGRATION_1_2,
            MIGRATION_2_5
        )
        .build()
    }

    single { get<TodoDatabase>().todoDao() }
}

val alarmSchedulerModule = module {
    single { AlarmScheduler(androidContext()) }
}

val dataSourceModule = module {
    single { TodoLocalDataSource(get()) }
}

val repositoryModule = module {
    single<TodoRepository> {TodoRepositoryImpl(get(), get())}
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { CreateViewModel(get()) }
    viewModel { EditViewModel(get())}
}

val notificationModule = module {
    single<NotificationIntentProvider> { AppNotificationIntentProvider() }
    single { NotificationHelper(get()) }
}

// DI 모듈 리스트
val appModules = listOf(notificationModule, databaseModule, alarmSchedulerModule, dataSourceModule, repositoryModule, viewModelModule)
