package com.paraooo.data.repository

import android.util.Log
import androidx.room.Transaction
import com.paraooo.data.datasource.TodoLocalDataSource
import com.paraooo.data.dto.TodoDto
import com.paraooo.data.local.dao.TodoDao
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
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.util.UUID

const val TAG = "PARAOOO"

class TodoRepositoryImpl(
//    private val todoLocalDataSource: TodoLocalDataSource,
    private val todoDao : TodoDao
) : TodoRepository {

    //    override suspend fun getTodoByDate(date: Long): List<TodoModel> {
//        return todoLocalDataSource.getTodoByDate(date).map { it.toModel() }
//    }
//
//    override suspend fun postTodo(todo: TodoModel) {
//        todoLocalDataSource.insertTodo(todo = todo.toDto())
//    }
//
//    override suspend fun updateTodoProgress(todoId: Int, progress: Float) {
//        todoLocalDataSource.updateTodoProgress(todoId = todoId, progress = progress)
//    }
//
//    override suspend fun deleteTodoById(todoId: Int) {
//        todoLocalDataSource.deleteTodoById(todoId = todoId)
//    }
//
//    override suspend fun updateTodo(todo: TodoModel) {
//        todoLocalDataSource.updateTodo(todo = todo.toDto())
//    }
//
//    override suspend fun findTodoById(todoId: Int): TodoModel {
//        return todoLocalDataSource.findTodoById(todoId).toModel()
//    }
//
//    override suspend fun postPeriodTodo(
//        todo : TodoModel,
//        startDate : LocalDate,
//        endDate : LocalDate
//    ) {
//
//        val groupId = UUID.randomUUID().toString()
//        val todos = mutableListOf<TodoModel>()
//
//        var currentDate = startDate
//        while (currentDate <= endDate) {
//            todos.add(
//                todo.copy(
//                    date = currentDate,
//                    groupId = groupId,
//                    startDate = startDate,
//                    endDate = endDate
//                )
//            )
//            currentDate = currentDate.plusDays(1)  // 하루씩 증가
//        }
//
//        todoLocalDataSource.insertTodos(todos.map { it.toDto() })
//    }
//
//    @Transaction
//    override suspend fun updatePeriodTodo(todo: TodoModel) {
//
//        Log.d(TAG, "updatePeriodTodo: ${todo}")
//
//        val groupId = todo.groupId ?: return
//        val selectedTodo = todo.toDto()
//
//        Log.d(TAG, "updatePeriodTodo: ${selectedTodo}")
//
//        if (selectedTodo.startDate!! > selectedTodo.endDate!!) return
//
//        val existingTodos = todoLocalDataSource.getTodosByGroupId(groupId)
//
//        val todosToDelete = existingTodos.filter { it.date !in selectedTodo.startDate..selectedTodo.endDate }
//        if (todosToDelete.isNotEmpty()) {
//            todoLocalDataSource.deleteTodos(todosToDelete)
//        }
//
//        val todosToUpdate = mutableListOf<TodoDto>()
//        val todosToInsert = mutableListOf<TodoDto>()
//
//        for (date in selectedTodo.startDate..selectedTodo.endDate step 24 * 60 * 60 * 1000) {
//            val existingTodo = existingTodos.find { it.date == date }
//
//            if (existingTodo != null) {
//                todosToUpdate.add(
//                    selectedTodo.copy(
//                        id = existingTodo.id,
//                        progressAngle = existingTodo.progressAngle,
//                        date = date
//                    )
//                )
//            } else {
//                todosToInsert.add(
//                    selectedTodo.copy(
//                        id = 0,
//                        date = date
//                    )
//                )
//            }
//        }
//
//        if (todosToUpdate.isNotEmpty()) {
//            todoLocalDataSource.updateTodos(todosToUpdate)
//        }
//        if (todosToInsert.isNotEmpty()) {
//            todoLocalDataSource.insertTodos(todosToInsert)
//        }
//    }
//
//    override suspend fun deletePeriodTodo(groupId : String) {
//        todoLocalDataSource.deleteTodosByGroupId(groupId)
//    }


    override suspend fun getTodoByDate(date: Long): List<TodoModel> {

        val instances = todoDao.getTodosByDate(date)
        val periodTodos = todoDao.getPeriodTodosByDate(date)

        val sortedList = (instances + periodTodos).sortedWith(compareBy({ it.hour }, { it.minute }))

        return sortedList.map { it.toModel() }
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
            progressAngle = instance.progressAngle
        )
    }

    override suspend fun postPeriodTodo(todo: TodoModel, startDate: LocalDate, endDate: LocalDate) {
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
            currentDate = currentDate.plusDays(1)  // 하루씩 증가
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

//    override suspend fun postPeriodTodo(todo: TodoModel, startDate: LocalDate, endDate: LocalDate) {
//        TODO("Not yet implemented")
//    }
//
    override suspend fun updatePeriodTodo(todo: TodoModel) {

        val instanceTodo = todoDao.getTodoInstanceById(todo.instanceId)

        val startDate = transferLocalDateToMillis(todo.startDate)
        val endDate = transferLocalDateToMillis(todo.endDate)

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

        // 기존 TodoInstance를 날짜 기준으로 저장 (progressAngle 유지)
        val existingDateMap = existingInstances.associate { it.date to it.progressAngle }

        val newDates = (startDate..endDate).toSet()
        val oldDates = existingDateMap.keys

        val datesToDelete = oldDates - newDates // 기존에 있었지만, 새 범위에 포함되지 않는 날짜
        val datesToAdd = newDates - oldDates // 새 범위에 포함되지만, 기존에 없던 날짜

        // 삭제
        todoDao.deleteInstancesByDates(instanceTodo.templateId, datesToDelete)

        // 추가
        val newInstances = datesToAdd.map { date ->
            TodoInstance(templateId = instanceTodo.templateId, date = date, progressAngle = 0F)
        }
        todoDao.insertInstances(newInstances)

    }
//
//    override suspend fun deletePeriodTodo(groupId: String) {
//        TODO("Not yet implemented")
//    }
}