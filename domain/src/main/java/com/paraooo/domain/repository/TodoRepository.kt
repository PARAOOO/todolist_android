package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoPeriodModel
import com.paraooo.domain.model.TodoTemplateModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class FindTodoByIdResponse(
    val todoInstance : TodoInstanceModel,
    val todoTemplate : TodoTemplateModel,
    val todoPeriod: TodoPeriodModel?,
    val todoDayOfWeek : List<TodoDayOfWeekModel>?
)
interface TodoRepository {

    suspend fun getTodoInstanceById(instanceId: Long) : TodoInstanceModel?

    suspend fun findTodoById(instanceId : Long) : FindTodoByIdResponse

    suspend fun postTodo(todoTemplate: TodoTemplateModel, todoInstance : TodoInstanceModel) : Long

    suspend fun updateTodo(todoTemplate: TodoTemplateModel, todoInstance: TodoInstanceModel)

    suspend fun updateTodoProgress(todoInstanceId: Long, progressAngle: Float)

    suspend fun deleteTodoTemplate(templateId: Long)

    suspend fun syncDayOfWeekInstance(todoInstances: List<TodoInstanceModel>)

    suspend fun observeTodosByDate(date: Long) : Flow<List<TodoModel>>
}