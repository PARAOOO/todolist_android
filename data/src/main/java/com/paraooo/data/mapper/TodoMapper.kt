package com.paraooo.data.mapper

import com.paraooo.data.dto.AlarmTypeDto
import com.paraooo.data.dto.TodoDto
import com.paraooo.data.dto.TodoTypeDto
import com.paraooo.data.local.entity.AlarmType
import com.paraooo.data.local.entity.TodoEntity
import com.paraooo.data.local.entity.TodoType
//import com.paraooo.data.local.entity.TodoEntity
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate

fun TodoDto.toEntity() : TodoEntity {
    return TodoEntity(
        instanceId = instanceId,
        templateId = templateId,
        title = title,
        description = description,
        date = date,
        hour = hour,
        minute = minute,
        progressAngle = progressAngle,
        alarmType = alarmType.toEntity(),
        startDate = startDate,
        endDate = endDate,
        dayOfWeeks = dayOfWeeks
    )
}

fun TodoEntity.toDto() : TodoDto {
    return TodoDto(
        instanceId = instanceId,
        templateId = templateId,
        title = title,
        description = description,
        date = date,
        hour = hour,
        minute = minute,
        progressAngle = progressAngle,
        alarmType = alarmType.toDto(),
        startDate = startDate,
        endDate = endDate,
        dayOfWeeks = dayOfWeeks
    )
}

fun TodoDto.toModel() : TodoModel {
    return TodoModel(
        instanceId = instanceId,
        title = title,
        description = description,
        date = transferMillis2LocalDate(date),
        progressAngle = progressAngle,
        time = when(hour) {
            null -> null
            else -> Time(hour, minute!!)
        },
        alarmType = alarmType.toModel(),
        startDate = startDate?.let { transferMillis2LocalDate(it) },
        endDate = endDate?.let { transferMillis2LocalDate(it) },
        dayOfWeeks = dayOfWeeks
    )
}

fun TodoType.toDto() : TodoTypeDto {
    return when(this) {
        TodoType.GENERAL -> TodoTypeDto.GENERAL
        TodoType.PERIOD -> TodoTypeDto.PERIOD
        TodoType.DAY_OF_WEEK -> TodoTypeDto.DAY_OF_WEEK
    }
}

fun TodoTypeDto.toEntity() : TodoType {
    return when(this) {
        TodoTypeDto.GENERAL -> TodoType.GENERAL
        TodoTypeDto.PERIOD -> TodoType.PERIOD
        TodoTypeDto.DAY_OF_WEEK -> TodoType.DAY_OF_WEEK
    }
}

fun AlarmType.toDto() : AlarmTypeDto {
    return when(this) {
        AlarmType.OFF -> AlarmTypeDto.OFF
        AlarmType.NOTIFY -> AlarmTypeDto.NOTIFY
        AlarmType.POPUP -> AlarmTypeDto.POPUP
    }
}

fun AlarmTypeDto.toEntity() : AlarmType {
    return when(this) {
        AlarmTypeDto.OFF -> AlarmType.OFF
        AlarmTypeDto.NOTIFY -> AlarmType.NOTIFY
        AlarmTypeDto.POPUP -> AlarmType.POPUP
    }
}

fun AlarmTypeDto.toModel() : com.paraooo.domain.model.AlarmType {
    return when(this) {
        AlarmTypeDto.OFF -> com.paraooo.domain.model.AlarmType.OFF
        AlarmTypeDto.NOTIFY -> com.paraooo.domain.model.AlarmType.NOTIFY
        AlarmTypeDto.POPUP -> com.paraooo.domain.model.AlarmType.POPUP
    }
}

fun com.paraooo.domain.model.AlarmType.toDto() : AlarmTypeDto {
    return when(this) {
        com.paraooo.domain.model.AlarmType.OFF -> AlarmTypeDto.OFF
        com.paraooo.domain.model.AlarmType.NOTIFY -> AlarmTypeDto.NOTIFY
        com.paraooo.domain.model.AlarmType.POPUP -> AlarmTypeDto.POPUP
    }
}
