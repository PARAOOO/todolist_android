package com.paraooo.todolist.ui.features.edit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.todolist.ui.components.DateInputState
import com.paraooo.todolist.ui.components.TimeInputState
import com.paraooo.todolist.ui.features.create.TAG
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

    fun onEvent(event : EditUiEvent) {
        when(event){
            is EditUiEvent.onInit -> {
                fetchTodo(event.todoId)
            }
            is EditUiEvent.onTodoNameInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    editButtonState = _uiState.value.editButtonState.copy(
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
                updateTodo(event.todoId)
            }
            is EditUiEvent.onTimeInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        timeInputState = event.timeInputState
                    )
                )
            }

            is EditUiEvent.onPeriodInputChanged -> {
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        dateInputState = DateInputState.Period(event.startDate, event.endDate)
                    )
                )
            }
        }
    }

    fun fetchTodo(todoId : Int) {
        viewModelScope.launch {
            try {
                val todo = todoRepository.findTodoById(todoId)
                _uiState.value = _uiState.value.copy(
                    todoInputState = _uiState.value.todoInputState.copy(
                        todoNameInputState = _uiState.value.todoInputState.todoNameInputState.copy(
                            content = todo.title
                        ),
                        descriptionInputState = _uiState.value.todoInputState.descriptionInputState.copy(
                            content = todo.description ?: ""
                        ),
                        dateInputState = when(todo.groupId) {
                            null -> DateInputState.Date(todo.date)
                            else -> DateInputState.Period(todo.startDate!!, todo.endDate!!)
                        },
                        timeInputState = when (todo.time) {
                            null -> TimeInputState.NoTime
                            else -> TimeInputState.Time(todo.time!!.hour, todo.time!!.minute)
                        }
                    )
                )
                selectedTodo.value = todo
                onEvent(EditUiEvent.onTodoNameInputChanged(todo.title))
            } catch(e : Exception) {
                Log.d(TAG, "fetchTodo: ${e}")
            }
        }
    }

    fun updateTodo(todoId : Int) {
        _uiState.value = _uiState.value.copy(
            editButtonState = _uiState.value.editButtonState.copy(
                isEnable = false
            )
        )
        viewModelScope.launch {
            when(selectedTodo.value!!.groupId) {
                null -> {
                    todoRepository.updateTodo(
                        TodoModel(
                            id = todoId,
                            title = uiState.value.todoInputState.todoNameInputState.content,
                            description = uiState.value.todoInputState.descriptionInputState.content,
                            date = when (uiState.value.todoInputState.dateInputState) {
                                is DateInputState.Date -> (uiState.value.todoInputState.dateInputState as DateInputState.Date).date
                                is DateInputState.Period -> (uiState.value.todoInputState.dateInputState as DateInputState.Period).startDate
                            },
                            time = when (_uiState.value.todoInputState.timeInputState) {
                                is TimeInputState.NoTime -> null
                                is TimeInputState.Time -> Time(
                                    (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).hour,
                                    (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).minute
                                )
                            },
                            progressAngle = when (selectedTodo.value) {
                                null -> 0f
                                else -> selectedTodo.value!!.progressAngle
                            }
                        )
                    )
                }
                else -> {
                    todoRepository.updatePeriodTodo(
                        TodoModel(
                            id = todoId,
                            title = uiState.value.todoInputState.todoNameInputState.content,
                            description = uiState.value.todoInputState.descriptionInputState.content,
                            date = LocalDate.now(),
                            time = when (_uiState.value.todoInputState.timeInputState) {
                                is TimeInputState.NoTime -> null
                                is TimeInputState.Time -> Time(
                                    (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).hour,
                                    (_uiState.value.todoInputState.timeInputState as TimeInputState.Time).minute
                                )
                            },
                            groupId = selectedTodo.value!!.groupId,
                            startDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).startDate,
                            endDate = (_uiState.value.todoInputState.dateInputState as DateInputState.Period).endDate
                        )
                    )
                }
            }
            _effectChannel.send(EditUiEffect.onUpdateTodoSuccess(todoTitle = _uiState.value.todoInputState.todoNameInputState.content))
            _uiState.value = _uiState.value.copy(
                editButtonState = _uiState.value.editButtonState.copy(
                    isEnable = true
                )
            )
        }
    }
}