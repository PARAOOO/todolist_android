package com.paraooo.todolist.ui.features.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.todolist.ui.features.create.CreateUiEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<HomeUiEffect>()
    val effectFlow = _effectChannel.receiveAsFlow()

//    var selectedTodoId : Int = 0
//    var selectedTodoTitle : String = ""

    var selectedTodo = mutableStateOf<TodoModel?>(null)

    private fun fetchTodoList(date: LocalDate) {
        viewModelScope.launch {
            // 로딩 상태로 변경
            _uiState.value = _uiState.value.copy(
                todoListState = _uiState.value.todoListState.copy(
                    isLoading = true
                )
            )

            try {
                val todoList = todoRepository.getTodoByDate(transferLocalDateToMillis(date))
                _uiState.value = _uiState.value.copy(
                    todoListState = _uiState.value.todoListState.copy(
                        todoList = todoList,
                        isLoading = false,
                        error = ""
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    todoListState = _uiState.value.todoListState.copy(
                        isLoading = false,
                        error = "${e.message}"
                    )
                )
            }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.onDateChanged -> {
                _uiState.value = _uiState.value.copy(
                    selectedDateState = _uiState.value.selectedDateState.copy(
                        date = event.date
                    )
                )
                fetchTodoList(event.date)
            }
            is HomeUiEvent.onIsSwipedChanged -> {

                Log.d(TAG, "HomeUiEvent.onIsSwipedChanged : eventTodoId : ${event.todo.instanceId}")
                Log.d(TAG, "HomeUiEvent.onIsSwipedChanged : todoList : ${_uiState.value.todoListState.todoList} ")

                _uiState.value = _uiState.value.copy(
                    todoListState = _uiState.value.todoListState.copy(
                        todoList = _uiState.value.todoListState.todoList.map { todo ->
                            if (todo.instanceId == event.todo.instanceId) {
                                todo.copy(isSwiped = event.isSwiped) // isSwiped 값 변경
                            } else {
                                todo
                            }
                        }
                    )
                )
            }
            is HomeUiEvent.onTodoProgressChanged -> {
                viewModelScope.launch{
                    _uiState.value = _uiState.value.copy(
                        todoListState = _uiState.value.todoListState.copy(
                            todoList = _uiState.value.todoListState.todoList.map { todo ->
                                if (todo.instanceId == event.todo.instanceId) {
                                    todo.copy(progressAngle = event.progress)
                                } else {
                                    todo
                                }
                            }
                        )
                    )
                    todoRepository.updateTodoProgress(event.todo.instanceId, event.progress)
                }
            }
            is HomeUiEvent.onTodoDeleteClicked -> {
                viewModelScope.launch{

//                    when(event.todo.groupId) {
//                        null -> {
//
//                            todoRepository.deleteTodoById(event.todo.id)
//                        }
//                        else -> {
////                            todoRepository.deletePeriodTodo(event.todo.groupId!!)
//                        }
//                    }

                    todoRepository.deleteTodoById(event.todo.instanceId)

                    _effectChannel.send(HomeUiEffect.onDeleteTodoSuccess)

                    fetchTodoList(_uiState.value.selectedDateState.date)
                }
            }

//            is HomeUiEvent.onTodoEditClicked -> {
//            }

            is HomeUiEvent.onIsToggleOpenedChanged -> {
                Log.d(TAG, "onEvent: ${uiState.value.todoListState.todoList}")
                Log.d(TAG, "onEvent: HomeUiEvent.onIsToggleOpenedChanged : ${event}")
                _uiState.value = _uiState.value.copy(
                    todoListState = _uiState.value.todoListState.copy(
                        todoList = _uiState.value.todoListState.todoList.map { todo ->
                            if (todo.instanceId == event.todo.instanceId) {
                                todo.copy(isToggleOpened = event.isToggleOpened)
                            } else {
                                todo
                            }
                        }
                    )
                )
                Log.d(TAG, "onEvent: ${uiState.value.todoListState.todoList}")
            }
        }
    }
}