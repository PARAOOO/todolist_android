package com.paraooo.data.repository

import android.util.Log
import com.paraooo.data.datasource.TodoLocalDataSource
import com.paraooo.data.local.entity.TodoEntity
import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoRepository
import kotlinx.coroutines.delay

class TodoRepositoryImpl(
    private val todoLocalDataSource: TodoLocalDataSource
) : TodoRepository {

    override suspend fun getTodoByDate(date: Long): List<TodoModel> {
        return todoLocalDataSource.getTodoByDate(date).map { it.toModel() }
    }

    override suspend fun postTodo(todo: TodoModel) {
        todoLocalDataSource.insertTodo(todo = todo.toDto())
    }

    override suspend fun updateTodoProgress(todoId: Int, progress: Float) {
        todoLocalDataSource.updateTodoProgress(todoId = todoId, progress = progress)
    }

    override suspend fun deleteTodoById(todoId: Int) {
        todoLocalDataSource.deleteTodoById(todoId = todoId)
    }

    override suspend fun updateTodo(todo: TodoModel) {
        todoLocalDataSource.updateTodo(todo = todo.toDto())
    }

    override suspend fun findTodoById(todoId: Int): TodoModel {
        return todoLocalDataSource.findTodoById(todoId).toModel()
    }
}