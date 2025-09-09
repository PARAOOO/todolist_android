package com.paraooo.todolist.ui.features.splash

sealed class SplashUiEffect {
    object NaviagateToHome: SplashUiEffect()
    object NaviagateToLogin: SplashUiEffect()
}