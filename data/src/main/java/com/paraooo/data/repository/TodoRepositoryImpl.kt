package com.paraooo.data.repository

import android.util.Log
import androidx.room.Transaction
import com.paraooo.data.datasource.TodoLocalDataSource
import com.paraooo.data.dto.TodoDto
import com.paraooo.data.local.entity.TodoEntity
import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoRepository
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.util.UUID

const val TAG = "PARAOOO"

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

    override suspend fun postPeriodTodo(
        todo : TodoModel,
        startDate : LocalDate,
        endDate : LocalDate
    ) {

        val groupId = UUID.randomUUID().toString()
        val todos = mutableListOf<TodoModel>()

        var currentDate = startDate
        while (currentDate <= endDate) {
            todos.add(
                todo.copy(
                    date = currentDate,
                    groupId = groupId,
                    startDate = startDate,
                    endDate = endDate
                )
            )
            currentDate = currentDate.plusDays(1)  // 하루씩 증가
        }

        todoLocalDataSource.insertTodos(todos.map { it.toDto() })
    }

    @Transaction
    override suspend fun updatePeriodTodo(todo: TodoModel) {

        Log.d(TAG, "updatePeriodTodo: ${todo}")

        val groupId = todo.groupId ?: return
        val selectedTodo = todo.toDto()

        Log.d(TAG, "updatePeriodTodo: ${selectedTodo}")

        if (selectedTodo.startDate!! > selectedTodo.endDate!!) return

        val existingTodos = todoLocalDataSource.getTodosByGroupId(groupId)

        val todosToDelete = existingTodos.filter { it.date !in selectedTodo.startDate..selectedTodo.endDate }
        if (todosToDelete.isNotEmpty()) {
            todoLocalDataSource.deleteTodos(todosToDelete)
        }

        val todosToUpdate = mutableListOf<TodoDto>()
        val todosToInsert = mutableListOf<TodoDto>()

        for (date in selectedTodo.startDate..selectedTodo.endDate step 24 * 60 * 60 * 1000) {
            val existingTodo = existingTodos.find { it.date == date }

            if (existingTodo != null) {
                todosToUpdate.add(
                    selectedTodo.copy(
                        id = existingTodo.id,
                        progressAngle = existingTodo.progressAngle,
                        date = date
                    )
                )
            } else {
                todosToInsert.add(
                    selectedTodo.copy(
                        id = 0,
                        date = date
                    )
                )
            }
        }

        if (todosToUpdate.isNotEmpty()) {
            todoLocalDataSource.updateTodos(todosToUpdate)
        }
        if (todosToInsert.isNotEmpty()) {
            todoLocalDataSource.insertTodos(todosToInsert)
        }
    }

    override suspend fun deletePeriodTodo(groupId : String) {
        todoLocalDataSource.deleteTodosByGroupId(groupId)
    }
}