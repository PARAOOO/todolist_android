package com.paraooo.domain.model

import java.time.LocalDate

data class Time(
    val hour: Int,
    val minute: Int
)

enum class AlarmType(val label : String) {
    OFF("Off"),
    NOTIFY("Notify"),
    POPUP("Pop-up")
}

data class TodoModel(
    val instanceId : Long,
    val title : String,
    val time : Time? = null,
    val date: LocalDate,
    val description : String? = null,
    val progressAngle : Float = 0F,
    val isSwiped : Boolean = false,
    val isToggleOpened : Boolean = false,
    val startDate : LocalDate? = null,
    val endDate : LocalDate? = null,
    val dayOfWeeks : List<Int>? = null,
    val alarmType : AlarmType,
    val isAlarmHasVibration : Boolean = false,
    val isAlarmHasSound : Boolean = false
)

data class TodoPeriodModel(
    val templateId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate
)

data class TodoPeriodWithTimeModel(
    val templateId: Long,
    val hour: Int?,
    val minute: Int?,
    val startDate: LocalDate,
    val endDate: LocalDate
)

enum class TodoType {
    GENERAL, PERIOD, DAY_OF_WEEK
}

data class TodoTemplateModel(
    val id: Long = 0,
    val title: String,
    val description: String,
    val hour: Int?,
    val minute: Int?,
    val type: TodoType,
    val alarmType: AlarmType,
    val isAlarmHasVibration: Boolean,
    val isAlarmHasSound: Boolean
)

data class TodoDayOfWeekModel(
    val id: Long = 0,
    val templateId: Long,
    val dayOfWeek: Int,
    val dayOfWeeks: List<Int>
)

data class TodoDayOfWeekWithTimeModel(
    val templateId: Long,
    val hour: Int?,
    val minute: Int?,
    val dayOfWeeks: List<Int>
)

//data class TodoModelDto(
//    val instanceId: Long,
//    val templateId: Long,
//    val title: String,
//    val description: String,
//    val date: LocalDate,
//    val hour: Int?,
//    val minute: Int?,
//    val progressAngle: Float,
//    val alarmType: AlarmType,
//    val startDate: LocalDate? = null,
//    val endDate: LocalDate? = null,
//    val dayOfWeeks: List<Int>? = null,
//    val isAlarmHasVibration: Boolean,
//    val isAlarmHasSound: Boolean
//)

data class TodoInstanceModel(
    val id: Long = 0,
    val templateId: Long,
    val date: LocalDate,
    val progressAngle: Float = 0F
)