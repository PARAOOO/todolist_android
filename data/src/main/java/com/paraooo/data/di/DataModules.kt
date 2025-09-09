package com.paraooo.data.di

import com.paraooo.data.platform.alarm.AlarmSchedulerImpl
import com.paraooo.data.platform.alarm.NotificationHelper
import com.paraooo.data.platform.handler.AlarmHandler
import com.paraooo.data.platform.handler.AlarmRestoreHandler
import com.paraooo.data.platform.logout.LogoutEffectProvider
import com.paraooo.data.platform.logout.LogoutEffectProviderImpl
import com.paraooo.data.platform.sync.SyncPullScheduler
import com.paraooo.data.platform.sync.SyncPullSchedulerImpl
import com.paraooo.data.platform.sync.SyncPushScheduler
import com.paraooo.data.platform.sync.SyncPushSchedulerImpl
import com.paraooo.data.repository.AuthRepositoryImpl
import com.paraooo.data.repository.SyncRepositoryImpl
import com.paraooo.data.repository.TodoDayOfWeekRepositoryImpl
import com.paraooo.data.repository.TodoInstanceRepositoryImpl
import com.paraooo.data.repository.TodoPeriodRepositoryImpl
import com.paraooo.data.repository.TodoRepositoryImpl
import com.paraooo.data.repository.TodoTemplateRepositoryImpl
import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.repository.AuthRepository
import com.paraooo.domain.repository.SyncRepository
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.usecase.auth.LoginUseCase
import com.paraooo.domain.usecase.auth.RefreshTokenUseCase
import com.paraooo.domain.usecase.auth.SendVerificationCodeUseCase
import com.paraooo.domain.usecase.auth.SignUpUseCase
import com.paraooo.domain.usecase.auth.VerifyCodeUseCase
import com.paraooo.domain.usecase.todo.DeleteTodoByIdUseCase
import com.paraooo.domain.usecase.todo.FindTodoByIdUseCase
import com.paraooo.domain.usecase.dayofweek.PostDayOfWeekUseCase
import com.paraooo.domain.usecase.period.PostPeriodTodoUseCase
import com.paraooo.domain.usecase.todo.PostTodoUseCase
import com.paraooo.domain.usecase.dayofweek.UpdateDayOfWeekTodoUseCase
import com.paraooo.domain.usecase.period.UpdatePeriodTodoUseCase
import com.paraooo.domain.usecase.sync.SyncPullUseCase
import com.paraooo.domain.usecase.sync.SyncPushUseCase
import com.paraooo.domain.usecase.todo.ObserveTodosUseCase
import com.paraooo.domain.usecase.todo.SyncDayOfWeekTodoUseCase
import com.paraooo.domain.usecase.todo.UpdateTodoProgressUseCase
import com.paraooo.domain.usecase.todo.UpdateTodoUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val repositoryModule = module {
    single<TodoTemplateRepository> { TodoTemplateRepositoryImpl(get()) }
    single<TodoInstanceRepository> { TodoInstanceRepositoryImpl(get(), get()) }
    single<TodoPeriodRepository> { TodoPeriodRepositoryImpl(get(), get(), get(), get()) }
    single<TodoDayOfWeekRepository> { TodoDayOfWeekRepositoryImpl(get(), get(), get()) }
    single<TodoRepository> { TodoRepositoryImpl(get(), get(),get(),get(), get(), get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<SyncRepository> { SyncRepositoryImpl(get(), get(), get(), get(), get()) }
}

private val schedulerModule = module {
    single<AlarmScheduler> { AlarmSchedulerImpl(androidContext()) }
    single<SyncPushScheduler> { SyncPushSchedulerImpl(androidContext()) }
    single<SyncPullScheduler> { SyncPullSchedulerImpl(androidContext()) }
}

private val handlerModule = module {
    single  { AlarmHandler(get(), get(), get(), get(), get(), get(), get(), get()) }
    single { AlarmRestoreHandler(get(), get(), get(), get(), get()) }
}

private val notificationModule = module {
    single { NotificationHelper(get()) }
}

private val providerModule = module {
    single<LogoutEffectProvider> { LogoutEffectProviderImpl() }
}

private val useCaseModule = module {
    single { PostTodoUseCase(get(), get()) }
    single { PostPeriodTodoUseCase(get(), get()) }
    single { PostDayOfWeekUseCase(get(), get()) }
    single { FindTodoByIdUseCase(get()) }
    single { DeleteTodoByIdUseCase(get(), get()) }
    single { UpdateTodoUseCase(get(), get()) }
    single { UpdateTodoProgressUseCase(get()) }
    single { UpdatePeriodTodoUseCase(get(), get(), get()) }
    single { UpdateDayOfWeekTodoUseCase(get(), get(), get()) }

    single { SyncDayOfWeekTodoUseCase(get(), get(), get()) }
    single { ObserveTodosUseCase(get()) }

    single { LoginUseCase(get()) }
    single { RefreshTokenUseCase(get()) }
    single { SendVerificationCodeUseCase(get()) }
    single { VerifyCodeUseCase(get()) }
    single { SignUpUseCase(get()) }

    single { SyncPushUseCase(get()) }
    single { SyncPullUseCase(get()) }
}

val dataModules = module {
    includes(
        schedulerModule, repositoryModule, handlerModule, providerModule, notificationModule, useCaseModule
    )
}
