package com.paraooo.data.mapper

import com.paraooo.data.dto.TodoInstanceDto
import com.paraooo.data.local.entity.TodoInstance
import com.paraooo.domain.model.TodoInstanceModel
import java.time.LocalDate

fun TodoInstance.toDto() : TodoInstanceDto {
    return TodoInstanceDto(
        id = id,
        templateId = templateId,
        date = date,
        progressAngle = progressAngle
    )
}

fun TodoInstanceDto.toEntity() : TodoInstance {
    return TodoInstance(
        id = id,
        templateId = templateId,
        date = date,
        progressAngle = progressAngle
    )
}

fun TodoInstanceModel.toDto(): TodoInstanceDto {
    return TodoInstanceDto(
        id = id,
        templateId = templateId,
        date = date.toEpochDay(), // LocalDate to Long
        progressAngle = progressAngle
    )
}

fun TodoInstanceDto.toModel(): TodoInstanceModel {
    return TodoInstanceModel(
        id = id,
        templateId = templateId,
        date = LocalDate.ofEpochDay(date), // Long to LocalDate
        progressAngle = progressAngle
    )
}