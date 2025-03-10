package com.paraooo.todolist.ui.features.create

import com.paraooo.todolist.ui.components.TimeInputState
import java.time.LocalDate

sealed class CreateUiEvent {

    data class onTodoNameInputChanged(val text : String) : CreateUiEvent()
    data class onDescriptionInputChanged(val text : String) : CreateUiEvent()
    data class onTimeInputChanged(val timeInputState: TimeInputState) : CreateUiEvent()
    data class onDateInputChanged(val date : LocalDate) : CreateUiEvent()
    data class onSelectedDateChanged(val date : LocalDate) : CreateUiEvent()
    data class onPeriodInputChanged(val startDate : LocalDate, val endDate : LocalDate) : CreateUiEvent()
    data object onCreateClicked : CreateUiEvent()

}