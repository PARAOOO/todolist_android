package com.paraooo.todolist.ui.base

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BaseViewModel() {

    private val _todoListState = MutableStateFlow<UiState>(UiState.Loading)
    val todoListState = _todoListState.asStateFlow()




}