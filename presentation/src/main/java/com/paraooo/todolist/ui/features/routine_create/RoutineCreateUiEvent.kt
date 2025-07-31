package com.paraooo.todolist.ui.features.routine_create

import com.paraooo.domain.model.RoutineAlarmType
import com.paraooo.domain.model.RoutineColorModel
import com.paraooo.domain.model.RoutineIconModel
import com.paraooo.domain.model.TodoModel
import java.time.LocalDate
import java.time.LocalTime

sealed class RoutineCreateUiEvent {

    data class onNameChanged(val name : String) : RoutineCreateUiEvent()
    data class onTimeChanged(val time : LocalTime) : RoutineCreateUiEvent()
    data class onDayOfWeekChanged(val dayOfWeek : List<Int>) : RoutineCreateUiEvent()
    data class onAlarmChanged(val alarm : RoutineAlarmType) : RoutineCreateUiEvent()
    data class onColorChanged(val color : RoutineColorModel) : RoutineCreateUiEvent()
    data class onIconChanged(val icon : RoutineIconModel) : RoutineCreateUiEvent()
}