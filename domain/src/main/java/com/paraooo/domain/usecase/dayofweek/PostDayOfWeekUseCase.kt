package com.paraooo.domain.usecase.dayofweek

import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import java.time.LocalDate
import java.time.LocalTime

class PostDayOfWeekUseCase(
    private val todoTemplateRepository: TodoTemplateRepository,
    private val todoDayOfWeekRepository: TodoDayOfWeekRepository,
    private val alarmScheduler: AlarmScheduler
) {

    suspend operator fun invoke(todo: TodoModel, dayOfWeek: List<Int>)  {

        val todoTemplate = TodoTemplateModel(
            title = todo.title,
            description = todo.description ?: "",
            hour = todo.time?.hour,
            minute = todo.time?.minute,
            type = TodoType.DAY_OF_WEEK,
            alarmType = todo.alarmType,
            isAlarmHasVibration = todo.isAlarmHasVibration,
            isAlarmHasSound = todo.isAlarmHasSound
        )

        val templateId = todoTemplateRepository.insertTodoTemplate(todoTemplate)

        for (week in dayOfWeek) {
            todoDayOfWeekRepository.insertTodoDayOfWeek(
                TodoDayOfWeekModel(
                    templateId = templateId,
                    dayOfWeeks = dayOfWeek,
                    dayOfWeek = week
                )
            )
        }

        if(todo.time != null && todo.alarmType != AlarmType.OFF){
            val today = LocalDate.now()
            val now = LocalTime.now()

            val todoTime = LocalTime.of(todo.time.hour, todo.time.minute) // ⏰ 시간 조합
            val isTimePassed = now > todoTime

            val startDayOffset = if (isTimePassed) 1 else 0

            val alarmDate = (startDayOffset..6).map { offset ->
                today.plusDays(offset.toLong())
            }.first { date ->
                dayOfWeek.contains(date.dayOfWeek.value)
            }

            alarmScheduler.schedule(
                date = alarmDate,
                time = todo.time,
                templateId = templateId
            )
        }
    }
}