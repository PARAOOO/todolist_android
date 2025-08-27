package com.paraooo.todolist.ui.features.signup

sealed class SignUpUiEvent {

    data class onEmailInputChanged(val email: String) : SignUpUiEvent()
    data class onNicknameInputChanged(val nickname: String) : SignUpUiEvent()
    data class onPasswordInputChanged(val password: String) : SignUpUiEvent()
    data class onPasswordCheckInputChanged(val passwordCheck: String) : SignUpUiEvent()
    object onSignUpClicked : SignUpUiEvent()

}