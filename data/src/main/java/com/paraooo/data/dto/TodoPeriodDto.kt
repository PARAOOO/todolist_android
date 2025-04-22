package com.paraooo.data.dto

data class TodoPeriodDto(
    val templateId: Long,
    val startDate: Long,
    val endDate: Long
)

data class TodoPeriodWithTimeDto(
    val templateId : Long,
    val hour: Int?,
    val minute: Int?,
    val startDate: Long,
    val endDate: Long
)