package com.paraooo.data.dto

data class TodoDayOfWeekDto(
    val id: Long,
    val templateId: Long,
    val dayOfWeek : Int,
    val dayOfWeeks: List<Int>,
)

data class TodoDayOfWeekWithTimeDto(
    val templateId : Long,
    val hour: Int?,
    val minute: Int?,
    val dayOfWeeks : List<Int>
)