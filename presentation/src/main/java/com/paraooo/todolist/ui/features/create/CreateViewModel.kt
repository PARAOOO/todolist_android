package com.paraooo.todolist.ui.features.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.todolist.ui.components.DateInputState
import com.paraooo.todolist.ui.components.TimeInputState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class CreateViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateUiState())
    val uiState: StateFlow<CreateUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<CreateUiEffect>()
    val effectFlow = _effectChannel.receiveAsFlow()

    fun onEvent(event : CreateUiEvent) {
        when (event) {
            CreateUiEvent.onCreateClicked -> {
                _uiState.value = _uiState.value.copy(
                    createButtonState = _uiState.value.createButtonState.copy(
                        isEnable = false
                    )
                )
                viewModelScope.launch {

                    when(_uiState.value.todoInputState.dateInputState) {
                        is DateInputState.Date -> {
                            todoRepository.postTodo(
                                TodoModel(
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
                                    }
                                )
                            )
                        }
                        is DateInputState.Period -> {
//                            todoRepository.postPeriodTodo(
//                                todo = TodoModel(
//                                    id = 0,
//                                    title = _uiState.value.todoInputState.todoNameInputState.content,
//                                    description = _uiState.value.todoInputState.descriptionInputState.content,
//                                    date = LocalDate.now(),
//                                    time = when (_uiState.value.todoInputState.timeInputState) {
//                                        is TimeInputState.NoTime -> null
//                                        is TimeInputState.Time -> Time(
//                                            (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).hour,
//                                            (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).minute
//                                        )
//                                    }
//                                ),
//                                startDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).startDate,
//                                endDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).endDate
//                            )
                        }
                    }

                    _effectChannel.send(CreateUiEffect.onPostTodoSuccess(todoTitle = _uiState.value.todoInputState.todoNameInputState.content))
                    _uiState.value = _uiState.value.copy(
                        createButtonState = _uiState.value.createButtonState.copy(
                            isEnable = true
                        )
                    )
                }
            }
            is CreateUiEvent.onDateInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        dateInputState = DateInputState.Date(event.date)
                    )
                )
            }
            is CreateUiEvent.onDescriptionInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        descriptionInputState = _uiState.value.todoInputState.descriptionInputState.copy(
                            content = event.text
                        )
                    )
                )
            }
            is CreateUiEvent.onTimeInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        timeInputState = event.timeInputState
                    )
                )
            }
            is CreateUiEvent.onTodoNameInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    createButtonState = _uiState.value.createButtonState.copy(
                        isValid = event.text.isNotEmpty()
                    )
                )
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        todoNameInputState = _uiState.value.todoInputState.todoNameInputState.copy(
                            content = event.text,
                            isValid = event.text.isNotEmpty()
                        )
                    )
                )
            }

            is CreateUiEvent.onSelectedDateChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        dateInputState = DateInputState.Date(event.date)
                    )
                )
            }

            is CreateUiEvent.onPeriodInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        dateInputState = DateInputState.Period(event.startDate, event.endDate)
                    )
                )
            }

            is CreateUiEvent.onDayOfWeekInputChanged -> {

            }
        }
    }
}