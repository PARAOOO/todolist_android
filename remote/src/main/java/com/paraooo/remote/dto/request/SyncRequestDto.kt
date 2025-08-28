package com.paraooo.remote.dto.request



data class TodoTemplateDto(
    val uuid: String,
    val title: String,
    val description: String,
    val hour: Int,
    val minute: Int,
    val type: String,
    val alarmType: String,
    val isAlarmHasVibration: Boolean,
    val isAlarmHasSound: Boolean,
    val isDeleted: Boolean
)

data class TodoTemplateIdDto(
    val uuid: String
)

data class TodoInstanceDto(
    val uuid: String,
    val template: List<TodoTemplateIdDto>,
    val date: String,
    val progressAngle: Double,
    val isDeleted: Boolean
)

data class SyncRequestDto(
    val templates: List<TodoTemplateDto>,
    val instances: List<TodoInstanceDto>,
    val deletedTemplateUuids: List<String>,
    val deletedInstanceUuids: List<String>
)
