package com.paraooo.todolist.ui.features.home

import java.time.LocalDate

sealed class HomeUiEvent {

    data class onDateChanged(val date : LocalDate) : HomeUiEvent()
    data class onTodoProgressChanged(val todoId : Int, val progress : Float) : HomeUiEvent()
    data class onIsSwipedChanged(val todoId : Int, val isSwiped : Boolean) : HomeUiEvent()
    data class onTodoEditClicked(val todoId : Int) : HomeUiEvent()
    data class onTodoDeleteClicked(val todoId : Int) : HomeUiEvent()
    data class onIsToggleOpenedChanged(val todoId : Int, val isToggleOpened : Boolean) : HomeUiEvent()

}