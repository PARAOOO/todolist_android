package com.paraooo.domain.usecase.period

import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoPeriodModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.util.todoToMillis
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class PostPeriodTodoUseCase(
    private val todoInstanceRepository: TodoInstanceRepository,
    private val todoTemplateRepository: TodoTemplateRepository,
    private val todoPeriodRepository: TodoPeriodRepository,
    private val alarmScheduler: AlarmScheduler
) {

    suspend operator fun invoke(todo: TodoModel, startDate: LocalDate, endDate: LocalDate) {
        withContext(Dispatchers.IO){

            val todoTemplate = TodoTemplateModel(
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoType.PERIOD,
                alarmType = todo.alarmType,
                isAlarmHasVibration = todo.isAlarmHasVibration,
                isAlarmHasSound = todo.isAlarmHasSound
            )

            val templateId = todoTemplateRepository.insertTodoTemplate(todoTemplate)
            val todos = mutableListOf<TodoInstanceModel>()

            var currentDate = startDate
            while (currentDate <= endDate) {
                todos.add(
                    TodoInstanceModel(
                        templateId = templateId,
                        date = transferLocalDateToMillis(currentDate)
                    )
                )
                currentDate = currentDate.plusDays(1)
            }

            todoInstanceRepository.insertTodoInstances(todos)

            todoPeriodRepository.insertTodoPeriod(
                TodoPeriodModel(
                    templateId = templateId,
                    startDate = transferLocalDateToMillis(startDate),
                    endDate = transferLocalDateToMillis(endDate)
                )
            )

            if(todo.time != null && todo.alarmType != AlarmType.OFF){
                val nowLocalDateMillis = transferLocalDateToMillis(LocalDate.now())

                for (todoInstance in todos) {

                    val alarmMillis = todoToMillis(
                        date = transferMillis2LocalDate(todoInstance.date),
                        time = todo.time
                    )

                    if (alarmMillis >= nowLocalDateMillis + 100) {
                        alarmScheduler.schedule(
                            date = transferMillis2LocalDate(todoInstance.date),
                            time = todo.time,
                            templateId = todoInstance.templateId,
                        )
                        break
                    }
                }
            }
        }
    }
}