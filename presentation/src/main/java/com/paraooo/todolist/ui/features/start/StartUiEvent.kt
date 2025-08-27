package com.paraooo.todolist.ui.features.start

sealed class StartUiEvent {
    data class onEmailInputChanged(val email: String): StartUiEvent()

    data class onPasswordInputChanged(val password: String): StartUiEvent()

    data object onLoginButtonClicked: StartUiEvent()

    data object onSignUpButtonClicked: StartUiEvent()

    data object onMissPasswordClicked: StartUiEvent()
}