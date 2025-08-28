package com.paraooo.domain.model

import java.time.LocalDate
import java.time.LocalTime

//data class Time(
//    val hour: Int,
//    val minute: Int
//)

enum class AlarmType(val label : String) {
    OFF("Off"),
    NOTIFY("Notify"),
    POPUP("Pop-up")
}

data class TodoModel(
    val instanceId : Long,
    val templateId : Long,
    val title : String,
    val time : LocalTime? = null,
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

data class TodoTemplateModel(
    val id: Long = 0,
    val title: String,
    val description: String,
    val hour: Int?, // null이면 시간 미지정,
    val minute: Int?,
    val type: TodoType, // GENERAL, PERIOD, DAY_OF_WEEK,
    val alarmType : AlarmType, // OFF, NOTIFY, POPUP
    val isAlarmHasVibration : Boolean,
    val isAlarmHasSound : Boolean
)

data class TodoInstanceModel(
    val id: Long = 0,
    val templateId: Long,
    val date: Long,
    val progressAngle: Float = 0F,
)

data class TodoPeriodModel(
    val templateId: Long,
    val startDate: Long,
    val endDate: Long
)

data class TodoDayOfWeekModel(
    val id: Long = 0,
    val templateId: Long,
    val dayOfWeeks: List<Int>,
    val dayOfWeek: Int
)

data class TodoPeriodWithTimeModel(
    val templateId : Long,
    val hour: Int?,
    val minute: Int?,
    val startDate: Long,
    val endDate: Long
)

data class TodoDayOfWeekWithTimeModel(
    val templateId : Long,
    val hour: Int?,
    val minute: Int?,
    val dayOfWeeks : List<Int>
)

enum class TodoType {
    GENERAL, PERIOD, DAY_OF_WEEK
}
