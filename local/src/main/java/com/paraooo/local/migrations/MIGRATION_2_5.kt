package com.paraooo.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val MIGRATION_2_5 = object : Migration(2, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 1. 새로운 테이블 생성
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS todo_template (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL DEFAULT 'undefined',
                description TEXT NOT NULL DEFAULT 'undefined',
                hour INTEGER,
                minute INTEGER,
                type TEXT NOT NULL DEFAULT 'undefined'
            );
        """.trimIndent())

        db.execSQL("CREATE INDEX IF NOT EXISTS index_todo_template_type ON todo_template(type)")

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS todo_instance (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                templateId INTEGER NOT NULL,
                date INTEGER NOT NULL,
                progressAngle REAL NOT NULL,
                FOREIGN KEY(templateId) REFERENCES todo_template(id) ON DELETE CASCADE
            )
        """.trimIndent())

        // ✅ Room이 자동 생성해주는 인덱스까지 수동으로 추가
        db.execSQL("CREATE INDEX IF NOT EXISTS index_todo_instance_templateId ON todo_instance(templateId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_todo_instance_date ON todo_instance(date)")

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS todo_period (
                templateId INTEGER NOT NULL,
                startDate INTEGER NOT NULL DEFAULT 'undefined',
                endDate INTEGER NOT NULL DEFAULT 'undefined',
                PRIMARY KEY(templateId),
                FOREIGN KEY(templateId) REFERENCES todo_template(id) ON DELETE CASCADE ON UPDATE NO ACTION
            );
        """.trimIndent())

        db.execSQL("CREATE INDEX IF NOT EXISTS index_todo_period_templateId ON todo_period(templateId)")

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS todo_day_of_week (
                id INTEGER NOT NULL PRIMARY KEY,
                templateId INTEGER NOT NULL,
                dayOfWeek INTEGER NOT NULL,
                dayOfWeeks TEXT NOT NULL,
                FOREIGN KEY(templateId) REFERENCES todo_template(id) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("CREATE INDEX IF NOT EXISTS index_todo_day_of_week_dayOfWeek ON todo_day_of_week(dayOfWeek)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_todo_day_of_week_templateId ON todo_day_of_week(templateId)")

        // 2. 기존 데이터 마이그레이션
        val cursor = db.query("SELECT * FROM TodoEntity")
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description")) ?: ""
            val date = cursor.getLong(cursor.getColumnIndexOrThrow("date"))
            val hour = if (cursor.isNull(cursor.getColumnIndexOrThrow("hour"))) null else cursor.getInt(cursor.getColumnIndexOrThrow("hour"))
            val minute = if (cursor.isNull(cursor.getColumnIndexOrThrow("minute"))) null else cursor.getInt(cursor.getColumnIndexOrThrow("minute"))
            val progressAngle = cursor.getFloat(cursor.getColumnIndexOrThrow("progressAngle"))
            val startDate = if (cursor.isNull(cursor.getColumnIndexOrThrow("startDate"))) null else cursor.getLong(cursor.getColumnIndexOrThrow("startDate"))
            val endDate = if (cursor.isNull(cursor.getColumnIndexOrThrow("endDate"))) null else cursor.getLong(cursor.getColumnIndexOrThrow("endDate"))

            val todoType = when {
                startDate != null && endDate != null -> "PERIOD"
                else -> "GENERAL"
            }

            // todo_template 삽입
            db.execSQL("""
                INSERT INTO todo_template (id, title, description, hour, minute, type)
                VALUES (?, ?, ?, ?, ?, ?)
            """.trimIndent(), arrayOf(id, title, description, hour, minute, todoType))

            // todo_instance 삽입
            db.execSQL("""
                INSERT INTO todo_instance (templateId, date, progressAngle)
                VALUES (?, ?, ?)
            """.trimIndent(), arrayOf(id, date, progressAngle))

            // PERIOD일 경우만 추가
            if (todoType == "PERIOD") {
                db.execSQL("""
                    INSERT INTO todo_period (templateId, startDate, endDate)
                    VALUES (?, ?, ?)
                """.trimIndent(), arrayOf(id, startDate, endDate))
            }
        }
        cursor.close()

        // 3. 기존 테이블 제거
        db.execSQL("DROP TABLE IF EXISTS TodoEntity")
    }
}