package com.paraooo.domain.usecase.alarm

import com.paraooo.domain.repository.TodoRepository

class ScheduleAlarmsUseCase(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(date: Long) {


    }
}
