package com.paraooo.todolist.ui.features.create

import com.paraooo.todolist.ui.components.TodoUiState
import java.time.LocalDate
//
//data class TodoNameInputState (
//    val content : String = "",
//    val isValid : Boolean = false
//)
//
//data class DescriptionInputState (
//    val content : String = ""
//)
//
//sealed class TimeInputState {
//    data class Time(val hour : Int, val minute : Int) : TimeInputState()
//    data object NoTime : TimeInputState()
//}
//
//data class DateInputState (
//    val date : LocalDate = LocalDate.now()
//)

data class CreateButtonState (
    val isEnable : Boolean = true,
    val isValid: Boolean = false
)

data class CreateUiState (
//    val todoNameInputState : TodoNameInputState = TodoNameInputState(),
//    val descriptionInputState : DescriptionInputState = DescriptionInputState(),
//    val timeInputState: TimeInputState = TimeInputState.NoTime,
//    val dateInputState : DateInputState = DateInputState(),
    val todoInputState : TodoUiState = TodoUiState(),
    val createButtonState: CreateButtonState = CreateButtonState()
)
