package com.paraooo.data.datasource

import com.paraooo.data.dto.TodoDto
import com.paraooo.data.local.dao.TodoDao
import com.paraooo.data.local.database.TodoDatabase
//import com.paraooo.data.local.entity.TodoEntity
//import com.paraooo.data.mapper.toDto
//import com.paraooo.data.mapper.toEntity
import kotlinx.coroutines.delay

class TodoLocalDataSource(
    private val todoDao : TodoDao
) {

//    suspend fun getTodoByDate(date : Long) : List<TodoDto> {
//        return todoDao.getTodoByDate(date).map { it.toDto() }
//    }
//
//    suspend fun insertTodo(todo : TodoDto) {
//        todoDao.insertTodo(todo.toEntity())
//    }
//
//    suspend fun updateTodoProgress(todoId : Int, progress : Float) {
//        todoDao.updateTodoProgress(todoId, progress)
//    }
//
//    suspend fun deleteTodoById(todoId : Int) {
//        todoDao.deleteTodoById(todoId)
//    }
//
//    suspend fun updateTodo(todo : TodoDto) {
//        todoDao.updateTodo(todo.toEntity())
//    }
//
//    suspend fun findTodoById(todoId : Int) : TodoDto {
//        return todoDao.findTodoById(todoId).toDto()
//    }
//
//    suspend fun insertTodos(todos : List<TodoDto>) {
//        todoDao.insertTodos(todos.map { it.toEntity() })
//    }
//
//    suspend fun getTodosByGroupId(groupId : String) : List<TodoDto> {
//        return todoDao.getTodosByGroupId(groupId).map { it.toDto() }
//    }
//
//    suspend fun deleteTodos(todos : List<TodoDto>) {
//        todoDao.deleteTodos(todos.map { it.toEntity() })
//    }
//
//    suspend fun updateTodos(todos : List<TodoDto>) {
//        todoDao.updatedTodos(todos.map { it.toEntity() })
//    }
//
//    suspend fun deleteTodosByGroupId(groupId : String) {
//        todoDao.deleteTodosByGroupId(groupId)
//    }

}