package com.paraooo.todolist.ui.features.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.todolist.ui.features.create.CreateUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmUiState())
    val uiState: StateFlow<AlarmUiState> = _uiState.asStateFlow()

    fun onEvent(event : AlarmUiEvent) {
        when (event) {
            is AlarmUiEvent.onInit -> {

                viewModelScope.launch(Dispatchers.IO) {
                    val todoTemplate = todoRepository.findTodoById(event.instanceId)

                    _uiState.value = _uiState.value.copy(
                        instanceId = todoTemplate.instanceId,
                        todoName = todoTemplate.title,
                        vibration = todoTemplate.isAlarmHasVibration,
                        sound = todoTemplate.isAlarmHasSound
                    )
                }
            }
        }
    }

}