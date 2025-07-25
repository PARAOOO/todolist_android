package com.paraooo.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "todo_template")
data class TodoTemplate(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val hour: Int?, // null이면 시간 미지정,
    val minute: Int?,
    @ColumnInfo(index = true) val type: TodoTypeEntity, // GENERAL, PERIOD, DAY_OF_WEEK,
    val alarmType : AlarmTypeEntity, // OFF, NOTIFY, POPUP
    val isAlarmHasVibration : Boolean,
    val isAlarmHasSound : Boolean
)

@Entity(
    tableName = "todo_instance",
    foreignKeys = [
        ForeignKey(entity = TodoTemplate::class, parentColumns = ["id"], childColumns = ["templateId"], onDelete = CASCADE)
    ],
    indices = [Index(value = ["date"]), Index(value = ["templateId"])] // date, templateId 인덱스 설정
)
data class TodoInstance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: Long, // 원본 TodoTemplate Id
    val date: Long,
    val progressAngle: Float = 0F,
)

@Entity(
    tableName = "todo_period",
    foreignKeys = [
        ForeignKey(entity = TodoTemplate::class, parentColumns = ["id"], childColumns = ["templateId"], onDelete = CASCADE)
    ],
    indices = [Index(value = ["templateId"])] // templateId 인덱스 설정
)
data class TodoPeriod(
    @PrimaryKey val templateId: Long, // 원본 TodoTemplate Id
    val startDate: Long,
    val endDate: Long
)

@Entity(
    tableName = "todo_day_of_week",
    foreignKeys = [
        ForeignKey(entity = TodoTemplate::class, parentColumns = ["id"], childColumns = ["templateId"], onDelete = CASCADE)
    ],
    indices = [Index(value = ["templateId"]), Index(value = ["dayOfWeek"])] // 검색 속도 향상을 위해 요일 인덱스 추가
)
data class TodoDayOfWeek(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: Long, // 원본 TodoTemplate Id
    val dayOfWeeks: List<Int>,
    val dayOfWeek: Int // 1(월) ~ 7(일)
)

data class TodoEntity(
    val instanceId: Long,
    val templateId: Long,
    val title: String,
    val description: String,
    val date: Long,
    val hour: Int?,
    val minute: Int?,
    val progressAngle: Float,
    val alarmType: AlarmTypeEntity,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val dayOfWeeks: List<Int>? = null,
    val isAlarmHasVibration : Boolean,
    val isAlarmHasSound : Boolean
)

data class TodoPeriodWithTime(
    val templateId : Long,
    val hour: Int?,
    val minute: Int?,
    val startDate: Long,
    val endDate: Long
)

data class TodoDayOfWeekWithTime(
    val templateId : Long,
    val hour: Int?,
    val minute: Int?,
    val dayOfWeeks : List<Int>
)

enum class TodoTypeEntity {
    GENERAL, PERIOD, DAY_OF_WEEK
}

enum class AlarmTypeEntity {
    OFF, NOTIFY, POPUP
}


