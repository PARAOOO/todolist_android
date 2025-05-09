package com.paraooo.data.dto

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.paraooo.data.local.entity.AlarmType
import com.paraooo.data.local.entity.TodoType

data class TodoTemplateDto(
    val id: Long = 0,
    val title: String,
    val description: String,
    val hour: Int?,
    val minute: Int?,
    val type: TodoTypeDto,
    val alarmType: AlarmTypeDto,
    val isAlarmHasVibration : Boolean,
    val isAlarmHasSound : Boolean
)

enum class TodoTypeDto{
    GENERAL, PERIOD, DAY_OF_WEEK
}

enum class AlarmTypeDto{
    OFF, NOTIFY, POPUP
}

