package com.paraooo.domain.usecase.alarm

import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.repository.TodoRepository

class ScheduleAlarmsUseCase(
    private val todoRepository: TodoRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(alarmSchedules: List<AlarmSchedule>) {
        alarmSchedules.forEach {
            alarmScheduler.schedule(
                it.date,
                it.time,
                it.templateId
            )
        }
    }
}
