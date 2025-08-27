package com.paraooo.todolist.ui.features.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.todolist.ui.features.home.HomeUiEffect
import com.paraooo.todolist.ui.features.home.HomeUiEvent
import com.paraooo.todolist.ui.features.home.HomeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartViewModel(
    private val initialUiState : StartUiState = StartUiState(),
    private val fakeLoginRepository: FakeLoginRepository = FakeLoginRepository()
): ViewModel() {
    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<StartUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<StartUiEffect>()
    val effectFlow = _effectChannel.receiveAsFlow()

    fun onEvent(event: StartUiEvent) {
        viewModelScope.launch {
            when(event) {
                is StartUiEvent.onEmailInputChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            email = event.email
                        )
                    }
                }
                StartUiEvent.onLoginButtonClicked -> {

                    val result = withContext(Dispatchers.IO) {
                        fakeLoginRepository.login(email = _uiState.value.email, password = _uiState.value.password)
                    }

                    when(result) {
                        is UseCaseResult.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    loginErrorMessage = "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
                                )
                            }
                        }
                        is UseCaseResult.Failure -> {
                            _uiState.update { state ->
                                state.copy(
                                    loginErrorMessage = result.message
                                )
                            }
                        }
                        is UseCaseResult.Success<Tokens> -> {
                            val result = withContext(Dispatchers.IO) {
                                fakeLoginRepository.storeTokens(result.data.accessToken, result.data.refreshToken)
                            }

                            when(result) {
                                is UseCaseResult.Error -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            loginErrorMessage = "사용자 정보 저장 과정에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
                                        )
                                    }
                                }
                                is UseCaseResult.Failure -> {}
                                is UseCaseResult.Success<*> -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            loginErrorMessage = null
                                        )
                                    }

                                    _effectChannel.send(StartUiEffect.onLoginSuccess)
                                }
                            }
                        }
                    }

                }
                StartUiEvent.onMissPasswordClicked -> {}
                is StartUiEvent.onPasswordInputChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            password = event.password
                        )
                    }
                }
                StartUiEvent.onSignUpButtonClicked -> {}
            }
        }
    }
}