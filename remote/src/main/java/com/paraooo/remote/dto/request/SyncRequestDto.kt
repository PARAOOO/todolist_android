package com.paraooo.remote.dto.request
data class TodoTemplateRequestDto(
    val uuid: String,
    val title: String,
    val description: String,
    val hour: Int,
    val minute: Int,
    val type: String,
    val alarmType: String,
    val alarmHasVibration: Boolean,
    val alarmHasSound: Boolean,
    val deleted: Boolean
)

data class TodoTemplateIdDto(
    val uuid: String
)

data class TodoInstanceRequestDto(
    val uuid: String,
    val template: List<TodoTemplateIdDto>,
    val date: String,
    val progressAngle: Double,
    val deleted: Boolean
)

data class SyncRequestDto(
    val templates: List<TodoTemplateRequestDto>,
    val instances: List<TodoInstanceRequestDto>,
    val deletedTemplateUuids: List<String>,
    val deletedInstanceUuids: List<String>
)
