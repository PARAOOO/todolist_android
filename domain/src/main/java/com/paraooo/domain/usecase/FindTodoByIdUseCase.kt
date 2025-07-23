package com.paraooo.domain.usecase

import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.Time
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.util.transferMillis2LocalDate

class FindTodoByIdUseCase(
    private val instanceRepository: TodoInstanceRepository,
    private val templateRepository: TodoTemplateRepository,
    private val periodRepository: TodoPeriodRepository,
    private val dayOfWeekRepository: TodoDayOfWeekRepository
) {
    suspend operator fun invoke(instanceId: Long): TodoModel? {
        val instance = instanceRepository.getTodoInstanceById(instanceId) ?: return null
        val template = templateRepository.getTodoTemplateById(instance.templateId) ?: return null
        val period = periodRepository.getTodoPeriodByTemplateId(instance.templateId)
        val dayOfWeek = dayOfWeekRepository.getDayOfWeekByTemplateId(instance.templateId)
            .takeIf { it.isNotEmpty() }

        return TodoModel(
            instanceId = instance.id,
            title = template.title,
            description = template.description,
            date = instance.date,
            time = if (template.hour != null && template.minute != null) {
                Time(template.hour, template.minute)
            } else {
                null
            },
            alarmType = template.alarmType,
            progressAngle = instance.progressAngle,
            startDate = period?.startDate,
            endDate = period?.endDate,
            dayOfWeeks = dayOfWeek?.map { it.dayOfWeek },
            isAlarmHasVibration = template.isAlarmHasVibration,
            isAlarmHasSound = template.isAlarmHasSound
        )
    }
}
