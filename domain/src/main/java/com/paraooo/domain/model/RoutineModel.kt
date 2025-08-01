package com.paraooo.domain.model

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime

enum class RoutineAlarmType(val label : String) {
    OFF("Off"),
    VIBRATION("Vibration"),
    SOUND("Sound")
}

enum class RoutineColorModel(val color : Long) {
    COLOR_DD86B9(0xFFDD86B9),
    COLOR_F497AA(0xFFF497AA),
    COLOR_F9B48A(0xFFF9B48A),
    COLOR_FDCD79(0xFFFDCD79),
    COLOR_C7BCA1(0xFFC7BCA1),
    COLOR_9977B4(0xFF9977B4),
    COLOR_71ABDD(0xFF71ABDD),
    COLOR_6BCADE(0xFF6BCADE),
    COLOR_82CCB3(0xFF82CCB3),
    COLOR_B6D884(0xFFB6D884)
}

data class RootRoutineModel(
    val id : Int,
    val name : String,
    val startTime : LocalTime? = null,
    val dayOfWeek : List<DayOfWeek>,
    val alarm : RoutineAlarmType,
    val color : RoutineColorModel,
    val icon : Int,
    val subRoutines : List<SubRoutineModel>
)

data class SubRoutineModel(
    val id : Int,
    val name : String,
    val time : Duration,
    val icon : Int,
    val alarm : RoutineAlarmType
)

