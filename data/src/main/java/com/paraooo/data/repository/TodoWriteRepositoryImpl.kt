package com.paraooo.data.repository

import com.paraooo.data.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.data.datasource.TodoInstanceLocalDataSource
import com.paraooo.data.datasource.TodoPeriodLocalDataSource
import com.paraooo.data.datasource.TodoTemplateLocalDataSource
import com.paraooo.data.dto.TodoDayOfWeekDto
import com.paraooo.data.dto.TodoInstanceDto
import com.paraooo.data.dto.TodoPeriodDto
import com.paraooo.data.dto.TodoTemplateDto
import com.paraooo.data.dto.TodoTypeDto
import com.paraooo.data.mapper.toDto
import com.paraooo.data.platform.alarm.AlarmScheduler
import com.paraooo.data.platform.alarm.todoToMillis
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoWriteRepository
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

const val TAG = "PARAOOO"

class TodoWriteRepositoryImpl(
    private val todoTemplateLocalDataSource : TodoTemplateLocalDataSource,
    private val todoInstanceLocalDataSource : TodoInstanceLocalDataSource,
    private val todoPeriodLocalDataSource : TodoPeriodLocalDataSource,
    private val todoDayOfWeekLocalDataSource : TodoDayOfWeekLocalDataSource,
    private val alarmScheduler: AlarmScheduler,
) : TodoWriteRepository {

    override suspend fun postTodo(todo: TodoModel) {

        val todoTemplate = TodoTemplateDto(
            title = todo.title,
            description = todo.description ?: "",
            hour = todo.time?.hour,
            minute = todo.time?.minute,
            type = TodoTypeDto.GENERAL,
            alarmType = todo.alarmType.toDto(),
            isAlarmHasVibration = todo.isAlarmHasVibration,
            isAlarmHasSound = todo.isAlarmHasSound
        )

        val templateId = todoTemplateLocalDataSource.insertTodoTemplate(todoTemplate)

        val instanceId = todoInstanceLocalDataSource.insertTodoInstance(
            TodoInstanceDto(
                templateId = templateId,
                date = transferLocalDateToMillis(todo.date)
            )
        )

        if(todo.time != null){
            when (todo.alarmType) {
                AlarmType.OFF -> {}
                AlarmType.NOTIFY, AlarmType.POPUP -> {
                    alarmScheduler.schedule(todo.date, todo.time!!, templateId)
                }
            }
        }

    }

    override suspend fun updateTodoProgress(instanceId: Long, progress: Float) {
        todoInstanceLocalDataSource.updateTodoProgress(instanceId, progress)

    }

    override suspend fun deleteTodoById(instanceId: Long) {
        val instanceTodo = todoInstanceLocalDataSource.getTodoInstanceById(instanceId)

        todoTemplateLocalDataSource.deleteTodoTemplate(instanceTodo!!.templateId)

        alarmScheduler.cancel(templateId = instanceTodo.templateId)

    }

    override suspend fun updateTodo(todo: TodoModel) {

        val instanceTodo = todoInstanceLocalDataSource.getTodoInstanceById(todo.instanceId)

        todoTemplateLocalDataSource.updateTodoTemplate(
            TodoTemplateDto(
                id = instanceTodo!!.templateId,
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoTypeDto.GENERAL,
                alarmType = todo.alarmType.toDto(),
                isAlarmHasVibration = todo.isAlarmHasVibration,
                isAlarmHasSound = todo.isAlarmHasSound
            )
        )

        todoInstanceLocalDataSource.updateTodoInstance(
            TodoInstanceDto(
                id = todo.instanceId,
                templateId = instanceTodo.templateId,
                date = transferLocalDateToMillis(todo.date),
                progressAngle = todo.progressAngle
            )
        )

        if(todo.time != null){
            when (todo.alarmType) {
                AlarmType.OFF -> {}
                AlarmType.NOTIFY, AlarmType.POPUP -> {
                    alarmScheduler.reschedule(todo.date, todo.time!!, instanceTodo.templateId)
                }
            }
        } else {
            alarmScheduler.cancel(instanceTodo.templateId)
        }
    }

    override suspend fun postPeriodTodo(todo: TodoModel, startDate: LocalDate, endDate: LocalDate) {

        withContext(Dispatchers.IO){

            val todoTemplate = TodoTemplateDto(
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoTypeDto.PERIOD,
                alarmType = todo.alarmType.toDto(),
                isAlarmHasVibration = todo.isAlarmHasVibration,
                isAlarmHasSound = todo.isAlarmHasSound
            )

            val templateId = todoTemplateLocalDataSource.insertTodoTemplate(todoTemplate)
            val todos = mutableListOf<TodoInstanceDto>()

            var currentDate = startDate
            while (currentDate <= endDate) {
                todos.add(
                    TodoInstanceDto(
                        templateId = templateId,
                        date = transferLocalDateToMillis(currentDate)
                    )
                )
                currentDate = currentDate.plusDays(1)
            }

            todoInstanceLocalDataSource.insertTodoInstances(todos)

            todoPeriodLocalDataSource.insertTodoPeriod(
                TodoPeriodDto(
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
                        time = todo.time!!
                    )

                    if (alarmMillis >= nowLocalDateMillis + 100) {
                        alarmScheduler.schedule(
                            date = transferMillis2LocalDate(todoInstance.date),
                            time = todo.time!!,
                            templateId = todoInstance.templateId,
                        )
                        break
                    }
                }
            }
        }
    }

    override suspend fun updatePeriodTodo(todo: TodoModel) {

        val instanceTodo = todoInstanceLocalDataSource.getTodoInstanceById(todo.instanceId)

        todoTemplateLocalDataSource.updateTodoTemplate(
            TodoTemplateDto(
                id = instanceTodo!!.templateId,
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoTypeDto.PERIOD,
                alarmType = todo.alarmType.toDto(),
                isAlarmHasVibration = todo.isAlarmHasVibration,
                isAlarmHasSound = todo.isAlarmHasSound
            )
        )

        val existingInstances = todoInstanceLocalDataSource.getInstancesByTemplateId(instanceTodo.templateId)

        val existingDateMap = existingInstances.associate { it.date to it.progressAngle }

        todoPeriodLocalDataSource.updateTodoPeriod(
            TodoPeriodDto(
                templateId = instanceTodo.templateId,
                startDate = transferLocalDateToMillis(todo.startDate),
                endDate = transferLocalDateToMillis(todo.endDate)
            )
        )

        val newDates = generateSequence(todo.startDate) { it.plusDays(1) }
            .takeWhile { !it.isAfter(todo.endDate) }
            .map { transferLocalDateToMillis(it) }
            .toSet()

        val oldDates = existingDateMap.keys

        val datesToDelete = oldDates - newDates // 기존에 있었지만, 새 범위에 포함되지 않는 날짜
        val datesToAdd = newDates - oldDates // 새 범위에 포함되지만, 기존에 없던 날짜

        todoInstanceLocalDataSource.deleteInstancesByDates(instanceTodo.templateId, datesToDelete)

        val newInstances = datesToAdd.map { date ->
            TodoInstanceDto(templateId = instanceTodo.templateId, date = date, progressAngle = 0F)
        }

        todoInstanceLocalDataSource.insertTodoInstances(newInstances)

        alarmScheduler.cancel(instanceTodo.templateId)

        if(todo.time != null && todo.alarmType != AlarmType.OFF){
            val nowLocalDateMillis = transferLocalDateToMillis(LocalDate.now())

            for (todoInstance in existingInstances) {

                val alarmMillis = todoToMillis(
                    date = transferMillis2LocalDate(todoInstance.date),
                    time = todo.time!!
                )

                if (alarmMillis >= nowLocalDateMillis + 100) {
                    alarmScheduler.schedule(
                        date = transferMillis2LocalDate(todoInstance.date),
                        time = todo.time!!,
                        templateId = instanceTodo.templateId,
                    )
                    break
                }
            }
        }
    }

    override suspend fun postDayOfWeekTodo(todo: TodoModel, dayOfWeek: List<Int>) {

        val todoTemplate = TodoTemplateDto(
            title = todo.title,
            description = todo.description ?: "",
            hour = todo.time?.hour,
            minute = todo.time?.minute,
            type = TodoTypeDto.DAY_OF_WEEK,
            alarmType = todo.alarmType.toDto(),
            isAlarmHasVibration = todo.isAlarmHasVibration,
            isAlarmHasSound = todo.isAlarmHasSound
        )

        val templateId = todoTemplateLocalDataSource.insertTodoTemplate(todoTemplate)

        for (week in dayOfWeek) {
            todoDayOfWeekLocalDataSource.insertTodoDayOfWeek(
                TodoDayOfWeekDto(
                    templateId = templateId,
                    dayOfWeeks = dayOfWeek,
                    dayOfWeek = week
                )
            )
        }

        if(todo.time != null && todo.alarmType != AlarmType.OFF){
            val today = LocalDate.now()
            val now = LocalTime.now()

            val todoTime = LocalTime.of(todo.time!!.hour, todo.time!!.minute) // ⏰ 시간 조합
            val isTimePassed = now > todoTime

            val startDayOffset = if (isTimePassed) 1 else 0

            val alarmDate = (startDayOffset..6).map { offset ->
                today.plusDays(offset.toLong())
            }.first { date ->
                dayOfWeek.contains(date.dayOfWeek.value)
            }

            alarmScheduler.schedule(
                date = alarmDate,
                time = todo.time!!,
                templateId = templateId
            )
        }
    }

    override suspend fun updateDayOfWeekTodo(todo: TodoModel) {
        val instanceTodo = todoInstanceLocalDataSource.getTodoInstanceById(todo.instanceId) ?: return
        val templateId = instanceTodo.templateId

        todoTemplateLocalDataSource.updateTodoTemplate(
            TodoTemplateDto(
                id = templateId,
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoTypeDto.DAY_OF_WEEK,
                alarmType = todo.alarmType.toDto(),
                isAlarmHasVibration = todo.isAlarmHasVibration,
                isAlarmHasSound = todo.isAlarmHasSound
            )
        )

        // 2. 기존 요일 조회
        val existingDayOfWeeks = todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(templateId)
        val existingDaysSet = existingDayOfWeeks.map { it.dayOfWeek }.toSet()
        val newDaysSet = todo.dayOfWeeks!!.toSet()

        // 3. 삭제할 요일
        val daysToDelete = existingDaysSet - newDaysSet
        if (daysToDelete.isNotEmpty()) {
            todoDayOfWeekLocalDataSource.deleteSpecificDayOfWeeks(templateId, daysToDelete.toList())
            todoDayOfWeekLocalDataSource.deleteInstancesByTemplateIdAndDaysOfWeek(templateId, daysToDelete.toList())
        }

        // 4. 추가할 요일
        val daysToAdd = newDaysSet - existingDaysSet
        val newDayOfWeekDtos = daysToAdd.map { dayOfWeek ->
            TodoDayOfWeekDto(
                templateId = templateId,
                dayOfWeeks = todo.dayOfWeeks!!,
                dayOfWeek = dayOfWeek
            )
        }
        todoDayOfWeekLocalDataSource.insertDayOfWeekTodos(newDayOfWeekDtos)

        alarmScheduler.cancel(templateId)

        if(todo.time != null && todo.alarmType != AlarmType.OFF){
            val today = LocalDate.now()
            val now = LocalTime.now()

            val todoTime = LocalTime.of(todo.time!!.hour, todo.time!!.minute) // ⏰ 시간 조합
            val isTimePassed = now > todoTime

            val startDayOffset = if (isTimePassed) 1 else 0

            val alarmDate = (startDayOffset..6).map { offset ->
                today.plusDays(offset.toLong())
            }.first { date ->
                todo.dayOfWeeks!!.contains(date.dayOfWeek.value)
            }

            alarmScheduler.schedule(
                date = alarmDate,
                time = todo.time!!,
                templateId = templateId
            )
        }
    }
}