package com.paraooo.data.repository

import android.util.Log
import androidx.room.Transaction
import com.paraooo.data.datasource.TodoLocalDataSource
import com.paraooo.data.dto.TodoDto
import com.paraooo.data.local.dao.TodoDao
import com.paraooo.data.local.entity.TodoDayOfWeek
import com.paraooo.data.local.entity.TodoInstance
import com.paraooo.data.local.entity.TodoPeriod
import com.paraooo.data.local.entity.TodoTemplate
import com.paraooo.data.local.entity.TodoType
//import com.paraooo.data.local.entity.TodoEntity
//import com.paraooo.data.mapper.toDto
//import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.UUID

const val TAG = "PARAOOO"

class TodoRepositoryImpl(
//    private val todoLocalDataSource: TodoLocalDataSource,
    private val todoDao : TodoDao
) : TodoRepository {

    override suspend fun getTodoByDate(date: Long): List<TodoModel> {

        val instances = todoDao.getTodosByDate(date)
        val dayOfWeekTemplates = todoDao.getDayOfWeekTodoTemplatesByDate(date)

        val instanceTemplateIds = instances.map { it.templateId }.toSet()
        val filteredDayOfWeekTemplates = dayOfWeekTemplates.filterNot { instanceTemplateIds.contains(it.id) }

        for (template in filteredDayOfWeekTemplates) {
            todoDao.insertTodoInstance(
                TodoInstance(
                    templateId = template.id,
                    date = date
                )
            )
        }

        val newInstances = todoDao.getTodosByDate(date)

        return newInstances
            .sortedWith(compareBy({ it.hour ?: Int.MAX_VALUE }, { it.minute ?: Int.MAX_VALUE }))
            .map { it.toModel() }
    }

    override suspend fun postTodo(todo: TodoModel) {

        val todoTemplate = TodoTemplate(
            title = todo.title,
            description = todo.description ?: "",
            hour = todo.time?.hour,
            minute = todo.time?.minute,
            type = TodoType.GENERAL
        )

        val templateId = todoDao.insertTodoTemplate(todoTemplate)

        todoDao.insertTodoInstance(
            TodoInstance(
                templateId = templateId,
                date = transferLocalDateToMillis(todo.date)
            )
        )
    }

    override suspend fun updateTodoProgress(instanceId: Long, progress: Float) {
        todoDao.updateTodoProgress(instanceId, progress)
    }

    override suspend fun deleteTodoById(instanceId: Long) {

        val instanceTodo = todoDao.getTodoInstanceById(instanceId)

        todoDao.deleteTodoTemplate(instanceTodo!!.templateId)
    }

    override suspend fun updateTodo(todo: TodoModel) {

        val instanceTodo = todoDao.getTodoInstanceById(todo.instanceId)

        todoDao.updateTodoTemplate(
            TodoTemplate(
                id = instanceTodo!!.templateId,
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoType.GENERAL
            )
        )

        todoDao.updateTodoInstance(
            TodoInstance(
                id = todo.instanceId,
                templateId = instanceTodo.templateId,
                date = transferLocalDateToMillis(todo.date),
                progressAngle = todo.progressAngle
            )
        )
    }

    override suspend fun findTodoById(instanceId: Long): TodoModel {
        val instance = todoDao.getTodoInstanceById(instanceId)
        val template = todoDao.getTodoTemplateById(instance!!.templateId)

        val period = todoDao.getTodoPeriodByTemplateId(instance.templateId)
        val dayOfWeek = todoDao.getDayOfWeekByTemplateId(instance.templateId)

        return TodoModel(
            instanceId = instance.id,
            title = template!!.title,
            description = template.description,
            date = transferMillis2LocalDate(instance.date),
            time = if (template.hour != null && template.minute != null) {
                com.paraooo.domain.model.Time(template.hour, template.minute)
            } else {
                null
            },
            progressAngle = instance.progressAngle,
            startDate = period?.startDate?.let { transferMillis2LocalDate(it) },
            endDate = period?.endDate?.let { transferMillis2LocalDate(it) },
            dayOfWeeks = dayOfWeek?.map { it.dayOfWeek }
        )
    }

    override suspend fun postPeriodTodo(todo: TodoModel, startDate: LocalDate, endDate: LocalDate) {

        withContext(Dispatchers.IO){
            Log.d(TAG, "postPeriodTodo: ${todo}")

            val todoTemplate = TodoTemplate(
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoType.PERIOD
            )

            val templateId = todoDao.insertTodoTemplate(todoTemplate)
            val todos = mutableListOf<TodoInstance>()

            var currentDate = startDate
            while (currentDate <= endDate) {
                todos.add(
                    TodoInstance(
                        templateId = templateId,
                        date = transferLocalDateToMillis(currentDate)
                    )
                )
                currentDate = currentDate.plusDays(1)
            }

            todoDao.insertTodoInstances(todos)

            todoDao.insertTodoPeriod(
                TodoPeriod(
                    templateId = templateId,
                    startDate = transferLocalDateToMillis(startDate),
                    endDate = transferLocalDateToMillis(endDate)
                )
            )
        }
    }

    override suspend fun updatePeriodTodo(todo: TodoModel) {

        val instanceTodo = todoDao.getTodoInstanceById(todo.instanceId)

        todoDao.updateTodoTemplate(
            TodoTemplate(
                id = instanceTodo!!.templateId,
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoType.PERIOD
            )
        )

        val existingInstances = todoDao.getInstancesByTemplateId(instanceTodo.templateId)

        val existingDateMap = existingInstances.associate { it.date to it.progressAngle }

        todoDao.updateTodoPeriod(
            TodoPeriod(
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

        todoDao.deleteInstancesByDates(instanceTodo.templateId, datesToDelete)

        val newInstances = datesToAdd.map { date ->
            TodoInstance(templateId = instanceTodo.templateId, date = date, progressAngle = 0F)
        }

        todoDao.insertInstances(newInstances)

    }

    override suspend fun postDayOfWeekTodo(todo: TodoModel, dayOfWeek: List<Int>) {

        val todoTemplate = TodoTemplate(
            title = todo.title,
            description = todo.description ?: "",
            hour = todo.time?.hour,
            minute = todo.time?.minute,
            type = TodoType.PERIOD
        )

        val templateId = todoDao.insertTodoTemplate(todoTemplate)

        for (week in dayOfWeek) {
            todoDao.insertTodoDayOfWeek(
                TodoDayOfWeek(
                    templateId = templateId,
                    dayOfWeeks = dayOfWeek,
                    dayOfWeek = week
                )
            )
        }
    }

    override suspend fun updateDayOfWeekTodo(todo: TodoModel) {
        val instanceTodo = todoDao.getTodoInstanceById(todo.instanceId) ?: return
        val templateId = instanceTodo.templateId

        // 1. 템플릿 업데이트
        todoDao.updateTodoTemplate(
            TodoTemplate(
                id = templateId,
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoType.DAY_OF_WEEK
            )
        )

        // 2. 기존 요일 조회
        val existingDayOfWeeks = todoDao.getDayOfWeekByTemplateId(templateId)
        val existingDaysSet = existingDayOfWeeks!!.map { it.dayOfWeek }.toSet()
        val newDaysSet = todo.dayOfWeeks!!.toSet()

        // 3. 삭제할 요일
        val daysToDelete = existingDaysSet - newDaysSet
        if (daysToDelete.isNotEmpty()) {
            todoDao.deleteSpecificDayOfWeeks(templateId, daysToDelete.toList())
            todoDao.deleteInstancesByTemplateIdAndDaysOfWeek(templateId, daysToDelete.toList())

        }

        // 4. 추가할 요일
        val daysToAdd = newDaysSet - existingDaysSet
        val newDayOfWeekEntities = daysToAdd.map { dayOfWeek ->
            TodoDayOfWeek(
                templateId = templateId,
                dayOfWeeks = todo.dayOfWeeks!!,
                dayOfWeek = dayOfWeek
            )
        }
        todoDao.insertDayOfWeekTodos(newDayOfWeekEntities)
    }
}