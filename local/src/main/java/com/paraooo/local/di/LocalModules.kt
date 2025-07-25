package com.paraooo.local.di

import androidx.room.Room
import com.paraooo.local.database.TodoDatabase
import com.paraooo.local.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.local.datasource.TodoInstanceLocalDataSource
import com.paraooo.local.datasource.TodoPeriodLocalDataSource
import com.paraooo.local.datasource.TodoTemplateLocalDataSource
import com.paraooo.local.datasourceimpl.TodoDayOfWeekLocalDataSourceImpl
import com.paraooo.local.datasourceimpl.TodoInstanceLocalDataSourceImpl
import com.paraooo.local.datasourceimpl.TodoPeriodLocalDataSourceImpl
import com.paraooo.local.datasourceimpl.TodoTemplateLocalDataSourceImpl
import com.paraooo.local.migrations.MIGRATION_1_2
import com.paraooo.local.migrations.MIGRATION_2_5
import com.paraooo.local.migrations.MIGRATION_5_7
import org.koin.dsl.module


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
            .build()
    }

    single { get<TodoDatabase>().todoTemplateDao() }
    single { get<TodoDatabase>().todoInstanceDao() }
    single { get<TodoDatabase>().todoPeriodDao() }
    single { get<TodoDatabase>().todoDayOfWeekDao() }
}

private val dataSourceModule = module {
    single<TodoTemplateLocalDataSource> { TodoTemplateLocalDataSourceImpl(get()) }
    single<TodoInstanceLocalDataSource> { TodoInstanceLocalDataSourceImpl(get()) }
    single<TodoPeriodLocalDataSource> { TodoPeriodLocalDataSourceImpl(get()) }
    single<TodoDayOfWeekLocalDataSource> { TodoDayOfWeekLocalDataSourceImpl(get()) }
}

val localModules = module {
    includes(databaseModule, dataSourceModule)
}