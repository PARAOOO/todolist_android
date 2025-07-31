package com.paraooo.todolist.ui.features.routine_create

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.usecase.todo.DeleteTodoByIdUseCase
import com.paraooo.domain.usecase.todo.GetTodoByDateUseCase
import com.paraooo.domain.usecase.todo.UpdateTodoProgressUseCase
import com.paraooo.domain.util.getDateDiff
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class RoutineCreateViewModel(
    private val initialUiState : RoutineCreateUiState = RoutineCreateUiState()
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<RoutineCreateUiState> = _uiState.asStateFlow()

//    private val _effectChannel = Channel<RoutineCreateUiEffect>()
//    val effectFlow = _effectChannel.receiveAsFlow()

    fun onEvent(event: RoutineCreateUiEvent) {
        viewModelScope.launch {
            when (event) {
                is RoutineCreateUiEvent.onNameChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            rootRoutineInput = RootRoutineInputState(
                                routineName = event.name
                            )
                        )
                    }
                }

                is RoutineCreateUiEvent.onAlarmChanged -> TODO()
                is RoutineCreateUiEvent.onColorChanged -> TODO()
                is RoutineCreateUiEvent.onDayOfWeekChanged -> TODO()
                is RoutineCreateUiEvent.onIconChanged -> TODO()
                is RoutineCreateUiEvent.onTimeChanged -> TODO()
            }
        }
    }
}