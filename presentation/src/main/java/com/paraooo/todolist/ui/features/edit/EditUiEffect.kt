package com.paraooo.todolist.ui.features.edit

sealed class EditUiEffect {

    data class onUpdateTodoSuccess(val todoTitle : String) : EditUiEffect()

}