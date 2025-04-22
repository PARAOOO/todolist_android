package com.paraooo.data.mapper

import com.paraooo.data.dto.AlarmTypeDto
import com.paraooo.data.dto.TodoTemplateDto
import com.paraooo.data.dto.TodoTypeDto
import com.paraooo.data.local.entity.AlarmType
import com.paraooo.data.local.entity.TodoTemplate
import com.paraooo.data.local.entity.TodoType



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