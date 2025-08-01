package com.paraooo.todolist.ui.features.alarm


sealed class AlarmUiEvent {

    data class onInit(val instanceId : Long) : AlarmUiEvent()

}