package com.paraooo.data.dto

import com.paraooo.data.local.entity.TodoType


data class TodoDto(
    val instanceId: Long, // TodoInstance의 ID (progressAngle 변경용)
    val templateId: Long, // 원본 TodoTemplate ID
    val title: String,
    val description: String,
    val date: Long,
    val hour: Int?,
    val minute: Int?,
    val progressAngle: Float, // 체크 상태
)