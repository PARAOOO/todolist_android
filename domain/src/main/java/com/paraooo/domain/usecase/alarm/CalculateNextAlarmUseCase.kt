package com.paraooo.domain.usecase.alarm

import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.model.TodoPeriodModel
import com.paraooo.domain.model.TodoTemplateModel
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
    private val todoTemplateRepository: TodoTemplateRepository,
    private val todoPeriodRepository: TodoPeriodRepository,
    private val todoDayOfWeekRepository: TodoDayOfWeekRepository
) {
    suspend operator fun invoke(todoTemplate : TodoTemplateModel, todoPeriod: TodoPeriodModel?, todoDayOfWeek : List<TodoDayOfWeekModel>?, todayDate: LocalDate): AlarmSchedule? {

        val todayMillis = transferLocalDateToMillis(todayDate)
        val todayLocalDate = todayDate

        when(todoTemplate.type) {
            TodoType.GENERAL -> {
                return null
            }
            TodoType.PERIOD -> {
                if(todoPeriod!!.endDate > todayMillis) {
                    val tomorrowLocalDate = todayLocalDate.plusDays(1)

                    return AlarmSchedule(
                        date = tomorrowLocalDate,
                        time = Time(todoTemplate.hour!!, todoTemplate.minute!!),
                        templateId = todoTemplate.id
                    )
                }
            }
            TodoType.DAY_OF_WEEK -> {
                val availableDays = todoDayOfWeek!!.first().dayOfWeeks

                val nextAlarmDate = (1..7)
                    .map { todayLocalDate.plusDays(it.toLong()) }
                    .first { availableDays.contains(it.dayOfWeek.value) }

                return AlarmSchedule(
                    date = nextAlarmDate,
                    time = Time(todoTemplate.hour!!, todoTemplate.minute!!),
                    templateId = todoTemplate.id
                )
            }
        }
        return null
    }
}
