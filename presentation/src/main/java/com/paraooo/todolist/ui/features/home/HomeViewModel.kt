package com.paraooo.todolist.ui.features.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoReadRepository
import com.paraooo.domain.repository.TodoWriteRepository
import com.paraooo.domain.util.transferLocalDateToMillis
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    private val todoWriteRepository: TodoWriteRepository,
    private val todoReadRepository: TodoReadRepository,
    private val initialUiState : HomeUiState = HomeUiState()
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<HomeUiEffect>()
    val effectFlow = _effectChannel.receiveAsFlow()

    private var todoCollectJob: Job? = null

    var selectedTodo = mutableStateOf<TodoModel?>(null)

    private fun fetchTodoList(date: LocalDate) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                todoListState = _uiState.value.todoListState.copy(
                    isLoading = true
                )
            )

            try {
                todoCollectJob?.cancel()

                todoCollectJob = viewModelScope.launch {
                    todoReadRepository.getTodoByDate(transferLocalDateToMillis(date)).collect { todoList ->

                        _uiState.update { currentState ->
                            currentState.copy(
                                todoListState = currentState.todoListState.copy(
                                    todoList = todoList,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                        }
                    }
                }

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
                    todoWriteRepository.updateTodoProgress(event.todo.instanceId, event.progress)
                }
            }

            is HomeUiEvent.onTodoDeleteClicked -> {
                viewModelScope.launch{

                    todoWriteRepository.deleteTodoById(event.todo.instanceId)

                    _effectChannel.send(HomeUiEffect.onDeleteTodoSuccess)

                    fetchTodoList(_uiState.value.selectedDateState.date)
                }
            }

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