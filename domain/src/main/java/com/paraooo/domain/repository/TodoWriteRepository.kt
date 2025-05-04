package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoModel
import java.time.LocalDate

interface TodoWriteRepository {

//    suspend fun getTodoByDate(date : Long) : List<TodoModel>

    suspend fun postTodo(todo : TodoModel)

    suspend fun updateTodoProgress(instanceId: Long, progress : Float)

    suspend fun deleteTodoById(instanceId: Long)

    suspend fun updateTodo(todo : TodoModel)

//    suspend fun findTodoById(instanceId : Long) : TodoModel

    suspend fun postPeriodTodo(
        todo : TodoModel,
        startDate : LocalDate,
        endDate : LocalDate
    )

    suspend fun updatePeriodTodo(todo: TodoModel)

    suspend fun postDayOfWeekTodo(
        todo: TodoModel,
        dayOfWeek : List<Int>
    )

    suspend fun updateDayOfWeekTodo(todo: TodoModel)

}