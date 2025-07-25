package com.paraooo.domain.usecase.dayofweek

import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import java.time.LocalDate
import java.time.LocalTime

class UpdateDayOfWeekTodoUseCase(
    private val todoInstanceRepository: TodoInstanceRepository,
    private val todoTemplateRepository: TodoTemplateRepository,
    private val todoDayOfWeekRepository: TodoDayOfWeekRepository,
    private val alarmScheduler: AlarmScheduler
) {

    suspend operator fun invoke(todo: TodoModel) {
        val instanceTodo = todoInstanceRepository.getTodoInstanceById(todo.instanceId) ?: return
        val templateId = instanceTodo.templateId

        todoTemplateRepository.updateTodoTemplate(
            TodoTemplateModel(
                id = templateId,
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoType.DAY_OF_WEEK,
                alarmType = todo.alarmType,
                isAlarmHasVibration = todo.isAlarmHasVibration,
                isAlarmHasSound = todo.isAlarmHasSound
            )
        )

        // 2. 기존 요일 조회
        val existingDayOfWeeks = todoDayOfWeekRepository.getDayOfWeekByTemplateId(templateId)
        val existingDaysSet = existingDayOfWeeks.map { it.dayOfWeek }.toSet()
        val newDaysSet = todo.dayOfWeeks!!.toSet()

        // 3. 삭제할 요일
        val daysToDelete = existingDaysSet - newDaysSet
        if (daysToDelete.isNotEmpty()) {
            todoDayOfWeekRepository.deleteSpecificDayOfWeeks(templateId, daysToDelete.toList())
            todoDayOfWeekRepository.deleteInstancesByTemplateIdAndDaysOfWeek(templateId, daysToDelete.toList())
        }

        // 4. 추가할 요일
        val daysToAdd = newDaysSet - existingDaysSet
        val newDayOfWeeks = daysToAdd.map { dayOfWeek ->
            TodoDayOfWeekModel(
                templateId = templateId,
                dayOfWeeks = todo.dayOfWeeks,
                dayOfWeek = dayOfWeek
            )
        }
        todoDayOfWeekRepository.insertDayOfWeekTodos(newDayOfWeeks)

        alarmScheduler.cancel(templateId)

        if(todo.time != null && todo.alarmType != AlarmType.OFF){
            val today = LocalDate.now()
            val now = LocalTime.now()

            val todoTime = LocalTime.of(todo.time.hour, todo.time.minute) // ⏰ 시간 조합
            val isTimePassed = now > todoTime

            val startDayOffset = if (isTimePassed) 1 else 0

            val alarmDate = (startDayOffset..6).map { offset ->
                today.plusDays(offset.toLong())
            }.first { date ->
                todo.dayOfWeeks.contains(date.dayOfWeek.value)
            }

            alarmScheduler.schedule(
                date = alarmDate,
                time = todo.time,
                templateId = templateId
            )
        }
    }
}