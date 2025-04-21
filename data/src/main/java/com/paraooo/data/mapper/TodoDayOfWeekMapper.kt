package com.paraooo.data.mapper

import com.paraooo.data.dto.TodoDayOfWeekDto
import com.paraooo.data.dto.TodoDayOfWeekWithTimeDto
import com.paraooo.data.local.entity.TodoDayOfWeek
import com.paraooo.data.local.entity.TodoDayOfWeekWithTime

fun TodoDayOfWeek.toDto() : TodoDayOfWeekDto {
    return TodoDayOfWeekDto(
        id = id,
        templateId = templateId,
        dayOfWeek = dayOfWeek,
        dayOfWeeks = dayOfWeeks
    )
}

fun TodoDayOfWeekDto.toEntity() : TodoDayOfWeek {
    return TodoDayOfWeek(
        id = id,
        templateId = templateId,
        dayOfWeek = dayOfWeek,
        dayOfWeeks = dayOfWeeks
    )
}

fun TodoDayOfWeekWithTime.toDto() : TodoDayOfWeekWithTimeDto {
    return TodoDayOfWeekWithTimeDto(
        templateId = templateId,
        hour = hour,
        minute = minute,
        dayOfWeeks = dayOfWeeks
    )
}