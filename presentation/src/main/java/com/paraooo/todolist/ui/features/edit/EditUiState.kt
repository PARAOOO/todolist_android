package com.paraooo.todolist.ui.features.edit

import com.paraooo.todolist.ui.components.TodoInputState

data class EditButtonState (
    val isEnabled : Boolean = true
)


data class EditUiState(
    val todoInputState : TodoInputState = TodoInputState(),
    val editButtonState: EditButtonState = EditButtonState()
)
