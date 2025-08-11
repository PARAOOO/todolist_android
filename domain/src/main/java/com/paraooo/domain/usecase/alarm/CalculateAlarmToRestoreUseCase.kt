package com.paraooo.domain.usecase.alarm

import com.paraooo.domain.repository.TodoRepository

class CalculateAlarmToRestoreUseCase(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(date: Long) {


    }
}
