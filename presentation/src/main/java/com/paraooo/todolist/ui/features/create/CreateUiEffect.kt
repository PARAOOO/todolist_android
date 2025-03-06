package com.paraooo.todolist.ui.features.create

import java.time.LocalDate

sealed class CreateUiEffect {

    data class onPostTodoSuccess(val todoTitle : String) : CreateUiEffect()

}