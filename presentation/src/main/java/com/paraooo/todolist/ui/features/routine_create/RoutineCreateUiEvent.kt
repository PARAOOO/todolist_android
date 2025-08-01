package com.paraooo.todolist.ui.features.routine_create

import com.paraooo.domain.model.RoutineAlarmType
import com.paraooo.domain.model.RoutineColorModel
import com.paraooo.domain.model.TodoModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

sealed class RoutineCreateUiEvent {

    data class onRootNameChanged(val name : String) : RoutineCreateUiEvent()
    data class onRootTimeChanged(val time : LocalTime?) : RoutineCreateUiEvent()
    data class onRootDayOfWeekChanged(val dayOfWeek : List<DayOfWeek>) : RoutineCreateUiEvent()
    data class onRootAlarmChanged(val alarm : RoutineAlarmType) : RoutineCreateUiEvent()
    data class onRootColorChanged(val color : RoutineColorModel) : RoutineCreateUiEvent()
    data class onRootIconChanged(val icon : Int) : RoutineCreateUiEvent()
    data object onRootCreateClicked : RoutineCreateUiEvent()
}