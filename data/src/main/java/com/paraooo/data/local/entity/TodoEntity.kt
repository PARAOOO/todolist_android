package com.paraooo.data.local.entity

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
    @ColumnInfo(index = true) val type: TodoType, // GENERAL, PERIOD, DAY_OF_WEEK,
    val alarmType : AlarmType
)

// ✅ 2. 특정 날짜에 생성된 Todo 인스턴스 (체크 상태 포함)
@Entity(
    tableName = "todo_instance",
    foreignKeys = [
        ForeignKey(entity = TodoTemplate::class, parentColumns = ["id"], childColumns = ["templateId"], onDelete = CASCADE)
    ],
    indices = [Index(value = ["date"]), Index(value = ["templateId"])] // 날짜와 템플릿 ID 인덱싱
)
data class TodoInstance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: Long, // 원본 TodoTemplate ID
    val date: Long, // Todo가 속한 특정 날짜
    val progressAngle: Float = 0F, // 체크 각도
)

//// ✅ 3. 특정 기간 동안 반복되는 Todo 정보
@Entity(
    tableName = "todo_period",
    foreignKeys = [
        ForeignKey(entity = TodoTemplate::class, parentColumns = ["id"], childColumns = ["templateId"], onDelete = CASCADE)
    ],
    indices = [Index(value = ["templateId"])]
)
data class TodoPeriod(
    @PrimaryKey val templateId: Long, // TodoTemplate의 ID
    val startDate: Long,
    val endDate: Long
)

// ✅ 4. 특정 요일에 반복되는 Todo 정보
@Entity(
    tableName = "todo_day_of_week",
    foreignKeys = [
        ForeignKey(entity = TodoTemplate::class, parentColumns = ["id"], childColumns = ["templateId"], onDelete = CASCADE)
    ],
    indices = [Index(value = ["templateId"]), Index(value = ["dayOfWeek"])] // 검색 속도 향상을 위해 요일 인덱스 추가
)
data class TodoDayOfWeek(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: Long, // TodoTemplate의 ID
    val dayOfWeeks: List<Int>,
    val dayOfWeek: Int // 1(월) ~ 7(일)
)

// ✅ 5. Todo 유형 정의
enum class TodoType {
    GENERAL, PERIOD, DAY_OF_WEEK
}

enum class AlarmType {
    OFF, NOTIFY, POPUP
}


