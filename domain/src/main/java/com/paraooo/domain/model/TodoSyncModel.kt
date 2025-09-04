package com.paraooo.domain.model

data class TodoSyncModel(
    val changedTemplates: List<TemplateTodoSyncModel>,
    val changedInstances: List<InstanceTodoSyncModel>,
    val newSyncTimestamp: String
)

data class TemplateTodoSyncModel(
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
    val alarmHasVibration: Boolean,
    val alarmHasSound: Boolean,
    val deleted: Boolean
)

data class InstanceTodoSyncModel(
    val id: Long,
    val uuid: String,
    val templateUuid: String,
    val date: String,
    val progressAngle: Float,
    val createdAt: String,
    val updatedAt: String,
    val deleted: Boolean
)