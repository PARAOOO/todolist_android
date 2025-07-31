package com.paraooo.todolist.ui.features.edit

import com.paraooo.domain.model.AlarmType
import com.paraooo.todolist.ui.components.TimeInputState
import com.paraooo.todolist.ui.components.TodoInputState

data class EditUiState(
    val todoInputState : TodoInputState = TodoInputState(),
    val isUpdateButtonUpdating : Boolean = false

) {
    val isEditButtonEnabled: Boolean
        get() {
            val isAlarmValid = todoInputState.alarmInputState.alarmType != AlarmType.OFF
            val isTimeValid = todoInputState.timeInputState is TimeInputState.Time
            val isTodoNameEmpty = todoInputState.todoNameInputState.content.isEmpty()

            return (((!isAlarmValid && !isTimeValid) || isTimeValid) && !isTodoNameEmpty) && !isUpdateButtonUpdating
        }
}
