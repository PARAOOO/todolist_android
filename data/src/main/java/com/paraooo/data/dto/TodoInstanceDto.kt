package com.paraooo.data.dto

import androidx.room.PrimaryKey

data class TodoInstanceDto(
    val id: Long,
    val templateId: Long,
    val date: Long,
    val progressAngle: Float,
)