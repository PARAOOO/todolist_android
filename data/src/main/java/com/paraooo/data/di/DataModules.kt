package com.paraooo.data.di

import com.paraooo.data.platform.alarm.AlarmSchedulerImpl
import com.paraooo.data.platform.alarm.NotificationHelper
import com.paraooo.data.platform.handler.AlarmHandler
import com.paraooo.data.platform.handler.AlarmRestoreHandler
import com.paraooo.data.repository.TodoDayOfWeekRepositoryImpl
import com.paraooo.data.repository.TodoInstanceRepositoryImpl
import com.paraooo.data.repository.TodoPeriodRepositoryImpl
import com.paraooo.data.repository.TodoTemplateRepositoryImpl
import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.usecase.todo.DeleteTodoByIdUseCase
import com.paraooo.domain.usecase.todo.FindTodoByIdUseCase
import com.paraooo.domain.usecase.todo.GetTodoByDateUseCase
import com.paraooo.domain.usecase.dayofweek.PostDayOfWeekUseCase
import com.paraooo.domain.usecase.period.PostPeriodTodoUseCase
import com.paraooo.domain.usecase.todo.PostTodoUseCase
import com.paraooo.domain.usecase.dayofweek.UpdateDayOfWeekTodoUseCase
import com.paraooo.domain.usecase.period.UpdatePeriodTodoUseCase
import com.paraooo.domain.usecase.todo.UpdateTodoProgressUseCase
import com.paraooo.domain.usecase.todo.UpdateTodoUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module



private val repositoryModule = module {
    single<TodoTemplateRepository> { TodoTemplateRepositoryImpl(get()) }
    single<TodoInstanceRepository> { TodoInstanceRepositoryImpl(get()) }
    single<TodoPeriodRepository> { TodoPeriodRepositoryImpl(get()) }
    single<TodoDayOfWeekRepository> { TodoDayOfWeekRepositoryImpl(get()) }
}


private val alarmSchedulerModule = module {
    single<AlarmScheduler> { AlarmSchedulerImpl(androidContext()) }
}

private val handlerModule = module {
    single  { AlarmHandler(get(), get(), get(), get(), get(), get(), get()) }
    single { AlarmRestoreHandler(get(), get(), get(), get()) }
}

private val notificationModule = module {
    single { NotificationHelper(get()) }
}

private val useCaseModule = module {
    single { PostTodoUseCase(get(), get(), get()) }
    single { PostPeriodTodoUseCase(get(), get(), get(), get()) }
    single { PostDayOfWeekUseCase(get(), get(), get()) }
    single { FindTodoByIdUseCase(get(), get(), get(), get()) }
    single { GetTodoByDateUseCase(get(), get(), get()) }
    single { DeleteTodoByIdUseCase(get(), get(), get()) }
    single { UpdateTodoUseCase(get(), get(), get()) }
    single { UpdateTodoProgressUseCase(get()) }
    single { UpdatePeriodTodoUseCase(get(), get(), get(), get()) }
    single { UpdateDayOfWeekTodoUseCase(get(), get(), get(), get()) }
}

val dataModules = module {
    includes(
        alarmSchedulerModule, repositoryModule, handlerModule, notificationModule, useCaseModule
    )
}
