package com.paraooo.todolist.ui.features.routine_create

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.RootRoutineModel
import com.paraooo.domain.model.RoutineAlarmType
import com.paraooo.domain.model.RoutineColorModel
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

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
                is RoutineCreateUiEvent.onRootNameChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            rootRoutineInput = state.rootRoutineInput.copy(
                                routineName = event.name
                            )
                        )
                    }
                }

                is RoutineCreateUiEvent.onRootAlarmChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            rootRoutineInput = state.rootRoutineInput.copy(
                                alarmType = event.alarm
                            )
                        )
                    }
                }
                is RoutineCreateUiEvent.onRootColorChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            rootRoutineInput = state.rootRoutineInput.copy(
                                color = event.color
                            )
                        )
                    }
                }
                is RoutineCreateUiEvent.onRootDayOfWeekChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            rootRoutineInput = state.rootRoutineInput.copy(
                                dayOfWeek = event.dayOfWeek
                            )
                        )
                    }
                }
                is RoutineCreateUiEvent.onRootIconChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            rootRoutineInput = state.rootRoutineInput.copy(
                                icon = event.icon
                            )
                        )
                    }
                }
                is RoutineCreateUiEvent.onRootTimeChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            rootRoutineInput = state.rootRoutineInput.copy(
                                time = event.time
                            )
                        )
                    }
                }

                RoutineCreateUiEvent.onRootCreateClicked -> {
                    _uiState.update { state ->
                        state.copy(
                            routine = RootRoutineModel(
                                id = 1,
                                name = state.rootRoutineInput.routineName,
                                startTime = state.rootRoutineInput.time,
                                dayOfWeek = state.rootRoutineInput.dayOfWeek,
                                alarm = state.rootRoutineInput.alarmType,
                                color = state.rootRoutineInput.color,
                                icon = state.rootRoutineInput.icon,
                                subRoutines = if(state.routine == null) listOf() else state.routine.subRoutines
                            )
                        )
                    }
                }

                is RoutineCreateUiEvent.onSubAlarmChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            subRoutineInput = state.subRoutineInput.copy(
                                alarmType = event.alarm
                            )
                        )
                    }
                }

                RoutineCreateUiEvent.onSubCreateClicked -> {
                }

                is RoutineCreateUiEvent.onSubIconChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            subRoutineInput = state.subRoutineInput.copy(
                                icon = event.icon
                            )
                        )
                    }
                }

                is RoutineCreateUiEvent.onSubNameChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            subRoutineInput = state.subRoutineInput.copy(
                                routineName = event.name
                            )
                        )
                    }
                }

                is RoutineCreateUiEvent.onSubTimeChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            subRoutineInput = state.subRoutineInput.copy(
                                time = event.time
                            )
                        )
                    }
                }

            }
        }
    }
}