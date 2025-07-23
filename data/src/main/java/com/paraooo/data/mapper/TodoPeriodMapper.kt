package com.paraooo.data.mapper

import com.paraooo.data.dto.TodoPeriodDto
import com.paraooo.data.dto.TodoPeriodWithTimeDto
import com.paraooo.data.local.entity.TodoPeriod
import com.paraooo.data.local.entity.TodoPeriodWithTime
import com.paraooo.domain.model.TodoPeriodModel
import java.time.LocalDate

fun TodoPeriod.toDto() : TodoPeriodDto {
    return TodoPeriodDto(
        templateId = templateId,
        startDate = startDate,
        endDate = endDate
    )
}

fun TodoPeriodDto.toEntity() : TodoPeriod {
    return TodoPeriod(
        templateId = templateId,
        startDate = startDate,
        endDate = endDate
    )
}

fun TodoPeriodWithTime.toDto() : TodoPeriodWithTimeDto {
    return TodoPeriodWithTimeDto(
        templateId = templateId,
        hour = hour,
        minute = minute,
        startDate = startDate,
        endDate = endDate
    )
}

fun TodoPeriodModel.toDto(): TodoPeriodDto {
    return TodoPeriodDto(
        templateId = templateId,
        startDate = startDate.toEpochDay(),
        endDate = endDate.toEpochDay()
    )
}

fun TodoPeriodDto.toModel(): TodoPeriodModel {
    return TodoPeriodModel(
        templateId = templateId,
        startDate = LocalDate.ofEpochDay(startDate),
        endDate = LocalDate.ofEpochDay(endDate)
    )
}