package com.paraooo.todolist.ui.features.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.usecase.todo.FindTodoByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmViewModel(
    private val findTodoByIdUseCase: FindTodoByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmUiState())
    val uiState: StateFlow<AlarmUiState> = _uiState.asStateFlow()

    fun onEvent(event : AlarmUiEvent) {
        viewModelScope.launch {
            when (event) {
                is AlarmUiEvent.onInit -> {
                    val result = withContext(Dispatchers.IO){
                        findTodoByIdUseCase(event.instanceId)
                    }

                    when(result) {
                        is UseCaseResult.Error -> {

                        }
                        is UseCaseResult.Failure -> {

                        }
                        is UseCaseResult.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    instanceId = result.data.instanceId,
                                    todoName = result.data.title,
                                    vibration = result.data.isAlarmHasVibration,
                                    sound = result.data.isAlarmHasSound
                                )
                            }
                        }
                    }
                }
            }

        }
    }

}