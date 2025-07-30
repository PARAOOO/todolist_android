package com.paraooo.domain.model

sealed class RoutineAlarmType {
    data object Off : RoutineAlarmType()
    data object Vibration : RoutineAlarmType()
    data object Sound : RoutineAlarmType()
}

enum class RoutineColorModel(val color : Long) {
    RED(0xFFFF0000),
    GREEN(0xFF00FF00),
    BLUE(0xFF0000FF)
}

enum class RoutineIconModel(val icon : String) {
    ICON1("icon1"),
    ICON2 ("icon2"),
}

data class RootRoutineModel(
    val id : Int,
    val name : String,
    val startTime : Long,
    val dayOfWeek : List<Int>,
    val alarm : RoutineAlarmType,
    val color : RoutineColorModel,
    val icon : RoutineIconModel,
    val subRoutines : List<SubRoutineModel>
)

data class SubRoutineModel(
    val id : Int,
    val name : String,
    val time : Long,
    val icon : RoutineIconModel,
    val alarm : RoutineAlarmType
)