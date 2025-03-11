package com.paraooo.todolist.ui.features.edit

import com.paraooo.todolist.ui.components.TodoInputState

data class EditButtonState (
    val isEnable : Boolean = true,
    val isValid: Boolean = false
)


data class EditUiState(
    val todoInputState : TodoInputState = TodoInputState(),
    val editButtonState: EditButtonState = EditButtonState()
)
