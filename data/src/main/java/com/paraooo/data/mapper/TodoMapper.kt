package com.paraooo.data.mapper

import com.paraooo.data.dto.TodoDto
import com.paraooo.data.local.entity.AlarmType
//import com.paraooo.data.local.entity.TodoEntity
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate

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
        alarmType = alarmType.toModel()
    )
}

fun AlarmType.toModel() : com.paraooo.domain.model.AlarmType {
    return when(this) {
        AlarmType.OFF -> com.paraooo.domain.model.AlarmType.OFF
        AlarmType.NOTIFY -> com.paraooo.domain.model.AlarmType.NOTIFY
        AlarmType.POPUP -> com.paraooo.domain.model.AlarmType.POPUP
    }
}

fun com.paraooo.domain.model.AlarmType.toEntity() : AlarmType {
    return when (this) {
        com.paraooo.domain.model.AlarmType.OFF -> AlarmType.OFF
        com.paraooo.domain.model.AlarmType.NOTIFY -> AlarmType.NOTIFY
        com.paraooo.domain.model.AlarmType.POPUP -> AlarmType.POPUP
    }
}
//fun TodoEntity.toDto() : TodoDto {
//    return TodoDto(
//        id, title, description, date, hour, minute, progressAngle, groupId, startDate, endDate
//    )
//}
//
//fun TodoDto.toEntity() : TodoEntity {
//    return TodoEntity(
//        id = id,
//        title = title,
//        description = description,
//        date = date,
//        hour = hour,
//        minute = minute,
//        progressAngle = progressAngle,
//        groupId = groupId,
//        startDate = startDate,
//        endDate = endDate
//    )
//}
//
//fun TodoModel.toDto() : TodoDto {
//    return TodoDto(
//        id = id,
//        title = title,
//        description = description,
//        date = transferLocalDateToMillis(date),
//        progressAngle = progressAngle,
//        hour = time?.hour,
//        minute = time?.minute,
//        groupId = groupId,
//        startDate = startDate?.let { transferLocalDateToMillis(it) },
//        endDate = endDate?.let { transferLocalDateToMillis(it) }
//    )
//}
//
//fun TodoDto.toModel() : TodoModel {
//    return TodoModel(
//        id = id,
//        title = title,
//        description = description,
//        date = transferMillis2LocalDate(date),
//        progressAngle = progressAngle,
//        time = when(hour) {
//            null -> null
//            else -> Time(hour, minute!!)
//        },
//        groupId = groupId,
//        startDate = startDate?.let { transferMillis2LocalDate(it) },
//        endDate = endDate?.let { transferMillis2LocalDate(it) }
//
//    )
//}