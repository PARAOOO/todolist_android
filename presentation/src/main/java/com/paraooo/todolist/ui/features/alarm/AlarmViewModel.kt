package com.paraooo.todolist.ui.features.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.repository.TodoReadRepository
import com.paraooo.domain.repository.TodoWriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val todoReadRepository: TodoReadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmUiState())
    val uiState: StateFlow<AlarmUiState> = _uiState.asStateFlow()

    fun onEvent(event : AlarmUiEvent) {
        when (event) {
            is AlarmUiEvent.onInit -> {

                viewModelScope.launch(Dispatchers.IO) {
                    val todoTemplate = todoReadRepository.findTodoById(event.instanceId)

                    _uiState.update { state ->
                        state.copy(
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

}