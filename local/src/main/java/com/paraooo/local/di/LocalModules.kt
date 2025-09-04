package com.paraooo.local.di

import androidx.room.Room
import com.paraooo.local.database.TodoDatabase
import com.paraooo.local.database.TodoDatabase.Companion.roomCallback
import com.paraooo.local.database.TransactionProvider
import com.paraooo.local.datasource.DeletedTodoLocalDataSource
import com.paraooo.local.datasource.SyncTimestampLocalDataSource
import com.paraooo.local.datasource.SyncTodoLocalDataSource
import com.paraooo.local.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.local.datasource.TodoInstanceLocalDataSource
import com.paraooo.local.datasource.TodoPeriodLocalDataSource
import com.paraooo.local.datasource.TodoTemplateLocalDataSource
import com.paraooo.local.datasource.TokenLocalDataSource
import com.paraooo.local.datasourceimpl.DeletedTodoLocalDataSourceImpl
import com.paraooo.local.datasourceimpl.SyncTimestampLocalDataSourceImpl
import com.paraooo.local.datasourceimpl.SyncTodoLocalDataSourceImpl
import com.paraooo.local.datasourceimpl.TodoDayOfWeekLocalDataSourceImpl
import com.paraooo.local.datasourceimpl.TodoInstanceLocalDataSourceImpl
import com.paraooo.local.datasourceimpl.TodoPeriodLocalDataSourceImpl
import com.paraooo.local.datasourceimpl.TodoTemplateLocalDataSourceImpl
import com.paraooo.local.datasourceimpl.TokenLocalDataSourceImpl
import com.paraooo.local.migrations.MIGRATION_1_2
import com.paraooo.local.migrations.MIGRATION_2_5
import com.paraooo.local.migrations.MIGRATION_5_7
import com.paraooo.local.util.CryptoManager
import com.paraooo.local.util.SyncTimestampManager
import com.paraooo.local.util.TokenManager
import org.koin.dsl.module

val storageModule = module {
    single { CryptoManager() }
    single { TokenManager(get(), get()) }
    single { SyncTimestampManager(get()) }
}
private val databaseModule = module {
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
            .addCallback(callback = roomCallback)
            .build()
    }

    single { get<TodoDatabase>().todoTemplateDao() }
    single { get<TodoDatabase>().todoInstanceDao() }
    single { get<TodoDatabase>().todoPeriodDao() }
    single { get<TodoDatabase>().todoDayOfWeekDao() }
    single { get<TodoDatabase>().deletedTodoDao() }
    single { get<TodoDatabase>().syncTodoDao() }
}

private val dataSourceModule = module {
    single<TokenLocalDataSource> { TokenLocalDataSourceImpl(get()) }
    single<TodoTemplateLocalDataSource> { TodoTemplateLocalDataSourceImpl(get()) }
    single<TodoInstanceLocalDataSource> { TodoInstanceLocalDataSourceImpl(get()) }
    single<TodoPeriodLocalDataSource> { TodoPeriodLocalDataSourceImpl(get()) }
    single<TodoDayOfWeekLocalDataSource> { TodoDayOfWeekLocalDataSourceImpl(get()) }
    single<DeletedTodoLocalDataSource> { DeletedTodoLocalDataSourceImpl(get()) }
    single<SyncTodoLocalDataSource> { SyncTodoLocalDataSourceImpl(get()) }
    single<SyncTimestampLocalDataSource> { SyncTimestampLocalDataSourceImpl(get()) }
}

private val providerModule = module {
    single<TransactionProvider> { get<TodoDatabase>() }
}

val localModules = module {
    includes(storageModule, databaseModule, dataSourceModule, providerModule)
}