package com.paraooo.todolist.ui.features.signup

sealed class SignUpUiEffect {
    data object onSendVerificationCodeSuccess: SignUpUiEffect()
    data object onVerifyCodeSuccess: SignUpUiEffect()
    data object onSignUpSuccess: SignUpUiEffect()
    data object onSignUpFailed: SignUpUiEffect()
}