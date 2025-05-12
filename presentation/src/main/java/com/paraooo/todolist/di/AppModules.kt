package com.paraooo.todolist.di

import androidx.room.Room
import com.paraooo.data.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.data.datasource.TodoInstanceLocalDataSource
import com.paraooo.data.datasource.TodoPeriodLocalDataSource
import com.paraooo.data.datasource.TodoTemplateLocalDataSource
import com.paraooo.data.local.database.TodoDatabase
import com.paraooo.data.local.migrations.MIGRATION_1_2
import com.paraooo.data.local.migrations.MIGRATION_2_5
import com.paraooo.data.local.migrations.MIGRATION_5_7
import com.paraooo.data.platform.alarm.AlarmScheduler
import com.paraooo.data.platform.alarm.IntentProvider
import com.paraooo.data.platform.alarm.NotificationHelper
import com.paraooo.data.platform.handler.AlarmHandler
import com.paraooo.data.platform.handler.AlarmRestoreHandler
import com.paraooo.data.repository.TodoReadRepositoryImpl
import com.paraooo.data.repository.TodoWriteRepositoryImpl
import com.paraooo.domain.repository.TodoReadRepository
import com.paraooo.domain.repository.TodoWriteRepository
import com.paraooo.domain.repository.WidgetUpdater
import com.paraooo.todolist.ui.features.alarm.AlarmViewModel
import com.paraooo.todolist.ui.features.create.CreateViewModel
import com.paraooo.todolist.ui.features.edit.EditViewModel
import com.paraooo.todolist.ui.features.home.HomeViewModel
import com.paraooo.todolist.ui.features.widget.WidgetUpdaterImpl
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
            MIGRATION_2_5,
            MIGRATION_5_7
        )
        .build()
    }

    single { get<TodoDatabase>().todoTemplateDao() }
    single { get<TodoDatabase>().todoInstanceDao() }
    single { get<TodoDatabase>().todoPeriodDao() }
    single { get<TodoDatabase>().todoDayOfWeekDao() }
}

val alarmSchedulerModule = module {
    single { AlarmScheduler(androidContext()) }
}

val dataSourceModule = module {
    single { TodoTemplateLocalDataSource(get()) }
    single { TodoInstanceLocalDataSource(get()) }
    single { TodoPeriodLocalDataSource(get()) }
    single { TodoDayOfWeekLocalDataSource(get()) }
}

val repositoryModule = module {
    single<TodoWriteRepository> {TodoWriteRepositoryImpl(get(), get(), get(), get(), get(), get())}
    single<TodoReadRepository> {TodoReadRepositoryImpl(get(), get(), get(), get())}
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CreateViewModel(get()) }
    viewModel { EditViewModel(get(), get())}
    viewModel { AlarmViewModel(get()) }
}

val notificationModule = module {
    single<IntentProvider> { AppIntentProvider() }
    single { NotificationHelper(get()) }
}

val handlerModule = module {
    single  { AlarmHandler(get(), get(), get(), get(), get(), get(), get()) }
    single { AlarmRestoreHandler(get(), get(), get(), get()) }
}


val updaterModule = module {
    single<WidgetUpdater> { WidgetUpdaterImpl(androidContext(),get()) }
}

// DI 모듈 리스트
val appModules = listOf(handlerModule, notificationModule, databaseModule, alarmSchedulerModule, dataSourceModule, repositoryModule, updaterModule, viewModelModule)
