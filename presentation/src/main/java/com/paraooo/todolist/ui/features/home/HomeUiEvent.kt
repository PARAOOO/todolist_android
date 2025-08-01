package com.paraooo.todolist.ui.features.home

import com.paraooo.domain.model.TodoModel
import java.time.LocalDate

sealed class HomeUiEvent {

    data class onDateChanged(val date : LocalDate) : HomeUiEvent()
    data class onDateChangedWithDialog(val date : Long?) : HomeUiEvent()
    data class onTodoProgressChanged(val todo : TodoModel, val progress : Float) : HomeUiEvent()
    data class onIsSwipedChanged(val todo : TodoModel, val isSwiped : Boolean) : HomeUiEvent()
    data class onTodoDeleteClicked(val todo : TodoModel) : HomeUiEvent()
    data class onIsToggleOpenedChanged(val todo : TodoModel, val isToggleOpened : Boolean) : HomeUiEvent()

}