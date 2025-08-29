package com.paraooo.remote.dto.response
data class SyncPullResponseDto(
    val changedTemplates: List<TodoTemplateResponseDto>,
    val changedInstances: List<TodoInstanceResponseDto>,
    val newSyncTimestamp: String
)

data class TodoTemplateResponseDto(
    val id: Long,
    val uuid: String,
    val title: String,
    val description: String,
    val hour: Int,
    val minute: Int,
    val type: String,
    val alarmType: String,
    val createdAt: String,
    val updatedAt: String,
    val todoInstances: List<TodoInstanceResponseDto>,
    val alarmHasVibration: Boolean,
    val alarmHasSound: Boolean,
    val deleted: Boolean
)

data class TodoInstanceResponseDto(
    val id: Long,
    val uuid: String,
    val date: String,
    val progressAngle: Double,
    val createdAt: String,
    val updatedAt: String,
    val deleted: Boolean
)