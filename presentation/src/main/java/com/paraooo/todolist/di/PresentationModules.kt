package com.paraooo.todolist.di

import com.paraooo.data.platform.alarm.IntentProvider
import com.paraooo.todolist.ui.features.alarm.AlarmViewModel
import com.paraooo.todolist.ui.features.create.CreateViewModel
import com.paraooo.todolist.ui.features.edit.EditViewModel
import com.paraooo.todolist.ui.features.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { CreateViewModel(get(), get(), get()) }
    viewModel { EditViewModel(get(), get(), get(), get())}
    viewModel { AlarmViewModel(get()) }
}

val intentProviderModule = module {
    single<IntentProvider> { AppIntentProvider() }
}


// DI 모듈 리스트
val presentationModules = module {
    includes (
        intentProviderModule, viewModelModule
    )
}
