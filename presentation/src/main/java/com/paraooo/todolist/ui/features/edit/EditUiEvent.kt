package com.paraooo.todolist.ui.features.edit

import com.paraooo.todolist.ui.components.TimeInputState
import com.paraooo.todolist.ui.features.create.CreateUiEvent
import java.time.LocalDate

sealed class EditUiEvent {

    data class onTodoNameInputChanged(val text : String) : EditUiEvent()
    data class onDescriptionInputChanged(val text : String) : EditUiEvent()
    data class onTimeInputChanged(val timeInputState: TimeInputState) : EditUiEvent()
    data class onDateInputChanged(val date : LocalDate) : EditUiEvent()
    data class onPeriodInputChanged(val startDate : LocalDate, val endDate : LocalDate) : EditUiEvent()
    data class onInit(val instanceId : Long) : EditUiEvent()
    data class onEditClicked(val instanceId: Long) : EditUiEvent()

}