package com.paraooo.todolist.ui.features.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.usecase.dayofweek.PostDayOfWeekUseCase
import com.paraooo.domain.usecase.period.PostPeriodTodoUseCase
import com.paraooo.domain.usecase.todo.PostTodoUseCase
import com.paraooo.todolist.ui.components.AlarmInputState
import com.paraooo.todolist.ui.components.AlarmSettingInputState
import com.paraooo.todolist.ui.components.DateInputState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class CreateViewModel(
    private val postTodoUseCase: PostTodoUseCase,
    private val postPeriodTodoUseCase: PostPeriodTodoUseCase,
    private val postDayOfWeekUseCase: PostDayOfWeekUseCase,
    private val initialUiState : CreateUiState = CreateUiState()
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<CreateUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<CreateUiEffect>()
    val effectFlow = _effectChannel.receiveAsFlow()

    fun onEvent(event : CreateUiEvent) {
        viewModelScope.launch {
            when (event) {
                CreateUiEvent.onCreateClicked -> {

                    val baseTodoModel = TodoModel(
                        instanceId = 0,
                        templateId = 0,
                        title = _uiState.value.todoInputState.todoNameInputState.content,
                        description = _uiState.value.todoInputState.descriptionInputState.content,
                        date = LocalDate.now(),
                        time = _uiState.value.todoInputState.timeInputState,
                        alarmType = _uiState.value.todoInputState.alarmInputState.alarmType,
                        isAlarmHasVibration = if (_uiState.value.todoInputState.alarmInputState.alarmType == AlarmType.POPUP) _uiState.value.todoInputState.alarmSettingInputState.vibration else false,
                        isAlarmHasSound = if (_uiState.value.todoInputState.alarmInputState.alarmType == AlarmType.POPUP) _uiState.value.todoInputState.alarmSettingInputState.sound else false
                    )

                    _uiState.update { state ->
                        state.copy(
                            isCreateButtonUpdating = true
                        )
                    }

                    withContext(Dispatchers.IO){
                        when (_uiState.value.todoInputState.dateInputState) {
                            is DateInputState.Date -> {
                                postTodoUseCase(
                                    baseTodoModel.copy(
                                        date = (_uiState.value.todoInputState.dateInputState as DateInputState.Date).date
                                    )
                                )
                            }

                            is DateInputState.Period -> {
                                postPeriodTodoUseCase(
                                    baseTodoModel.copy(
                                        startDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).startDate,
                                        endDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).endDate
                                    ),
                                    startDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).startDate,
                                    endDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).endDate
                                )
                            }

                            is DateInputState.DayOfWeek -> {
                                postDayOfWeekUseCase(
                                    baseTodoModel.copy(),
                                    dayOfWeek = (_uiState.value.todoInputState.dateInputState as DateInputState.DayOfWeek).dayOfWeek
                                )
                            }
                        }
                    }

                    _effectChannel.send(CreateUiEffect.onPostTodoSuccess(todoTitle = _uiState.value.todoInputState.todoNameInputState.content))
                    _uiState.update { state ->
                        state.copy(
                            isCreateButtonUpdating = false
                        )
                    }
                }

                is CreateUiEvent.onDateInputChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            todoInputState = state.todoInputState.copy(
                                dateInputState = DateInputState.Date(event.date)
                            )
                        )
                    }
                }

                is CreateUiEvent.onDescriptionInputChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            todoInputState = state.todoInputState.copy(
                                descriptionInputState = _uiState.value.todoInputState.descriptionInputState.copy(
                                    content = event.text
                                )
                            )
                        )
                    }
                }

                is CreateUiEvent.onTimeInputChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            todoInputState = state.todoInputState.copy(
                                timeInputState = event.timeInputState
                            )
                        )
                    }
                }

                is CreateUiEvent.onTodoNameInputChanged -> {

                    _uiState.update { state ->
                        state.copy(
                            todoInputState = state.todoInputState.copy(
                                todoNameInputState = state.todoInputState.todoNameInputState.copy(
                                    content = event.text,
                                )
                            )
                        )
                    }
                }

                is CreateUiEvent.onSelectedDateChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            todoInputState = state.todoInputState.copy(
                                dateInputState = DateInputState.Date(event.date)
                            )
                        )
                    }
                }

                is CreateUiEvent.onPeriodInputChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            todoInputState = state.todoInputState.copy(
                                dateInputState = DateInputState.Period(
                                    event.startDate,
                                    event.endDate
                                )
                            )
                        )
                    }
                }

                is CreateUiEvent.onDayOfWeekInputChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            todoInputState = state.todoInputState.copy(
                                dateInputState = DateInputState.DayOfWeek(event.daysOfWeek.map { it.value })
                            )
                        )
                    }
                }

                is CreateUiEvent.onAlarmInputChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            todoInputState = state.todoInputState.copy(
                                alarmInputState = AlarmInputState(event.alarm)
                            )
                        )
                    }
                }

                is CreateUiEvent.onAlarmSettingInputChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            todoInputState = state.todoInputState.copy(
                                alarmSettingInputState = AlarmSettingInputState(
                                    vibration = event.vibration,
                                    sound = event.sound
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}