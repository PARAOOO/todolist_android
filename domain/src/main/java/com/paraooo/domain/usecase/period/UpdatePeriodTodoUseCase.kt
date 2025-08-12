package com.paraooo.domain.usecase.period

import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoPeriodModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.util.todoToMillis
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import java.time.LocalDate

class UpdatePeriodTodoUseCase(
    private val todoInstanceRepository: TodoInstanceRepository,
    private val todoPeriodRepository: TodoPeriodRepository,
    private val alarmScheduler: AlarmScheduler
) {

    suspend operator fun invoke(todo: TodoModel): UseCaseResult<Unit> {

        try{
            val instanceTodo = todoInstanceRepository.getTodoInstanceById(todo.instanceId)
            val existingInstances =
                todoInstanceRepository.getInstancesByTemplateId(instanceTodo!!.templateId)

            val todoTemplate = TodoTemplateModel(
                id = instanceTodo.templateId,
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoType.PERIOD,
                alarmType = todo.alarmType,
                isAlarmHasVibration = todo.isAlarmHasVibration,
                isAlarmHasSound = todo.isAlarmHasSound
            )

            val todoPeriod = TodoPeriodModel(
                templateId = instanceTodo.templateId,
                startDate = transferLocalDateToMillis(todo.startDate),
                endDate = transferLocalDateToMillis(todo.endDate)
            )

            val existingDateMap = existingInstances.associate { it.date to it.progressAngle }

            val newDates = generateSequence(todo.startDate) { it.plusDays(1) }
                .takeWhile { !it.isAfter(todo.endDate) }
                .map { transferLocalDateToMillis(it) }
                .toSet()

            val oldDates = existingDateMap.keys

            val datesToDelete = oldDates - newDates // 기존에 있었지만, 새 범위에 포함되지 않는 날짜
            val datesToAdd = newDates - oldDates // 새 범위에 포함되지만, 기존에 없던 날짜

            val newInstances = datesToAdd.map { date ->
                TodoInstanceModel(
                    templateId = instanceTodo.templateId,
                    date = date,
                    progressAngle = 0F
                )
            }

            todoPeriodRepository.updateTodoPeriod(
                templateId = instanceTodo.templateId,
                todoTemplate = todoTemplate,
                todoPeriod = todoPeriod,
                datesToDelete = datesToDelete,
                todoInstancesToInsert = newInstances
            )

            alarmScheduler.cancel(instanceTodo.templateId)

            if (todo.time != null && todo.alarmType != AlarmType.OFF) {
                val nowLocalDateMillis = transferLocalDateToMillis(LocalDate.now())

                for (todoInstance in existingInstances) {

                    val alarmMillis = todoToMillis(
                        date = transferMillis2LocalDate(todoInstance.date),
                        time = todo.time
                    )

                    if (alarmMillis >= nowLocalDateMillis + 100) {
                        alarmScheduler.schedule(
                            date = transferMillis2LocalDate(todoInstance.date),
                            time = todo.time,
                            templateId = instanceTodo.templateId,
                        )
                        break
                    }
                }
            }

            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(e)
        }
    }
}