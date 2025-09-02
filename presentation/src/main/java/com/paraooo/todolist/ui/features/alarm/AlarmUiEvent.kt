package com.paraooo.todolist.ui.features.alarm

import java.util.UUID


sealed class AlarmUiEvent {

    data class onInit(val instanceId : UUID) : AlarmUiEvent()

}