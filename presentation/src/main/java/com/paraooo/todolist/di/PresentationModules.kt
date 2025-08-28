package com.paraooo.todolist.di

import com.paraooo.data.platform.alarm.IntentProvider
import com.paraooo.todolist.ui.features.alarm.AlarmViewModel
import com.paraooo.todolist.ui.features.create.CreateViewModel
import com.paraooo.todolist.ui.features.edit.EditViewModel
import com.paraooo.todolist.ui.features.home.HomeViewModel
import com.paraooo.todolist.ui.features.routine_create.RoutineCreateViewModel
import com.paraooo.todolist.ui.features.signup.FakeSignUpRepository
import com.paraooo.todolist.ui.features.signup.SignUpViewModel
import com.paraooo.todolist.ui.features.start.FakeLoginRepository
import com.paraooo.todolist.ui.features.start.StartViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val fakeRepositoryModule = module {
    single { FakeLoginRepository(get()) }
    single { FakeSignUpRepository(get()) }
}
val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get(),get()) }
    viewModel { StartViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { CreateViewModel(get(), get(), get()) }
    viewModel { EditViewModel(get(), get(), get(), get())}
    viewModel { AlarmViewModel(get()) }
    viewModel { RoutineCreateViewModel() }
}

val intentProviderModule = module {
    single<IntentProvider> { AppIntentProvider() }
}


// DI 모듈 리스트
val presentationModules = module {
    includes (
        fakeRepositoryModule, intentProviderModule, viewModelModule
    )
}