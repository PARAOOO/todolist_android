package com.paraooo.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.paraooo.local.dao.DeletedTodoDao
import com.paraooo.local.dao.SyncTodoDao
import com.paraooo.local.dao.TodoDayOfWeekDao
import com.paraooo.local.dao.TodoInstanceDao
import com.paraooo.local.dao.TodoPeriodDao
import com.paraooo.local.dao.TodoTemplateDao
import com.paraooo.local.entity.DeletedTodo
import com.paraooo.local.entity.TodoDayOfWeek
import com.paraooo.local.entity.TodoInstance
import com.paraooo.local.entity.TodoPeriod
import com.paraooo.local.entity.TodoTemplate
import com.paraooo.local.util.TodoConverters

//import com.paraooo.data.local.entity.TodoEntity

@Database(entities = [TodoInstance::class, TodoTemplate::class, TodoPeriod::class, TodoDayOfWeek::class, DeletedTodo::class], version = 11, exportSchema = false)
@TypeConverters(TodoConverters::class) // 여기 등록
internal abstract class TodoDatabase : RoomDatabase(), TransactionProvider {
    abstract fun todoTemplateDao(): TodoTemplateDao
    abstract fun todoInstanceDao(): TodoInstanceDao
    abstract fun todoPeriodDao() : TodoPeriodDao
    abstract fun todoDayOfWeekDao() : TodoDayOfWeekDao
    abstract fun deletedTodoDao() : DeletedTodoDao
    abstract fun syncTodoDao() : SyncTodoDao

    override suspend fun <R> runInTransaction(block: suspend () -> R): R {
        return withTransaction(block)
    }

    companion object {
        val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                db.execSQL("""
                    CREATE TRIGGER create_template_tombstone_after_delete
                    AFTER DELETE ON todo_template
                    BEGIN
                        INSERT INTO deleted_todo (id, itemType, deletedAt)
                        VALUES (OLD.id, 'TEMPLATE', STRFTIME('%s', 'now') * 1000);
                    END;
                """.trimIndent())

                db.execSQL("""
                    CREATE TRIGGER create_instance_tombstone_after_delete
                    AFTER DELETE ON todo_instance
                    BEGIN
                        INSERT INTO deleted_todo (id, itemType, deletedAt)
                        VALUES (OLD.id, 'INSTANCE', STRFTIME('%s', 'now') * 1000);
                    END;
                """.trimIndent())


                db.execSQL("""
                    CREATE TRIGGER IF NOT EXISTS template_sync_on_update
                    AFTER UPDATE ON todo_template
                    FOR EACH ROW
                    WHEN OLD.needsSync = 0 AND NEW.needsSync = 0
                    BEGIN
                        UPDATE todo_template SET needsSync = 1 WHERE id = NEW.id;
                    END
                """.trimIndent())

                db.execSQL("""
                    CREATE TRIGGER IF NOT EXISTS instance_sync_on_update
                    AFTER UPDATE ON todo_instance
                    FOR EACH ROW
                    WHEN OLD.needsSync = 0 AND NEW.needsSync = 0
                    BEGIN
                        UPDATE todo_instance SET needsSync = 1 WHERE id = NEW.id;
                    END
                """.trimIndent())

            }
        }
    }

}
