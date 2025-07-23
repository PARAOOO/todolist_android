package com.paraooo.data.mapper

import com.paraooo.data.dto.AlarmTypeDto
import com.paraooo.data.dto.TodoTemplateDto
import com.paraooo.data.dto.TodoTypeDto
import com.paraooo.data.local.entity.AlarmType
import com.paraooo.data.local.entity.TodoTemplate
import com.paraooo.data.local.entity.TodoType
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoType as DomainTodoType
import com.paraooo.domain.model.AlarmType as DomainAlarmType

fun TodoTemplate.toDto() : TodoTemplateDto {
    return TodoTemplateDto(
        id = id,
        title = title,
        description = description,
        hour = hour,
        minute = minute,
        type = type.toDto(),
        alarmType = alarmType.toDto(),
        isAlarmHasVibration = isAlarmHasVibration,
        isAlarmHasSound = isAlarmHasSound
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
        alarmType = alarmType.toEntity(),
        isAlarmHasVibration = isAlarmHasVibration,
        isAlarmHasSound = isAlarmHasSound
    )
}

fun TodoTemplateModel.toDto(): TodoTemplateDto {
    return TodoTemplateDto(
        id = id,
        title = title,
        description = description,
        hour = hour,
        minute = minute,
        type = when (type) {
            DomainTodoType.GENERAL -> TodoTypeDto.GENERAL
            DomainTodoType.PERIOD -> TodoTypeDto.PERIOD
            DomainTodoType.DAY_OF_WEEK -> TodoTypeDto.DAY_OF_WEEK
        },
        alarmType = when (alarmType) {
            DomainAlarmType.OFF -> AlarmTypeDto.OFF
            DomainAlarmType.NOTIFY -> AlarmTypeDto.NOTIFY
            DomainAlarmType.POPUP -> AlarmTypeDto.POPUP
        },
        isAlarmHasVibration = isAlarmHasVibration,
        isAlarmHasSound = isAlarmHasSound
    )
}

fun TodoTemplateDto.toModel(): TodoTemplateModel {
    return TodoTemplateModel(
        id = id,
        title = title,
        description = description,
        hour = hour,
        minute = minute,
        type = when (type) {
            TodoTypeDto.GENERAL -> DomainTodoType.GENERAL
            TodoTypeDto.PERIOD -> DomainTodoType.PERIOD
            TodoTypeDto.DAY_OF_WEEK -> DomainTodoType.DAY_OF_WEEK
        },
        alarmType = when (alarmType) {
            AlarmTypeDto.OFF -> DomainAlarmType.OFF
            AlarmTypeDto.NOTIFY -> DomainAlarmType.NOTIFY
            AlarmTypeDto.POPUP -> DomainAlarmType.POPUP
        },
        isAlarmHasVibration = isAlarmHasVibration,
        isAlarmHasSound = isAlarmHasSound
    )
}