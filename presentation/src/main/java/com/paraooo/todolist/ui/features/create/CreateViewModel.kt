package com.paraooo.todolist.ui.features.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoWriteRepository
import com.paraooo.todolist.ui.components.AlarmInputState
import com.paraooo.todolist.ui.components.AlarmSettingInputState
import com.paraooo.todolist.ui.components.DateInputState
import com.paraooo.todolist.ui.components.TimeInputState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class CreateViewModel(
    private val todoWriteRepository: TodoWriteRepository,
    private val initialUiState : CreateUiState = CreateUiState()
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<CreateUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<CreateUiEffect>()
    val effectFlow = _effectChannel.receiveAsFlow()

    private fun updateCreateButtonEnabled() {
        val isAlarmValid = _uiState.value.todoInputState.alarmInputState.alarmType != AlarmType.OFF
        val isTimeValid = _uiState.value.todoInputState.timeInputState != TimeInputState.NoTime
        val isTodoNameEmpty = _uiState.value.todoInputState.todoNameInputState.content.isEmpty()

        _uiState.update { state ->
            state.copy(
                createButtonState = state.createButtonState.copy(
                    isEnabled = ((!isAlarmValid && !isTimeValid) || isTimeValid) && !isTodoNameEmpty
                )
            )
        }
    }

    fun onEvent(event : CreateUiEvent) {
        when (event) {
            CreateUiEvent.onCreateClicked -> {

                val baseTodoModel = TodoModel(
                    instanceId = 0,
                    title = _uiState.value.todoInputState.todoNameInputState.content,
                    description = _uiState.value.todoInputState.descriptionInputState.content,
                    date = (_uiState.value.todoInputState.dateInputState as DateInputState.Date).date,
                    time = when (_uiState.value.todoInputState.timeInputState) {
                        is TimeInputState.NoTime -> null
                        is TimeInputState.Time -> Time(
                            (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).hour,
                            (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).minute
                        )
                    },
                    alarmType = _uiState.value.todoInputState.alarmInputState.alarmType,
                    isAlarmHasVibration = if(_uiState.value.todoInputState.alarmInputState.alarmType == AlarmType.POPUP) _uiState.value.todoInputState.alarmSettingInputState.vibration else false,
                    isAlarmHasSound = if(_uiState.value.todoInputState.alarmInputState.alarmType == AlarmType.POPUP) _uiState.value.todoInputState.alarmSettingInputState.sound else false
                )

                _uiState.update { state ->
                    state.copy(
                        createButtonState = state.createButtonState.copy(
                            isEnabled = false
                        )
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {

                    when(_uiState.value.todoInputState.dateInputState) {
                        is DateInputState.Date -> {
                            todoWriteRepository.postTodo(
                                baseTodoModel.copy()
                            )
                        }
                        is DateInputState.Period -> {
                            todoWriteRepository.postPeriodTodo(
                                baseTodoModel.copy(
                                    startDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).startDate,
                                    endDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).endDate
                                ),
                                startDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).startDate,
                                endDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).endDate
                            )
                        }

                        is DateInputState.DayOfWeek -> {
                            todoWriteRepository.postDayOfWeekTodo(
                                baseTodoModel.copy(),
                                dayOfWeek = (_uiState.value.todoInputState.dateInputState as DateInputState.DayOfWeek).dayOfWeek
                            )
                        }
                    }

                    _effectChannel.send(CreateUiEffect.onPostTodoSuccess(todoTitle = _uiState.value.todoInputState.todoNameInputState.content))
                    _uiState.update { state ->
                        state.copy(
                            createButtonState = state.createButtonState.copy(
                                isEnabled = true
                            )
                        )
                    }
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
                updateCreateButtonEnabled()
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

                updateCreateButtonEnabled()
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
                            dateInputState = DateInputState.Period(event.startDate, event.endDate)
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
                updateCreateButtonEnabled()
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