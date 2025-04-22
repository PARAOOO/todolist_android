package com.paraooo.data.dto

import androidx.room.PrimaryKey

data class TodoInstanceDto(
    val id: Long = 0,
    val templateId: Long,
    val date: Long,
    val progressAngle: Float = 0F,
)