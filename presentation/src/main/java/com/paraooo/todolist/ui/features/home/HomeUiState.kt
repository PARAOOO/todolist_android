package com.paraooo.todolist.ui.features.home

import com.paraooo.domain.model.TodoModel
import java.time.LocalDate


data class TodoListState(
    val isLoading : Boolean = false,
    val todoList : List<TodoModel> = emptyList(),
    val error : String = ""
)

data class SelectedDateState(
    val date : LocalDate = LocalDate.now()
)

data class HomeUiState(
    val todoListState: TodoListState = TodoListState(),
    val selectedDateState : SelectedDateState = SelectedDateState()
)
