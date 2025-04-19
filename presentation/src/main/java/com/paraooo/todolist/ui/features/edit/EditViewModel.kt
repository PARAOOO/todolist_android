package com.paraooo.todolist.ui.features.edit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.todolist.ui.components.AlarmInputState
import com.paraooo.todolist.ui.components.AlarmSettingInputState
import com.paraooo.todolist.ui.components.DateInputState
import com.paraooo.todolist.ui.components.TimeInputState
import com.paraooo.todolist.ui.features.create.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class EditViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditUiState())
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<EditUiEffect>()
    val effectFlow = _effectChannel.receiveAsFlow()

    var selectedTodo = mutableStateOf<TodoModel?>(null)

    private fun updateCreateButtonEnabled() {
        val isAlarmValid = _uiState.value.todoInputState.alarmInputState.alarmType != AlarmType.OFF
        val isTimeValid = _uiState.value.todoInputState.timeInputState != TimeInputState.NoTime
        val isTodoNameEmpty = _uiState.value.todoInputState.todoNameInputState.content.isEmpty()

        _uiState.value = _uiState.value.copy(
            editButtonState = _uiState.value.editButtonState.copy(
                isEnabled = ((!isAlarmValid && !isTimeValid) || isTimeValid) && !isTodoNameEmpty
            )
        )
    }

    fun onEvent(event : EditUiEvent) {
        when(event){
            is EditUiEvent.onInit -> {
                fetchTodo(event.instanceId)
            }

            is EditUiEvent.onTodoNameInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        todoNameInputState = _uiState.value.todoInputState.todoNameInputState.copy(
                            content = event.text,
                        )
                    )
                )
                updateCreateButtonEnabled()
            }

            is EditUiEvent.onDateInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        dateInputState = DateInputState.Date(event.date)
                    )
                )
            }

            is EditUiEvent.onDescriptionInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        descriptionInputState = _uiState.value.todoInputState.descriptionInputState.copy(
                            content = event.text
                        )
                    )
                )
            }

            is EditUiEvent.onEditClicked -> {
                updateTodo(event.instanceId)
            }

            is EditUiEvent.onTimeInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        timeInputState = event.timeInputState
                    )
                )
                updateCreateButtonEnabled()
            }

            is EditUiEvent.onPeriodInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        dateInputState = DateInputState.Period(event.startDate, event.endDate)
                    )
                )
            }

            is EditUiEvent.onDayOfWeekInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        dateInputState = DateInputState.DayOfWeek(event.daysOfWeek.map { it.value })
                    )
                )
            }

            is EditUiEvent.onAlarmInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        alarmInputState = AlarmInputState(event.alarm)
                    )
                )
                updateCreateButtonEnabled()
            }

            is EditUiEvent.onAlarmSettingInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        alarmSettingInputState = AlarmSettingInputState(
                            vibration = event.vibration,
                            sound = event.sound
                        )
                    )
                )
            }
        }
    }

    private fun fetchTodo(instanceId: Long) {
        viewModelScope.launch {
            try {
                val todo = todoRepository.findTodoById(instanceId)
                Log.d(TAG, "fetchTodo: ${todo}")
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        todoNameInputState = _uiState.value.todoInputState.todoNameInputState.copy(
                            content = todo.title
                        ),
                        descriptionInputState = _uiState.value.todoInputState.descriptionInputState.copy(
                            content = todo.description ?: ""
                        ),
                        dateInputState = when {
                            todo.startDate != null -> DateInputState.Period(todo.startDate!!, todo.endDate!!)
                            (todo.dayOfWeeks != null ) -> DateInputState.DayOfWeek(todo.dayOfWeeks!!)
                            else -> DateInputState.Date(todo.date)
                        },
                        timeInputState = when (todo.time) {
                            null -> TimeInputState.NoTime
                            else -> TimeInputState.Time(todo.time!!.hour, todo.time!!.minute)
                        },
                        alarmInputState = _uiState.value.todoInputState.alarmInputState.copy(
                            alarmType = todo.alarmType
                        ),
                        alarmSettingInputState = _uiState.value.todoInputState.alarmSettingInputState.copy(
                            sound = todo.isAlarmHasSound,
                            vibration = todo.isAlarmHasVibration
                        )
                    )
                )
                selectedTodo.value = todo
                onEvent(EditUiEvent.onTodoNameInputChanged(todo.title))
            } catch(e : Exception) {
                Log.d(TAG, "fetchTodo: ${e}")
            }
        }
    }

    fun updateTodo(instanceId : Long) {

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(
                editButtonState = _uiState.value.editButtonState.copy(
                    isEnabled = false
                )
            )

            when {
                selectedTodo.value!!.startDate != null -> {
                    todoRepository.updatePeriodTodo(
                        TodoModel(
                            instanceId = instanceId,
                            title = uiState.value.todoInputState.todoNameInputState.content,
                            description = uiState.value.todoInputState.descriptionInputState.content,
                            date = when (uiState.value.todoInputState.dateInputState) {
                                is DateInputState.Date -> (uiState.value.todoInputState.dateInputState as DateInputState.Date).date
                                is DateInputState.Period -> LocalDate.now()
                                is DateInputState.DayOfWeek -> LocalDate.now()
                            },
                            time = when (_uiState.value.todoInputState.timeInputState) {
                                is TimeInputState.NoTime -> null
                                is TimeInputState.Time -> Time(
                                    (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).hour,
                                    (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).minute
                                )
                            },
                            alarmType = _uiState.value.todoInputState.alarmInputState.alarmType,

                            isAlarmHasVibration = if(_uiState.value.todoInputState.alarmInputState.alarmType == AlarmType.POPUP) _uiState.value.todoInputState.alarmSettingInputState.vibration else false,
                            isAlarmHasSound = if(_uiState.value.todoInputState.alarmInputState.alarmType == AlarmType.POPUP) _uiState.value.todoInputState.alarmSettingInputState.sound else false,
                            progressAngle = when (selectedTodo.value) {
                                null -> 0f
                                else -> selectedTodo.value!!.progressAngle
                            },
                            startDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).startDate,
                            endDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).endDate
                        )
                    )
                }
                selectedTodo.value!!.dayOfWeeks != null -> {
                   todoRepository.updateDayOfWeekTodo(
                       TodoModel(
                           instanceId = instanceId,
                           title = uiState.value.todoInputState.todoNameInputState.content,
                           description = uiState.value.todoInputState.descriptionInputState.content,
                           date = when (uiState.value.todoInputState.dateInputState) {
                               is DateInputState.Date -> (uiState.value.todoInputState.dateInputState as DateInputState.Date).date
                               is DateInputState.Period -> LocalDate.now()
                               is DateInputState.DayOfWeek -> LocalDate.now()
                           },
                           time = when (_uiState.value.todoInputState.timeInputState) {
                               is TimeInputState.NoTime -> null
                               is TimeInputState.Time -> Time(
                                   (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).hour,
                                   (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).minute
                               )
                           },
                           alarmType = _uiState.value.todoInputState.alarmInputState.alarmType,
                           isAlarmHasVibration = if(_uiState.value.todoInputState.alarmInputState.alarmType == AlarmType.POPUP) _uiState.value.todoInputState.alarmSettingInputState.vibration else false,
                           isAlarmHasSound = if(_uiState.value.todoInputState.alarmInputState.alarmType == AlarmType.POPUP) _uiState.value.todoInputState.alarmSettingInputState.sound else false,
                           progressAngle = when (selectedTodo.value) {
                               null -> 0f
                               else -> selectedTodo.value!!.progressAngle
                           },
                           dayOfWeeks = (_uiState.value.todoInputState.dateInputState as DateInputState.DayOfWeek).dayOfWeek
                       )
                   )
                }
                else -> {
                    todoRepository.updateTodo(
                        TodoModel(
                            instanceId = instanceId,
                            title = uiState.value.todoInputState.todoNameInputState.content,
                            description = uiState.value.todoInputState.descriptionInputState.content,
                            date = when (uiState.value.todoInputState.dateInputState) {
                                is DateInputState.Date -> (uiState.value.todoInputState.dateInputState as DateInputState.Date).date
                                is DateInputState.Period -> LocalDate.now()
                                is DateInputState.DayOfWeek -> LocalDate.now()
                            },
                            time = when (_uiState.value.todoInputState.timeInputState) {
                                is TimeInputState.NoTime -> null
                                is TimeInputState.Time -> Time(
                                    (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).hour,
                                    (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).minute
                                )
                            },
                            alarmType = _uiState.value.todoInputState.alarmInputState.alarmType,
                            isAlarmHasVibration = if(_uiState.value.todoInputState.alarmInputState.alarmType == AlarmType.POPUP) _uiState.value.todoInputState.alarmSettingInputState.vibration else false,
                            isAlarmHasSound = if(_uiState.value.todoInputState.alarmInputState.alarmType == AlarmType.POPUP) _uiState.value.todoInputState.alarmSettingInputState.sound else false,
                            progressAngle = when (selectedTodo.value) {
                                null -> 0f
                                else -> selectedTodo.value!!.progressAngle
                            }
                        )
                    )
                }
            }

            _effectChannel.send(EditUiEffect.onUpdateTodoSuccess(todoTitle = _uiState.value.todoInputState.todoNameInputState.content))
            _uiState.value = _uiState.value.copy(
                editButtonState = _uiState.value.editButtonState.copy(
                    isEnabled = true
                )
            )
        }
    }
}