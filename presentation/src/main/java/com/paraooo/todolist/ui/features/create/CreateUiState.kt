package com.paraooo.todolist.ui.features.create

import com.paraooo.todolist.ui.components.TodoInputState

data class CreateButtonState (
    val isEnabled : Boolean = false
)

data class CreateUiState (
    val todoInputState : TodoInputState = TodoInputState(),
    val createButtonState: CreateButtonState = CreateButtonState()
)
