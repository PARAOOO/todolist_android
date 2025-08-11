package com.paraooo.todolist.ui.features.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.usecase.todo.DeleteTodoByIdUseCase
import com.paraooo.domain.usecase.todo.GetTodoByDateUseCase
import com.paraooo.domain.usecase.todo.ObserveTodosUseCase
import com.paraooo.domain.usecase.todo.SyncDayOfWeekTodoUseCase
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

class HomeViewModel(
    private val getTodoByDateUseCase: GetTodoByDateUseCase,
    private val updateTodoProgressUseCase: UpdateTodoProgressUseCase,
    private val deleteTodoByIdUseCase: DeleteTodoByIdUseCase,
    private val syncDayOfWeekTodoUseCase: SyncDayOfWeekTodoUseCase,
    private val observeTodosUseCase: ObserveTodosUseCase,
    private val initialUiState : HomeUiState = HomeUiState()
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<HomeUiEffect>()
    val effectFlow = _effectChannel.receiveAsFlow()

    private var todoCollectJob: Job? = null

    var selectedTodo = mutableStateOf<TodoModel?>(null)

    private fun fetchTodoList(date: LocalDate) {

        todoCollectJob?.cancel()

        todoCollectJob = viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    todoListState = state.todoListState.copy(
                        isLoading = true
                    )
                )
            }

            try {

                syncDayOfWeekTodoUseCase(transferLocalDateToMillis(date))

                observeTodosUseCase(transferLocalDateToMillis(date)).flowOn(Dispatchers.IO)
                    .collect { todoList ->
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
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        todoListState = state.todoListState.copy(
                            isLoading = false,
                            error = "${e.message}"
                        )
                    )
                }
            }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeUiEvent.onDateChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            selectedDateState = state.selectedDateState.copy(
                                date = event.date
                            )
                        )
                    }
                    fetchTodoList(event.date)
                }

                is HomeUiEvent.onDateChangedWithDialog -> {

                    val selectedDate = transferMillis2LocalDate(event.date)

                    _uiState.update { state ->
                        state.copy(
                            selectedDateState = state.selectedDateState.copy(
                                date = selectedDate
                            )
                        )
                    }

                    val selectedPage =
                        Int.MAX_VALUE / 2 - 3 + getDateDiff(LocalDate.now(), selectedDate)

                    _effectChannel.send(HomeUiEffect.onScrollToPage(selectedPage))
                }

                is HomeUiEvent.onIsSwipedChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            todoListState = state.todoListState.copy(
                                todoList = state.todoListState.todoList.map { todo ->
                                    if (todo.instanceId == event.todo.instanceId) {
                                        todo.copy(isSwiped = event.isSwiped) // isSwiped 값 변경
                                    } else {
                                        todo
                                    }
                                }
                            )
                        )
                    }
                }

                is HomeUiEvent.onTodoProgressChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            todoListState = state.todoListState.copy(
                                todoList = state.todoListState.todoList.map { todo ->
                                    if (todo.instanceId == event.todo.instanceId) {
                                        todo.copy(progressAngle = event.progress)
                                    } else {
                                        todo
                                    }
                                }
                            )
                        )
                    }
                    withContext(Dispatchers.IO) {
                        updateTodoProgressUseCase(event.todo.instanceId, event.progress)
                    }
                }

                is HomeUiEvent.onTodoDeleteClicked -> {

                    withContext(Dispatchers.IO) {
                        deleteTodoByIdUseCase(event.todo.instanceId)
                    }
                    _effectChannel.send(HomeUiEffect.onDeleteTodoSuccess)

                    fetchTodoList(_uiState.value.selectedDateState.date)


                }

                is HomeUiEvent.onIsToggleOpenedChanged -> {
                    _uiState.update { state ->
                        state.copy(
                            todoListState = state.todoListState.copy(
                                todoList = state.todoListState.todoList.map { todo ->
                                    if (todo.instanceId == event.todo.instanceId) {
                                        todo.copy(isToggleOpened = event.isToggleOpened)
                                    } else {
                                        todo
                                    }
                                }
                            )
                        )
                    }
                    Log.d(TAG, "onEvent: ${uiState.value.todoListState.todoList}")
                }


            }
        }
    }
}