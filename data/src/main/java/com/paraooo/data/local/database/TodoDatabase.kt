package com.paraooo.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.paraooo.data.local.dao.TodoDao
import com.paraooo.data.local.entity.TodoEntity

@Database(entities = [TodoEntity::class], version = 2)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 새로 추가된 컬럼을 포함한 ALTER TABLE 쿼리 작성
        db.execSQL("ALTER TABLE TodoEntity ADD COLUMN groupId TEXT DEFAULT NULL")
        db.execSQL("ALTER TABLE TodoEntity ADD COLUMN startDate INTEGER DEFAULT NULL")
        db.execSQL("ALTER TABLE TodoEntity ADD COLUMN endDate INTEGER DEFAULT NULL")
    }
}