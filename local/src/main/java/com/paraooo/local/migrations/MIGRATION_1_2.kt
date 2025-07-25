package com.paraooo.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE TodoEntity ADD COLUMN groupId TEXT DEFAULT NULL")
        db.execSQL("ALTER TABLE TodoEntity ADD COLUMN startDate INTEGER DEFAULT NULL")
        db.execSQL("ALTER TABLE TodoEntity ADD COLUMN endDate INTEGER DEFAULT NULL")
    }
}