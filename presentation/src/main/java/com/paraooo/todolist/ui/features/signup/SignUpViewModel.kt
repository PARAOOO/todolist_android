package com.paraooo.todolist.ui.features.signup

import android.annotation.SuppressLint
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.todolist.ui.features.home.HomeUiEffect
import com.paraooo.todolist.ui.features.home.HomeUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(
    private val fakeSignUpRepository: FakeSignUpRepository,
    private val initialUiState: SignUpUiState = SignUpUiState(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<SignUpUiEffect>()
    val effectFlow = _effectChannel.receiveAsFlow()

    private var timerJob: Job? = null

    private fun startTimer(durationSeconds: Int) {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            for (seconds in durationSeconds downTo 0) {
                _uiState.update {
                    it.copy(
                        verificationState = VerificationState.WAITING(seconds)
                    )
                }
                delay(1000L) // 1초 대기
            }

            _uiState.update {
                it.copy(
                    verificationState = VerificationState.NONE
                )
            }
        }
    }

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

                is SignUpUiEvent.onVerificationCodeInputChanged -> {
                    _uiState.update { state ->
                        val newState = state.copy(
                            verificationCode = event.verificationCode
                        )
                        val errorMessage = null
                        newState.copy(
                            verificationCodeErrorMessage = errorMessage
                        )
                    }
                }

                SignUpUiEvent.onGetVerificationCodeButtonClicked -> {
                    when(uiState.value.verificationState) {
                        VerificationState.NONE -> {
                            val result = withContext(Dispatchers.IO) {
                                fakeSignUpRepository.sendVerificationCode(uiState.value.email)
                            }

                            when(result) {
                                is UseCaseResult.Error -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            emailErrorMessage = "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요"
                                        )
                                    }
                                }
                                is UseCaseResult.Failure -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            emailErrorMessage = result.message
                                        )
                                    }
                                }
                                is UseCaseResult.Success<*> -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            emailErrorMessage = null
                                        )
                                    }

                                    _effectChannel.send(SignUpUiEffect.onSendVerificationCodeSuccess)

                                    startTimer(300)
                                }
                            }
                        }
                        VerificationState.SUCCESS -> {}
                        is VerificationState.WAITING -> {
                            val result = withContext(Dispatchers.IO) {
                                fakeSignUpRepository.verifyCode(uiState.value.email, uiState.value.verificationCode)
                            }

                            when(result) {
                                is UseCaseResult.Error -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            verificationCodeErrorMessage = "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요"
                                        )
                                    }
                                }
                                is UseCaseResult.Failure -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            verificationCodeErrorMessage = result.message
                                        )
                                    }
                                }
                                is UseCaseResult.Success<*> -> {

                                    timerJob?.cancel()

                                    _uiState.update { state ->
                                        state.copy(
                                            verificationState = VerificationState.SUCCESS,
                                            verificationCodeErrorMessage = null
                                        )
                                    }

                                    _effectChannel.send(SignUpUiEffect.onVerifyCodeSuccess)

                                }
                            }
                        }
                    }
                }

                SignUpUiEvent.onSignUpClicked -> {
                    val result = withContext(Dispatchers.IO) {
                        fakeSignUpRepository.signUp(uiState.value.nickname, uiState.value.email, uiState.value.password)
                    }

                    when(result) {
                        is UseCaseResult.Error -> {
                            _effectChannel.send(SignUpUiEffect.onSignUpFailed)
                        }
                        is UseCaseResult.Failure -> {}
                        is UseCaseResult.Success<*> -> {
                            _effectChannel.send(SignUpUiEffect.onSignUpSuccess)
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}