package com.paraooo.domain.model

import java.time.Duration
import java.time.LocalTime

enum class RoutineAlarmType(val label : String) {
    OFF("Off"),
    VIBRATION("Vibration"),
    SOUND("Sound")
}

enum class RoutineColorModel(val color : Long) {
    RED(0xFFFF0000),
    GREEN(0xFF00FF00),
    BLUE(0xFF0000FF)
}

enum class RoutineIconModel(val icon : String) {
    ICON1("1"),
    ICON2 ("2"),
    ICON3 ("3")
}

data class RootRoutineModel(
    val id : Int,
    val name : String,
    val startTime : LocalTime,
    val dayOfWeek : List<Int>,
    val alarm : RoutineAlarmType,
    val color : RoutineColorModel,
    val icon : RoutineIconModel,
    val subRoutines : List<SubRoutineModel>
)

data class SubRoutineModel(
    val id : Int,
    val name : String,
    val time : Duration,
    val icon : RoutineIconModel,
    val alarm : RoutineAlarmType
)

