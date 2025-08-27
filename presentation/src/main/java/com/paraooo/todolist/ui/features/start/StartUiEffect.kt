package com.paraooo.todolist.ui.features.start

sealed class StartUiEffect {
    data object onLoginSuccess : StartUiEffect()
}

