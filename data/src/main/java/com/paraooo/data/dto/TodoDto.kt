package com.paraooo.data.dto


data class TodoDto(
    val id: Int,
    val title: String,
    val description: String? = null,
    val date: Long,
    val hour : Int? = null,
    val minute : Int? = null,
    val progressAngle : Float = 0F
)