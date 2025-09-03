package com.paraooo.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "deleted_todo")
data class DeletedTodo(
    @PrimaryKey val id: UUID,
    val itemType: String,
    val deletedAt: Long = System.currentTimeMillis()
)
