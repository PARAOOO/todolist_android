package com.paraooo.todolist.ui.features.edit

import com.paraooo.todolist.ui.components.TimeInputState
import java.time.LocalDate

sealed class EditUiEvent {

    data class onTodoNameInputChanged(val text : String) : EditUiEvent()
    data class onDescriptionInputChanged(val text : String) : EditUiEvent()
    data class onTimeInputChanged(val timeInputState: TimeInputState) : EditUiEvent()
    data class onDateInputChanged(val date : LocalDate) : EditUiEvent()
    data class onInit(val todoId : Int, val selectedDate : LocalDate) : EditUiEvent()
    data class onEditClicked(val todoId : Int) : EditUiEvent()

}