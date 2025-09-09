package com.paraooo.todolist.ui.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.data.platform.logout.LogoutEffectProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed class MainUiEvent {
    object NavigateToLogin : MainUiEvent()
}

class MainViewModel(
    private val logoutEffectProvider: LogoutEffectProvider
) : ViewModel() {

    private val _navEventChannel = Channel<MainUiEvent>()
    val navEventFlow = _navEventChannel.receiveAsFlow()

    init {
        observeLogoutEffects()
    }

    private fun observeLogoutEffects() {
        viewModelScope.launch {
            logoutEffectProvider.logoutEffectFlow.collect {
                _navEventChannel.send(MainUiEvent.NavigateToLogin)
            }
        }
    }
}