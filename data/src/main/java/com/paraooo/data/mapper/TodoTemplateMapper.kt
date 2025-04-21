package com.paraooo.data.mapper

import com.paraooo.data.dto.AlarmTypeDto
import com.paraooo.data.dto.TodoTemplateDto
import com.paraooo.data.dto.TodoTypeDto
import com.paraooo.data.local.entity.AlarmType
import com.paraooo.data.local.entity.TodoTemplate
import com.paraooo.data.local.entity.TodoType

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

fun TodoTemplate.toDto() : TodoTemplateDto {
    return TodoTemplateDto(
        id = id,
        title = title,
        description = description,
        hour = hour,
        minute = minute,
        type = type.toDto(),
        alarmType = alarmType.toDto()
    )
}

fun TodoTemplateDto.toEntity() : TodoTemplate {
    return TodoTemplate(
        id = id,
        title = title,
        description = description,
        hour = hour,
        minute = minute,
        type = type.toEntity(),
        alarmType = alarmType.toEntity()
    )
}