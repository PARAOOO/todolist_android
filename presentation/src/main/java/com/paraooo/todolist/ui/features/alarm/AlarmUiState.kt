package com.paraooo.todolist.ui.features.alarm

import com.paraooo.data.local.entity.TodoTemplate

data class AlarmUiState(
    val instanceId : Long = 0L,
    val todoName : String = "",
    val vibration : Boolean = false,
    val sound : Boolean = false
)
