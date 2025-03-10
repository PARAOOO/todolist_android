package com.paraooo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String? = null,
    val date: Long,
    val hour : Int? = null,
    val minute : Int? = null,
    val progressAngle : Float = 0F,

    // Period Todo만 가지고 있는 속성
    val groupId : String? = null,
    val startDate : Long? = null,
    val endDate : Long? = null
)
