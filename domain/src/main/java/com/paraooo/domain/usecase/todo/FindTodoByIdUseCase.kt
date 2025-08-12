package com.paraooo.domain.usecase.todo

import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.util.transferMillis2LocalDate

class FindTodoByIdUseCase(
    private val todoRepository : TodoRepository
) {

    suspend operator fun invoke(instanceId: Long): UseCaseResult<TodoModel> {

        try {
            val findTodoByIdResponse = todoRepository.findTodoById(instanceId)

            val instance = findTodoByIdResponse.todoInstance
            val template = findTodoByIdResponse.todoTemplate
            val period = findTodoByIdResponse.todoPeriod
            val dayOfWeek = findTodoByIdResponse.todoDayOfWeek

            return UseCaseResult.Success(
                TodoModel(
                    instanceId = instance.id,
                    templateId = instance.templateId,
                    title = template.title,
                    description = template.description,
                    date = transferMillis2LocalDate(instance.date),
                    time = if (template.hour != null && template.minute != null) {
                        Time(template.hour, template.minute)
                    } else {
                        null
                    },
                    alarmType = template.alarmType,
                    progressAngle = instance.progressAngle,
                    startDate = period?.startDate?.let { transferMillis2LocalDate(it) },
                    endDate = period?.endDate?.let { transferMillis2LocalDate(it) },
                    dayOfWeeks = dayOfWeek?.map { it.dayOfWeek },
                    isAlarmHasVibration = template.isAlarmHasVibration,
                    isAlarmHasSound = template.isAlarmHasSound
                )
            )
        } catch (e: Exception) {
            return UseCaseResult.Error(e)
        }
    }
}