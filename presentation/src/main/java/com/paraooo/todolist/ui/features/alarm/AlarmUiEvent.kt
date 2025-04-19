package com.paraooo.todolist.ui.features.alarm

import com.paraooo.domain.model.AlarmType
import com.paraooo.todolist.ui.components.TimeInputState
import java.time.DayOfWeek
import java.time.LocalDate

sealed class AlarmUiEvent {

    data class onInit(val instanceId : Long) : AlarmUiEvent()

}