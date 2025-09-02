package com.paraooo.todolist.ui.features.alarm

import java.util.UUID

data class AlarmUiState(
    val instanceId : UUID = UUID.randomUUID(),
    val todoName : String = "",
    val vibration : Boolean = false,
    val sound : Boolean = false
)
