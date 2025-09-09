package com.paraooo.todolist.ui.features.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class SettingUiEvent(){
    object onLogoutClicked : SettingUiEvent()
}

class SettingViewModel(
    private val authRepository: AuthRepository,
): ViewModel() {


    fun onEvent(event : SettingUiEvent) {
        viewModelScope.launch{
            when (event) {
                is SettingUiEvent.onLogoutClicked -> {
                    withContext(Dispatchers.IO){
                        authRepository.logout()
                    }
                }

            }
        }
    }

}