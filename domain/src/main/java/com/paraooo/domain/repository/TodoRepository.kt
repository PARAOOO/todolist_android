package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoModel
import java.time.LocalDate

interface TodoRepository {

    suspend fun getTodoByDate(date : Long) : List<TodoModel>

    suspend fun postTodo(todo : TodoModel)

    suspend fun updateTodoProgress(todoId : Int, progress : Float)

    suspend fun deleteTodoById(todoId : Int)

    suspend fun updateTodo(todo : TodoModel)

    suspend fun findTodoById(todoId : Int) : TodoModel

    suspend fun postPeriodTodo(
        todo : TodoModel,
        startDate : LocalDate,
        endDate : LocalDate
    )

    suspend fun updatePeriodTodo(todo: TodoModel)

    suspend fun deletePeriodTodo(groupId : String)
}