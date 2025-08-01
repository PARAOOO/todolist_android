package com.paraooo.domain.usecase.todo

import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.util.transferMillis2LocalDate
import java.time.LocalTime

class FindTodoByIdUseCase(
    private val todoTemplateRepository: TodoTemplateRepository,
    private val todoDayOfWeekRepository: TodoDayOfWeekRepository,
    private val todoInstanceRepository: TodoInstanceRepository,
    private val todoPeriodRepository: TodoPeriodRepository
) {

    suspend operator fun invoke(instanceId: Long): TodoModel {
        val instance = todoInstanceRepository.getTodoInstanceById(instanceId)
        val template = todoTemplateRepository.getTodoTemplateById(instance!!.templateId)
        val period = todoPeriodRepository.getTodoPeriodByTemplateId(instance.templateId)
        val dayOfWeek = todoDayOfWeekRepository.getDayOfWeekByTemplateId(instance.templateId).takeIf { it.isNotEmpty() }

        return TodoModel(
            instanceId = instance.id,
            templateId = instance.templateId,
            title = template!!.title,
            description = template.description,
            date = transferMillis2LocalDate(instance.date),
            time = if (template.hour != null && template.minute != null) {
                LocalTime.of(template.hour, template.minute)
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
    }
}