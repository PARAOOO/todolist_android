package com.paraooo.todolist.ui.features.home

sealed class HomeUiEffect {

    data object onDeleteTodoSuccess : HomeUiEffect()
    data class onScrollToPage(val page: Int) : HomeUiEffect()

}