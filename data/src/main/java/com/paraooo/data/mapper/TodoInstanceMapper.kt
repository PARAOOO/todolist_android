package com.paraooo.data.mapper

import com.paraooo.data.dto.TodoInstanceDto
import com.paraooo.data.local.entity.TodoInstance

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