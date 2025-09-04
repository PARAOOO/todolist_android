package com.paraooo.data.mapper

import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.InstanceTodoSyncModel
import com.paraooo.domain.model.TemplateTodoSyncModel
import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.model.TodoDayOfWeekWithTimeModel
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoPeriodModel
import com.paraooo.domain.model.TodoPeriodWithTimeModel
import com.paraooo.domain.model.TodoSyncModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.util.convertDateStringToMillis
import com.paraooo.domain.util.convertMillisToDateString
import com.paraooo.domain.util.transferMillis2LocalDate
import com.paraooo.local.entity.AlarmTypeEntity
import com.paraooo.local.entity.TodoDayOfWeek
import com.paraooo.local.entity.TodoDayOfWeekWithTime
import com.paraooo.local.entity.TodoEntity
import com.paraooo.local.entity.TodoInstance
import com.paraooo.local.entity.TodoPeriod
import com.paraooo.local.entity.TodoPeriodWithTime
import com.paraooo.local.entity.TodoTemplate
import com.paraooo.local.entity.TodoTypeEntity
import com.paraooo.remote.dto.request.TodoInstanceRequestDto
import com.paraooo.remote.dto.request.TodoTemplateIdDto
import com.paraooo.remote.dto.request.TodoTemplateRequestDto
import com.paraooo.remote.dto.response.SyncPullResponseDto
import com.paraooo.remote.dto.response.TodoInstanceResponseDto
import com.paraooo.remote.dto.response.TodoTemplateResponseDto
import java.time.LocalTime
import java.util.UUID

internal fun AlarmTypeEntity.toModel() : AlarmType {
    return when(this) {
        AlarmTypeEntity.OFF -> AlarmType.OFF
        AlarmTypeEntity.NOTIFY -> AlarmType.NOTIFY
        AlarmTypeEntity.POPUP -> AlarmType.POPUP
    }
}

internal fun AlarmType.toEntity() : AlarmTypeEntity {
    return when(this) {
        AlarmType.OFF -> AlarmTypeEntity.OFF
        AlarmType.NOTIFY -> AlarmTypeEntity.NOTIFY
        AlarmType.POPUP -> AlarmTypeEntity.POPUP
    }
}


internal fun TodoEntity.toModel() = TodoModel(
    instanceId = instanceId,
    templateId = templateId,
    title = title,
    description = description,
    date = transferMillis2LocalDate(date),
    progressAngle = progressAngle,
    time = when(hour) {
        null -> null
        else -> LocalTime.of(hour!!, minute!!)
    },
    alarmType = alarmType.toModel(),
    startDate = startDate?.let { transferMillis2LocalDate(it) },
    endDate = endDate?.let { transferMillis2LocalDate(it) },
    dayOfWeeks = dayOfWeeks,
    isAlarmHasVibration = isAlarmHasVibration,
    isAlarmHasSound = isAlarmHasSound
)

internal fun TodoDayOfWeekModel.toEntity() = TodoDayOfWeek(
    id = id,
    templateId = templateId,
    dayOfWeeks = dayOfWeeks,
    dayOfWeek = dayOfWeek
)

internal fun TodoDayOfWeek.toModel() = TodoDayOfWeekModel(
    id = id,
    templateId = templateId,
    dayOfWeeks = dayOfWeeks,
    dayOfWeek = dayOfWeek
)
internal fun TodoTemplateModel.toEntity() = TodoTemplate(
    id = id,
    title = title,
    description = description,
    hour = hour,
    minute = minute,
    type = when(type) {
        TodoType.GENERAL -> TodoTypeEntity.GENERAL
        TodoType.PERIOD -> TodoTypeEntity.PERIOD
        TodoType.DAY_OF_WEEK -> TodoTypeEntity.DAY_OF_WEEK
    },
    alarmType = alarmType.toEntity(),
    isAlarmHasVibration = isAlarmHasVibration,
    isAlarmHasSound = isAlarmHasSound
)

internal fun TodoTemplate.toModel() = TodoTemplateModel(
    id = id,
    title = title,
    description = description,
    hour = hour,
    minute = minute,
    type = when(type) {
        TodoTypeEntity.GENERAL -> TodoType.GENERAL
        TodoTypeEntity.PERIOD -> TodoType.PERIOD
        TodoTypeEntity.DAY_OF_WEEK -> TodoType.DAY_OF_WEEK
    },
    alarmType = alarmType.toModel(),
    isAlarmHasVibration = isAlarmHasVibration,
    isAlarmHasSound = isAlarmHasSound
)

internal fun TodoDayOfWeekWithTime.toModel() = TodoDayOfWeekWithTimeModel(
    templateId = templateId,
    hour = hour,
    minute = minute,
    dayOfWeeks = dayOfWeeks
)

internal fun TodoInstance.toModel() = TodoInstanceModel(
    id = id,
    templateId = templateId,
    date = date,
    progressAngle = progressAngle
)

internal fun TodoInstanceModel.toEntity() = TodoInstance(
    id = id,
    templateId = templateId,
    date = date,
    progressAngle = progressAngle
)

internal fun TodoPeriod.toModel() = TodoPeriodModel(
    templateId = templateId,
    startDate = startDate,
    endDate = endDate
)

internal fun TodoPeriodModel.toEntity() = TodoPeriod(
    templateId = templateId,
    startDate = startDate,
    endDate = endDate
)

internal fun TodoPeriodWithTime.toModel() = TodoPeriodWithTimeModel(
    templateId = templateId,
    hour = hour,
    minute = minute,
    startDate = startDate,
    endDate = endDate
)

internal fun TodoTemplateModel.toDto() = TodoTemplateRequestDto(
    uuid = id.toString(),
    title = title,
    description = description,
    hour = hour,
    minute = minute,
    type = type.name,
    alarmType = alarmType.name,
    alarmHasVibration = isAlarmHasVibration,
    alarmHasSound = isAlarmHasSound,
    deleted = false
)
internal fun TodoInstanceModel.toDto() = TodoInstanceRequestDto(
    uuid = id.toString(),
    template = TodoTemplateIdDto(templateId.toString()),
    date = convertMillisToDateString(date),
    progressAngle = progressAngle,
    deleted = false
)

internal fun SyncPullResponseDto.toModel() = TodoSyncModel(
    changedTemplates = changedTemplates.map { it.toModel() },
    changedInstances = changedInstances.map { it.toModel() },
    newSyncTimestamp = newSyncTimestamp
)


internal fun TodoTemplateResponseDto.toModel() = TemplateTodoSyncModel(
    id = id,
    uuid = uuid,
    title = title,
    description = description,
    hour = hour,
    minute = minute,
    type = type,
    alarmType = alarmType,
    createdAt = createdAt,
    updatedAt = updatedAt,
    alarmHasVibration = alarmHasVibration,
    alarmHasSound = alarmHasSound,
    deleted = deleted
)

internal fun TodoInstanceResponseDto.toModel() = InstanceTodoSyncModel(
    id = id,
    uuid = uuid,
    templateUuid = templateUuid,
    date = date,
    progressAngle = progressAngle,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deleted = deleted
)

internal fun TemplateTodoSyncModel.toEntity() = TodoTemplate(
    id = UUID.fromString(uuid),
    title = title,
    description = description,
    hour = hour,
    minute = minute,
    type = when(type) {
        "GENERAL" -> TodoTypeEntity.GENERAL
        "PERIOD" -> TodoTypeEntity.PERIOD
        "DAY_OF_WEEK" -> TodoTypeEntity.DAY_OF_WEEK
        else -> throw IllegalArgumentException("Invalid type: $type")
    },
    alarmType = when(alarmType){
        "OFF" -> AlarmTypeEntity.OFF
        "NOTIFY" -> AlarmTypeEntity.NOTIFY
        "POPUP" -> AlarmTypeEntity.POPUP
        else -> throw IllegalArgumentException("Invalid alarmType: $alarmType")
    },
    isAlarmHasVibration = alarmHasVibration,
    isAlarmHasSound = alarmHasSound,
    needsSync = false
)

internal fun InstanceTodoSyncModel.toEntity() = TodoInstance(
    id = UUID.fromString(uuid),
    templateId = UUID.fromString(templateUuid),
    date = convertDateStringToMillis(date),
    progressAngle = progressAngle,
    needsSync = false
)