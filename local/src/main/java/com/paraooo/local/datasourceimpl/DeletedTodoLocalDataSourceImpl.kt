package com.paraooo.local.datasourceimpl

import com.paraooo.local.dao.DeletedTodoDao
import com.paraooo.local.datasource.DeletedTodoLocalDataSource
import com.paraooo.local.entity.DeletedTodo
import java.util.UUID

class DeletedTodoLocalDataSourceImpl(
    private val deletedTodoDao: DeletedTodoDao
): DeletedTodoLocalDataSource {
//    override suspend fun insert(deletedTodo: UUID) {
//        deletedTodoDao.insert(DeletedTodo(deletedTodo, ""))
//    }

    override suspend fun getDeletedTemplates(): List<DeletedTodo> {
        return deletedTodoDao.getDeletedTemplates()
    }

    override suspend fun getDeletedInstances(): List<DeletedTodo> {
        return deletedTodoDao.getDeletedInstances()
    }

    override suspend fun deleteByIds(ids: List<UUID>) {
        deletedTodoDao.deleteByIds(ids)
    }
}