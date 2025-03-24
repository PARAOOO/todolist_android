package com.paraooo.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.paraooo.data.local.dao.TodoDao
import com.paraooo.data.local.entity.TodoInstance
import com.paraooo.data.local.entity.TodoPeriod
import com.paraooo.data.local.entity.TodoTemplate

//import com.paraooo.data.local.entity.TodoEntity

//@Database(entities = [TodoEntity::class], version = 3)
@Database(entities = [TodoInstance::class, TodoTemplate::class, TodoPeriod::class], version = 4, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE TodoEntity ADD COLUMN groupId TEXT DEFAULT NULL")
        db.execSQL("ALTER TABLE TodoEntity ADD COLUMN startDate INTEGER DEFAULT NULL")
        db.execSQL("ALTER TABLE TodoEntity ADD COLUMN endDate INTEGER DEFAULT NULL")
    }
}