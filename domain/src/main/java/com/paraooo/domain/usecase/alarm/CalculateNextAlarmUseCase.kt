package com.paraooo.domain.usecase.alarm

import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import java.time.LocalDate

data class AlarmSchedule(
    val date: LocalDate,
    val time: Time,
    val templateId: Long
)

class CalculateNextAlarmUseCase(
    private val todoRepository: TodoRepository,
    private val todoInstanceRepository: TodoInstanceRepository,
    private val todoTemplateRepository: TodoTemplateRepository,
    private val todoPeriodRepository: TodoPeriodRepository,
    private val todoDayOfWeekRepository: TodoDayOfWeekRepository
) {
    suspend operator fun invoke(templateId: Long, todayDate: LocalDate): AlarmSchedule? {

        val todayMillis = transferLocalDateToMillis(todayDate)
        val todayLocalDate = todayDate

        val todoInstances = todoInstanceRepository.getInstancesByTemplateId(templateId)
        val todoTemplate = todoTemplateRepository.getTodoTemplateById(templateId) ?: return null
        val period = todoPeriodRepository.getTodoPeriodByTemplateId(templateId)
        val dayOfWeek = todoDayOfWeekRepository.getDayOfWeekByTemplateId(templateId).takeIf { it.isNotEmpty() }

        val todayInstance = todoInstances.firstOrNull {
            transferMillis2LocalDate(it.date) == todayLocalDate
        }

        when(todoTemplate.type) {
            TodoType.GENERAL -> {

            }
            TodoType.PERIOD -> {
                if(period!!.endDate > todayMillis) {
                    val tomorrowLocalDate = todayLocalDate.plusDays(1)
                    alarmScheduler.schedule(
                        date = tomorrowLocalDate,
                        time = Time(todoTemplate.hour!!, todoTemplate.minute!!),
                        templateId = templateId
                    )
                }
            }
            TodoType.DAY_OF_WEEK -> {
                val availableDays = dayOfWeek!!.first().dayOfWeeks

                val nextAlarmDate = (1..7)
                    .map { todayLocalDate.plusDays(it.toLong()) }
                    .first { availableDays.contains(it.dayOfWeek.value) }

                alarmScheduler.schedule(
                    date = nextAlarmDate,
                    time = Time(todoTemplate.hour!!, todoTemplate.minute!!),
                    templateId = templateId
                )
            }
        }
    }
}
