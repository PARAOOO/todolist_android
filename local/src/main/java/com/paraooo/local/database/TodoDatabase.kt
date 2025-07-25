package com.paraooo.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.paraooo.local.dao.TodoDayOfWeekDao
import com.paraooo.local.dao.TodoInstanceDao
import com.paraooo.local.dao.TodoPeriodDao
import com.paraooo.local.dao.TodoTemplateDao
import com.paraooo.local.entity.TodoDayOfWeek
import com.paraooo.local.entity.TodoInstance
import com.paraooo.local.entity.TodoPeriod
import com.paraooo.local.entity.TodoTemplate
import com.paraooo.local.util.TodoConverters

//import com.paraooo.data.local.entity.TodoEntity

@Database(entities = [TodoInstance::class, TodoTemplate::class, TodoPeriod::class, TodoDayOfWeek::class], version = 7, exportSchema = false)
@TypeConverters(TodoConverters::class) // 여기 등록
internal abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoTemplateDao(): TodoTemplateDao
    abstract fun todoInstanceDao(): TodoInstanceDao
    abstract fun todoPeriodDao() : TodoPeriodDao
    abstract fun todoDayOfWeekDao() : TodoDayOfWeekDao

}
