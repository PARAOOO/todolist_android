package com.paraooo.data.repository

import android.util.Log
import com.paraooo.data.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.data.datasource.TodoInstanceLocalDataSource
import com.paraooo.data.datasource.TodoPeriodLocalDataSource
import com.paraooo.data.datasource.TodoTemplateLocalDataSource
import com.paraooo.data.dto.TodoInstanceDto
import com.paraooo.data.mapper.toModel
import com.paraooo.data.platform.alarm.AlarmScheduler
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoReadRepository
import com.paraooo.domain.util.transferMillis2LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformLatest

class TodoReadRepositoryImpl(
    private val todoTemplateLocalDataSource : TodoTemplateLocalDataSource,
    private val todoInstanceLocalDataSource : TodoInstanceLocalDataSource,
    private val todoPeriodLocalDataSource : TodoPeriodLocalDataSource,
    private val todoDayOfWeekLocalDataSource : TodoDayOfWeekLocalDataSource,
) : TodoReadRepository {

    override suspend fun getTodoByDate(date: Long): Flow<List<TodoModel>> {

        return todoTemplateLocalDataSource.observeTodosByDate(date)
            .transformLatest { currentList ->
                // insert가 일어날 수도 있는 타이밍
                val existingTemplateIds = currentList.map { it.templateId }.toSet()
                val dayOfWeekTemplates = todoDayOfWeekLocalDataSource.getDayOfWeekTodoTemplatesByDate(date)
                val newTemplates = dayOfWeekTemplates.filterNot { existingTemplateIds.contains(it.id) }

                Log.d(TAG, "getTodoByDate: ${currentList}")
                
                if (newTemplates.isNotEmpty()) {
                    for (template in newTemplates) {
                        todoInstanceLocalDataSource.insertTodoInstance(
                            TodoInstanceDto(templateId = template.id, date = date)
                        )
                    }
                    // insert 이후 DB가 갱신되면 observeTodosByDate가 다시 emit 하니까
                    // 이번 emit은 무시 (return)
                    return@transformLatest
                }

                // insert가 없어서 변화도 없으면 그냥 이걸 emit
                emit(currentList
                    .sortedWith(compareBy({ it.hour ?: Int.MAX_VALUE }, { it.minute ?: Int.MAX_VALUE }))
                    .map { it.toModel() })
            }

//        val todos = todoTemplateLocalDataSource.getTodosByDate(date)
//        val dayOfWeekTemplates = todoDayOfWeekLocalDataSource.getDayOfWeekTodoTemplatesByDate(date)
//
//        val templateIds = todos.map { it.templateId }.toSet()
//        val filteredDayOfWeekTemplates = dayOfWeekTemplates.filterNot { templateIds.contains(it.id) }
//
//        for (template in filteredDayOfWeekTemplates) {
//            todoInstanceLocalDataSource.insertTodoInstance(
//                TodoInstanceDto(
//                    templateId = template.id,
//                    date = date
//                )
//            )
//        }
//
//        val newInstances = todoTemplateLocalDataSource.observeTodosByDate(date)
//
//        return newInstances.map { list ->
//            list.sortedWith(compareBy({ it.hour ?: Int.MAX_VALUE }, { it.minute ?: Int.MAX_VALUE }))
//                .map { it.toModel() }
//        }
    }

    override suspend fun findTodoById(instanceId: Long): TodoModel {
        val instance = todoInstanceLocalDataSource.getTodoInstanceById(instanceId)
        val template = todoTemplateLocalDataSource.getTodoTemplateById(instance!!.templateId)
        val period = todoPeriodLocalDataSource.getTodoPeriodByTemplateId(instance.templateId)
        val dayOfWeek = todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(instance.templateId).takeIf { it.isNotEmpty() }

        return TodoModel(
            instanceId = instance.id,
            title = template!!.title,
            description = template.description,
            date = transferMillis2LocalDate(instance.date),
            time = if (template.hour != null && template.minute != null) {
                Time(template.hour, template.minute)
            } else {
                null
            },
            alarmType = template.alarmType.toModel(),
            progressAngle = instance.progressAngle,
            startDate = period?.startDate?.let { transferMillis2LocalDate(it) },
            endDate = period?.endDate?.let { transferMillis2LocalDate(it) },
            dayOfWeeks = dayOfWeek?.map { it.dayOfWeek },
            isAlarmHasVibration = template.isAlarmHasVibration,
            isAlarmHasSound = template.isAlarmHasSound
        )
    }
}