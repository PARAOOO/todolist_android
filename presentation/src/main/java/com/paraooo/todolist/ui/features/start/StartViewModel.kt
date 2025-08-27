package com.paraooo.todolist.ui.features.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.todolist.ui.features.home.HomeUiEvent
import com.paraooo.todolist.ui.features.home.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartViewModel(
    private val initialUiState : StartUiState = StartUiState()
): ViewModel() {
    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<StartUiState> = _uiState.asStateFlow()

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
                StartUiEvent.onLoginButtonClicked -> {}
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