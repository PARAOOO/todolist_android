package com.paraooo.todolist.ui.features.routine_create

import com.paraooo.domain.model.RootRoutineModel
import com.paraooo.domain.model.RoutineAlarmType
import com.paraooo.domain.model.RoutineColorModel
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

data class RootRoutineInputState(
    val routineName : String = "",
    val time : LocalTime? = null,
    val dayOfWeek : List<DayOfWeek> = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    ),
    val alarmType : RoutineAlarmType = RoutineAlarmType.OFF,
    val color : RoutineColorModel = RoutineColorModel.COLOR_9977B4,
    val icon : Int = 1
)

data class SubRoutineInputState(
    val routineName : String = "",
    val time : Duration = Duration.ofMinutes(10),
    val icon : Int = 1,
    val alarmType : RoutineAlarmType = RoutineAlarmType.OFF
)

data class RoutineCreateUiState(
    val routine : RootRoutineModel? = null,
    val rootRoutineInput : RootRoutineInputState = RootRoutineInputState(),
    val subRoutineInput : SubRoutineInputState = SubRoutineInputState(),
)
