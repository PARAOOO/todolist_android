package com.paraooo.todolist.ui.features.routine_create

sealed class RoutineCreateUiEffect {

    data object onDeleteTodoSuccess : RoutineCreateUiEffect()
    data class onScrollToPage(val page: Int) : RoutineCreateUiEffect()

}