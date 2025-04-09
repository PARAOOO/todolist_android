package com.paraooo.domain.model

import java.time.LocalDate

data class Time(
    val hour: Int,
    val minute: Int
)

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
    val dayOfWeeks : List<Int>? = null
)