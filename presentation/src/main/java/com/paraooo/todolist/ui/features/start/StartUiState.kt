package com.paraooo.todolist.ui.features.start

import com.paraooo.domain.model.AlarmType

data class StartUiState (
    val email: String = "",
    val password: String = "",
    val errorMessage: String? = null
) {
    val isLoginButtonEnabled: Boolean
        get() {
            val isEmailValid = !email.isEmpty()
            val isPasswordValid = !password.isEmpty()

            return isEmailValid && isPasswordValid
        }
}
