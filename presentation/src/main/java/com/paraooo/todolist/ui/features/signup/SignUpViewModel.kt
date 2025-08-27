package com.paraooo.todolist.ui.features.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.todolist.ui.features.home.HomeUiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val initialUiState: SignUpUiState = SignUpUiState()
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onEvent(event: SignUpUiEvent) {
        viewModelScope.launch {
            when (event) {
                is SignUpUiEvent.onEmailInputChanged -> {
                    _uiState.update { state ->
                        val newState = state.copy(
                            email = event.email
                        )
                        val errorMessage = if(newState.isEmailValid) null else "올바른 이메일 형식이 아닙니다."

                        newState.copy(
                            emailErrorMessage = errorMessage
                        )
                    }
                }

                is SignUpUiEvent.onNicknameInputChanged -> {
                    _uiState.update { state ->
                        val newState = state.copy(
                            nickname = event.nickname
                        )
                        val errorMessage = when {
                            !newState.isNicknameAlphabetKoreanDigitOnly -> "닉네임은 한글, 영문, 숫자만 사용할 수 있습니다"
                            !newState.isNicknameLengthValid -> "닉네임은 2자 이상 10자 이하로 입력해주세요"
                            else -> null
                        }
                        newState.copy(
                            nicknameErrorMessage = errorMessage
                        )
                    }
                }
                is SignUpUiEvent.onPasswordInputChanged -> {
                    _uiState.update { state ->
                        val newState = state.copy(
                            password = event.password
                        )
                        val errorMessage = when {
                            !newState.isPasswordLengthValid -> "비밀번호는 8자 이상 20자 이하로 입력해주세요"
                            !newState.isPasswordContainAlphabetDigitSpecialChar -> "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다"
                            !newState.isPasswordAlphabetDigitSpecialCharOnly -> "비밀번호는 영문, 숫자, 특수문자만 사용할 수 있습니다"
                            else -> null
                        }
                        newState.copy(
                            passwordErrorMessage = errorMessage
                        )
                    }
                }
                is SignUpUiEvent.onPasswordCheckInputChanged -> {
                    _uiState.update { state ->
                        val newState = state.copy(
                            passwordCheck = event.passwordCheck
                        )
                        val errorMessage = if(newState.isPasswordCheckValid) null else "비밀번호가 일치하지 않습니다."

                        newState.copy(
                            passwordCheckErrorMessage = errorMessage
                        )
                    }
                }
                SignUpUiEvent.onSignUpClicked -> TODO()
            }
        }
    }
}