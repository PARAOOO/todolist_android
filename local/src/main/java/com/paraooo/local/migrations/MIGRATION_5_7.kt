package com.paraooo.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val MIGRATION_5_7 = object : Migration(5, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE todo_template ADD COLUMN isAlarmHasVibration INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE todo_template ADD COLUMN isAlarmHasSound INTEGER NOT NULL DEFAULT 0")
    }
}