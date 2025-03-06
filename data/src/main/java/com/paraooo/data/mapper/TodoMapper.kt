package com.paraooo.data.mapper

import com.paraooo.data.dto.TodoDto
import com.paraooo.data.local.entity.TodoEntity
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate

fun TodoEntity.toDto() : TodoDto {
    return TodoDto(
        id, title, description, date, hour, minute, progressAngle
    )
}

fun TodoDto.toEntity() : TodoEntity {
    return TodoEntity(
        id = id,
        title = title,
        description = description,
        date = date,
        hour = hour,
        minute = minute,
        progressAngle = progressAngle
    )
}

fun TodoModel.toDto() : TodoDto {
    return TodoDto(
        id = id,
        title = title,
        description = description,
        date = transferLocalDateToMillis(date),
        progressAngle = progressAngle,
        hour = time?.hour,
        minute = time?.minute
    )
}

fun TodoDto.toModel() : TodoModel {
    return TodoModel(
        id = id,
        title = title,
        description = description,
        date = transferMillis2LocalDate(date),
        progressAngle = progressAngle,
        time = when(hour) {
            null -> null
            else -> Time(hour, minute!!)
        }
    )
}