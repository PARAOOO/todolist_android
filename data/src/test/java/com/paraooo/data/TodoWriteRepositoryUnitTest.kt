package com.paraooo.data

import android.util.Log
import com.paraooo.data.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.data.datasource.TodoInstanceLocalDataSource
import com.paraooo.data.datasource.TodoPeriodLocalDataSource
import com.paraooo.data.datasource.TodoTemplateLocalDataSource
import com.paraooo.data.dto.AlarmTypeDto
import com.paraooo.data.dto.TodoDayOfWeekDto
import com.paraooo.data.dto.TodoDto
import com.paraooo.data.dto.TodoInstanceDto
import com.paraooo.data.dto.TodoPeriodDto
import com.paraooo.data.dto.TodoTemplateDto
import com.paraooo.data.dto.TodoTypeDto
import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toModel
import com.paraooo.data.platform.alarm.AlarmScheduler
import com.paraooo.data.repository.TodoReadRepositoryImpl
import com.paraooo.data.repository.TodoWriteRepositoryImpl
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoReadRepository
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class TodoWriteRepositoryUnitTest {

    private val todoTemplateLocalDataSource = mockk<TodoTemplateLocalDataSource>()
    private val todoInstanceLocalDataSource = mockk<TodoInstanceLocalDataSource>()
    private val todoPeriodLocalDataSource = mockk<TodoPeriodLocalDataSource>()
    private val todoDayOfWeekLocalDataSource = mockk<TodoDayOfWeekLocalDataSource>()
    private val alarmScheduler = mockk<AlarmScheduler>(relaxed = true) // alarm 호출 무시
    private val widgetUpdater = mockk<WidgetUpdater>(relaxed = true)

    private val sampleTodoModel = TodoModel(
        instanceId = 1L,
        title = "sample title",
        time = null,
        progressAngle = 0F,
        date = LocalDate.of(2000, 1, 1),
        description = "sample description",
        alarmType = AlarmType.OFF,
        isAlarmHasVibration = false,
        isAlarmHasSound = false
    )

    private val sampleTodoDto = TodoDto(
        templateId = 1L,
        instanceId = 1L,
        title = "sample title",
        hour = null,
        minute = null,
        progressAngle = 0F,
        date = transferLocalDateToMillis(LocalDate.of(2000, 1, 1)),
        description = "sample description",
        alarmType = AlarmTypeDto.OFF,
        isAlarmHasVibration = false,
        isAlarmHasSound = false
    )

    private val sampleTodoTemplateDto = TodoTemplateDto(
        id = 1L,
        title = "sample title",
        hour = null,
        minute = null,
        description = "sample description",
        type = TodoTypeDto.GENERAL,
        alarmType = AlarmTypeDto.OFF,
        isAlarmHasVibration = false,
        isAlarmHasSound = false
    )

    private val sampleTodoInstanceDto = TodoInstanceDto(
        id = 1L,
        templateId = 1L,
        date = transferLocalDateToMillis(LocalDate.of(2000, 1, 1)),
        progressAngle = 0F
    )

    private val sampleTodoPeriodDto = TodoPeriodDto(
        templateId = 1L,
        startDate = transferLocalDateToMillis(LocalDate.of(2000, 1, 1)),
        endDate = transferLocalDateToMillis(LocalDate.of(2000, 1, 2))
    )

    private val sampleTodoDayOfWeekDto = TodoDayOfWeekDto(
        templateId = 1L,
        dayOfWeek = 1,
        dayOfWeeks = listOf(1, 2)
    )

    private lateinit var todoWriteRepository: TodoWriteRepositoryImpl
    private lateinit var todoReadRepository: TodoReadRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        todoWriteRepository = TodoWriteRepositoryImpl(
            todoTemplateLocalDataSource,
            todoInstanceLocalDataSource,
            todoPeriodLocalDataSource,
            todoDayOfWeekLocalDataSource,
            alarmScheduler,
            widgetUpdater
        )

        todoReadRepository = TodoReadRepositoryImpl(
            todoTemplateLocalDataSource,
            todoInstanceLocalDataSource,
            todoPeriodLocalDataSource,
            todoDayOfWeekLocalDataSource
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTodoByDate inserts only missing templates and returns sorted list`() = runTest {
        // Given
        val date = transferLocalDateToMillis(LocalDate.of(2025, 4, 22))

        val existingInstance = sampleTodoDto.copy(
            templateId = 1L,
            title = "Existing Instance",
            date = date,
            hour = 11,
            minute = 20
        )

        val updatedInstance = sampleTodoDto.copy(
            templateId = 2L,
            title = "Updated Instance",
            date = date,
            hour = 12,
            minute = 30
        )

        val existingInstances = listOf(
            existingInstance
        )

        val dayOfWeekTemplates = listOf(
            sampleTodoTemplateDto.copy(
                id = existingInstance.templateId,
                title = existingInstance.title,
                hour = existingInstance.hour,
                minute = existingInstance.minute
            ),
            sampleTodoTemplateDto.copy(
                id = updatedInstance.templateId,
                title = updatedInstance.title,
                hour = updatedInstance.hour,
                minute = updatedInstance.minute
            )
        )

        val updatedInstances = listOf(
            existingInstance,
            updatedInstance
        )

        // When
        coEvery { todoTemplateLocalDataSource.getTodosByDate(date) } returnsMany listOf(
            existingInstances,
            updatedInstances
        )
        coEvery { todoTemplateLocalDataSource.observeTodosByDate(date) } returns flowOf(
            existingInstances,
            updatedInstances
        )
        coEvery { todoDayOfWeekLocalDataSource.getDayOfWeekTodoTemplatesByDate(date) } returns dayOfWeekTemplates
        coEvery { todoInstanceLocalDataSource.insertTodoInstance(any()) } just Runs

        // Then
        val result = todoReadRepository.getTodoByDate(date).first()

        coVerify(exactly = 1) {
            todoInstanceLocalDataSource.insertTodoInstance(
                match { it.templateId == updatedInstance.templateId && it.date == date }
            )
        }

        // 리턴 결과가 정렬되어 있는지
        assertEquals(
            result.map { it.title },
            listOf( existingInstance.title,updatedInstance.title)
        )
    }

    @Test
    fun `postTodo should insert template and instance`() = runTest {

        val todo = sampleTodoModel.copy(
            date = LocalDate.of(2000, 1, 1),
            time = Time(
                hour = 22, minute = 30
            ),
            alarmType = AlarmType.NOTIFY
        )

        coEvery { todoTemplateLocalDataSource.insertTodoTemplate(any()) } returns 1L
        coEvery { todoInstanceLocalDataSource.insertTodoInstance(any()) } just Runs
        every { alarmScheduler.schedule(any(), any(), any()) } just Runs

        todoWriteRepository.postTodo(todo)

        coVerify { todoTemplateLocalDataSource.insertTodoTemplate(any()) }
        coVerify { todoInstanceLocalDataSource.insertTodoInstance(any()) }
        coVerify { alarmScheduler.schedule(todo.date, todo.time!!, 1L) }
    }

    @Test
    fun `postTodo should not schedule alarm if alarmType is OFF`() = runTest {
        val todo = sampleTodoModel.copy(
            time = Time(
                hour = 22, minute = 30
            ),
            alarmType = AlarmType.OFF
        )

        coEvery { todoTemplateLocalDataSource.insertTodoTemplate(any()) } returns 1L
        coEvery { todoInstanceLocalDataSource.insertTodoInstance(any()) } just Runs
        every { alarmScheduler.schedule(any(), any(), any()) } just Runs

        todoWriteRepository.postTodo(todo)

        coVerify { todoTemplateLocalDataSource.insertTodoTemplate(any()) }
        coVerify { todoInstanceLocalDataSource.insertTodoInstance(any()) }
        coVerify(exactly = 0) { alarmScheduler.schedule(any(), any(), any()) }

    }

    @Test
    fun `updateTodoProgress should update progress angle`() = runTest {
        val instanceId = 1L
        val progress = 0.5F

        coEvery { todoInstanceLocalDataSource.updateTodoProgress(instanceId, progress) } just Runs

        todoWriteRepository.updateTodoProgress(instanceId, progress)

        coVerify { todoInstanceLocalDataSource.updateTodoProgress(instanceId, progress) }
    }

    @Test
    fun `deleteTodoById should delete template`() = runTest {

        val instanceId = 1L
        val deletedInstance = sampleTodoInstanceDto.copy(
            id = instanceId,
            templateId = 2L
        )

        coEvery { todoInstanceLocalDataSource.getTodoInstanceById(instanceId) } returns deletedInstance
        coEvery { todoTemplateLocalDataSource.deleteTodoTemplate(deletedInstance.templateId) } just Runs

        todoWriteRepository.deleteTodoById(instanceId)

        coVerify { todoInstanceLocalDataSource.getTodoInstanceById(instanceId) }
        coVerify { todoTemplateLocalDataSource.deleteTodoTemplate(deletedInstance.templateId) }
    }

    @Test
    fun `updateTodo should update template and instance`() = runTest {
        val todoModel = sampleTodoModel.copy(
            instanceId = 1L,
            time = Time( 22, 30),
            alarmType = AlarmType.NOTIFY
        )

        val todoInstance = sampleTodoInstanceDto.copy(
            id = todoModel.instanceId,
            templateId = 2L,
        )

        coEvery { todoInstanceLocalDataSource.getTodoInstanceById(todoModel.instanceId) } returns todoInstance
        coEvery { todoTemplateLocalDataSource.updateTodoTemplate(any()) } just Runs
        coEvery { todoInstanceLocalDataSource.updateTodoInstance(any()) } just Runs
        coEvery { alarmScheduler.schedule(any(), any(), any()) } just Runs

        todoWriteRepository.updateTodo(todoModel)

        coVerify { todoInstanceLocalDataSource.getTodoInstanceById(todoModel.instanceId) }
        coVerify { todoTemplateLocalDataSource.updateTodoTemplate(any()) }
        coVerify { todoInstanceLocalDataSource.updateTodoInstance(any()) }
        coVerify { alarmScheduler.reschedule(todoModel.date, todoModel.time!!, 2L) }
    }

    @Test
    fun `updateTodo should not update alarm if time is null`() = runTest {
        val todoModel = sampleTodoModel.copy(
            instanceId = 1L,
            time = null,
            alarmType = AlarmType.NOTIFY
        )
        val todoInstance = sampleTodoInstanceDto.copy(
            id = todoModel.instanceId,
            templateId = 2L,
        )
        coEvery { todoInstanceLocalDataSource.getTodoInstanceById(todoModel.instanceId) } returns todoInstance
        coEvery { todoTemplateLocalDataSource.updateTodoTemplate(any()) } just Runs
        coEvery { todoInstanceLocalDataSource.updateTodoInstance(any()) } just Runs
        coEvery { alarmScheduler.reschedule(any(), any(), any()) } just Runs

        todoWriteRepository.updateTodo(todoModel)

        coVerify { todoInstanceLocalDataSource.getTodoInstanceById(todoModel.instanceId) }
        coVerify { todoTemplateLocalDataSource.updateTodoTemplate(any()) }
        coVerify { todoInstanceLocalDataSource.updateTodoInstance(any()) }
        coVerify(exactly = 0) { alarmScheduler.reschedule(any(), any(), any()) }
        coVerify { alarmScheduler.cancel(todoInstance.templateId) }
    }

    @Test
    fun `findTodoById should return todo model`() = runTest {

        val instanceId = 1L
        val templateId = 2L

        val todoInstance = sampleTodoInstanceDto.copy(
            id = instanceId,
            templateId = templateId
        )
        val todoTemplate = sampleTodoTemplateDto.copy(
            id = templateId
        )
        val todoPeriod = sampleTodoPeriodDto.copy()
        val todoDayOfWeeks = listOf(
            sampleTodoDayOfWeekDto.copy(id = 1L, dayOfWeek = 1),
            sampleTodoDayOfWeekDto.copy(id = 2L, dayOfWeek = 2)
        )
        coEvery { todoInstanceLocalDataSource.getTodoInstanceById(instanceId) } returns todoInstance
        coEvery { todoTemplateLocalDataSource.getTodoTemplateById(templateId) } returns todoTemplate
        coEvery { todoPeriodLocalDataSource.getTodoPeriodByTemplateId(templateId) } returns todoPeriod
        coEvery { todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(templateId) } returns todoDayOfWeeks

        val result = todoReadRepository.findTodoById(instanceId)

        coVerify { todoInstanceLocalDataSource.getTodoInstanceById(instanceId) }
        coVerify { todoTemplateLocalDataSource.getTodoTemplateById(templateId) }
        coVerify { todoPeriodLocalDataSource.getTodoPeriodByTemplateId(templateId) }
        coVerify { todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(templateId) }

        assertEquals(
            result,
            sampleTodoModel.copy(
                instanceId = instanceId,
                title = todoTemplate.title,
                description = todoTemplate.description,
                date = transferMillis2LocalDate(todoInstance.date),
                time = if(todoTemplate.hour != null) Time(todoTemplate.hour!!, todoTemplate.minute!!) else null,
                alarmType = todoTemplate.alarmType.toModel(),
                progressAngle = todoInstance.progressAngle,
                startDate = transferMillis2LocalDate(todoPeriod.startDate),
                endDate = transferMillis2LocalDate(todoPeriod.endDate),
                dayOfWeeks = todoDayOfWeeks.map { it.dayOfWeek },
                isAlarmHasVibration = todoTemplate.isAlarmHasVibration,
                isAlarmHasSound = todoTemplate.isAlarmHasSound
            )
        )
    }

    @Test
    fun `postPeriodTodo should insert template and instances and schedule alarm`() = runTest {

        val templateId = 1L
        val todoModel = sampleTodoModel.copy(
            time = Time(12,20),
            alarmType = AlarmType.NOTIFY
        )
        val startDate = LocalDate.now().minusDays(1)
        val endDate = LocalDate.now().plusDays(1)

        coEvery { todoTemplateLocalDataSource.insertTodoTemplate(any()) } returns templateId
        coEvery { todoInstanceLocalDataSource.insertTodoInstances(any()) } just Runs
        coEvery { todoPeriodLocalDataSource.insertTodoPeriod(any()) } just Runs

        todoWriteRepository.postPeriodTodo(todoModel, startDate, endDate)

        coVerify { todoTemplateLocalDataSource.insertTodoTemplate(any()) }
        coVerify { todoInstanceLocalDataSource.insertTodoInstances(any()) }
        coVerify { todoPeriodLocalDataSource.insertTodoPeriod(any()) }

        coVerify { alarmScheduler.schedule(
            date = any(),
            time = todoModel.time!!,
            templateId = templateId
        ) }
    }

    @Test
    fun `postPeriodTodo should not schedule alarm if today is not in period`() = runTest {
        val templateId = 1L
        val todoModel = sampleTodoModel.copy(
            time = Time(12,20),
            alarmType = AlarmType.NOTIFY
        )
        val startDate = LocalDate.now().minusDays(3)
        val endDate = LocalDate.now().minusDays(1)

        coEvery { todoTemplateLocalDataSource.insertTodoTemplate(any()) } returns templateId
        coEvery { todoInstanceLocalDataSource.insertTodoInstances(any()) } just Runs
        coEvery { todoPeriodLocalDataSource.insertTodoPeriod(any()) } just Runs

        todoWriteRepository.postPeriodTodo(todoModel, startDate, endDate)

        coVerify { todoTemplateLocalDataSource.insertTodoTemplate(any()) }
        coVerify { todoInstanceLocalDataSource.insertTodoInstances(any()) }
        coVerify { todoPeriodLocalDataSource.insertTodoPeriod(any()) }

        coVerify(exactly = 0) { alarmScheduler.schedule(any(), any(), any())}
    }

    @Test
    fun `updatePeriodTodo should update template and instances and schedule alarms`() = runTest {

        val todayDate = LocalDate.now()

        val templateId = 1L
        val instanceId = 1L
        val todoModel = sampleTodoModel.copy(
            instanceId = instanceId,
            startDate = todayDate,
            endDate = todayDate.plusDays(5),
            time = Time(11, 20),
            alarmType = AlarmType.NOTIFY
        )

        val existingInstances = listOf(
            sampleTodoInstanceDto.copy(
                date = transferLocalDateToMillis(todayDate.plusDays(1))
            ),
            sampleTodoInstanceDto.copy(
                date = transferLocalDateToMillis(todayDate.plusDays(2))
            ),
            sampleTodoInstanceDto.copy(
                date = transferLocalDateToMillis(todayDate.plusDays(3))
            )
        )

        coEvery { todoInstanceLocalDataSource.getTodoInstanceById(instanceId) } returns sampleTodoInstanceDto.copy(
            templateId = templateId
        )
        coEvery { todoTemplateLocalDataSource.updateTodoTemplate(any()) } just Runs
        coEvery { todoInstanceLocalDataSource.getInstancesByTemplateId(templateId)} returns existingInstances
        coEvery { todoPeriodLocalDataSource.updateTodoPeriod(any()) } just Runs
        coEvery { todoInstanceLocalDataSource.deleteInstancesByDates(any(), any()) } just Runs
        coEvery { todoInstanceLocalDataSource.insertTodoInstances(any()) } just Runs
        coEvery { alarmScheduler.cancel(templateId) } just Runs
        coEvery { alarmScheduler.schedule(any(), any(), any()) } just Runs

        todoWriteRepository.updatePeriodTodo(todoModel)

        coVerify { todoInstanceLocalDataSource.getTodoInstanceById(instanceId) }
        coVerify { todoTemplateLocalDataSource.updateTodoTemplate(any()) }
        coVerify { todoInstanceLocalDataSource.getInstancesByTemplateId(templateId) }
        coVerify { todoPeriodLocalDataSource.updateTodoPeriod(any()) }
        coVerify { todoInstanceLocalDataSource.deleteInstancesByDates(templateId, any()) }
        coVerify { todoInstanceLocalDataSource.insertTodoInstances(any()) }
        coVerify { alarmScheduler.cancel(templateId) }
        coVerify { alarmScheduler.schedule(todayDate.plusDays(1), todoModel.time!!, templateId) }

    }

    @Test
    fun `updatePeriodTodo should update template and instances and schedule alarms but not at startDate`() = runTest {

        val todayDate = LocalDate.now()
        val todayTime = LocalTime.now()

        val templateId = 1L
        val instanceId = 1L
        val todoModel = sampleTodoModel.copy(
            instanceId = instanceId,
            startDate = todayDate,
            endDate = todayDate.plusDays(5),
            time = Time(todayTime.hour, todayTime.minute + 10),
            alarmType = AlarmType.NOTIFY
        )

        val existingInstances = listOf(
            sampleTodoInstanceDto.copy(
                date = transferLocalDateToMillis(todayDate.minusDays(1))
            ),
            sampleTodoInstanceDto.copy(
                date = transferLocalDateToMillis(todayDate)
            ),
            sampleTodoInstanceDto.copy(
                date = transferLocalDateToMillis(todayDate.plusDays(1))
            )
        )

        coEvery { todoInstanceLocalDataSource.getTodoInstanceById(instanceId) } returns sampleTodoInstanceDto.copy(
            templateId = templateId
        )
        coEvery { todoTemplateLocalDataSource.updateTodoTemplate(any()) } just Runs
        coEvery { todoInstanceLocalDataSource.getInstancesByTemplateId(templateId)} returns existingInstances
        coEvery { todoPeriodLocalDataSource.updateTodoPeriod(any()) } just Runs
        coEvery { todoInstanceLocalDataSource.deleteInstancesByDates(any(), any()) } just Runs
        coEvery { todoInstanceLocalDataSource.insertTodoInstances(any()) } just Runs
        coEvery { alarmScheduler.cancel(templateId) } just Runs
        coEvery { alarmScheduler.schedule(any(), any(), any()) } just Runs

        todoWriteRepository.updatePeriodTodo(todoModel)

        coVerify { todoInstanceLocalDataSource.getTodoInstanceById(instanceId) }
        coVerify { todoTemplateLocalDataSource.updateTodoTemplate(any()) }
        coVerify { todoInstanceLocalDataSource.getInstancesByTemplateId(templateId) }
        coVerify { todoPeriodLocalDataSource.updateTodoPeriod(any()) }
        coVerify { todoInstanceLocalDataSource.deleteInstancesByDates(templateId, any()) }
        coVerify { todoInstanceLocalDataSource.insertTodoInstances(any()) }
        coVerify { alarmScheduler.cancel(templateId) }
        coVerify { alarmScheduler.schedule(todayDate, todoModel.time!!, templateId) }

    }

    @Test
    fun `postDayOfWeekTodo should insert template and day of weeks in 1,2,3`() = runTest {

        val templateId = 1L
        val todoModel = sampleTodoModel.copy(
            title = "Test Todo",
            time = Time(12, 20),
            alarmType = AlarmType.NOTIFY
        )
        val dayOfWeek = listOf(1, 2, 3)

        coEvery {
            todoTemplateLocalDataSource.insertTodoTemplate(match {
                it.title == todoModel.title &&
                        it.hour == todoModel.time!!.hour &&
                        it.minute == todoModel.time!!.minute &&
                        it.type == TodoTypeDto.DAY_OF_WEEK
            })
        } returns templateId
        coEvery { todoDayOfWeekLocalDataSource.insertTodoDayOfWeek(any()) } just Runs
        coEvery { alarmScheduler.schedule(any(), any(), any()) } just Runs

        todoWriteRepository.postDayOfWeekTodo(todoModel, dayOfWeek)

        coVerify(exactly = 1) {
            todoTemplateLocalDataSource.insertTodoTemplate(any())
        }
        coVerify(exactly = dayOfWeek.size) {
            todoDayOfWeekLocalDataSource.insertTodoDayOfWeek(match {
                it.templateId == templateId &&
                        it.dayOfWeeks == dayOfWeek &&
                        dayOfWeek.contains(it.dayOfWeek)
            })
        }
        coVerify(exactly = 1) {
            alarmScheduler.schedule(
                date = match {
                    it.dayOfWeek.value in dayOfWeek
                },
                time = match { it.hour == 12 && it.minute == 20 },
                templateId = templateId
            )
        }
    }

    @Test
    fun `postDayOfWeekTodo should insert template and day of weeks in 4,5,6,7`() = runTest {

        val templateId = 1L
        val todoModel = sampleTodoModel.copy(
            title = "Test Todo",
            time = Time(12, 20),
            alarmType = AlarmType.NOTIFY
        )
        val dayOfWeek = listOf(4,5,6,7)

        coEvery {
            todoTemplateLocalDataSource.insertTodoTemplate(match {
                it.title == todoModel.title &&
                        it.hour == todoModel.time!!.hour &&
                        it.minute == todoModel.time!!.minute &&
                        it.type == TodoTypeDto.DAY_OF_WEEK
            })
        } returns templateId
        coEvery { todoDayOfWeekLocalDataSource.insertTodoDayOfWeek(any()) } just Runs
        coEvery { alarmScheduler.schedule(any(), any(), any()) } just Runs

        todoWriteRepository.postDayOfWeekTodo(todoModel, dayOfWeek)

        coVerify(exactly = 1) {
            todoTemplateLocalDataSource.insertTodoTemplate(any())
        }
        coVerify(exactly = dayOfWeek.size) {
            todoDayOfWeekLocalDataSource.insertTodoDayOfWeek(match {
                it.templateId == templateId &&
                        it.dayOfWeeks == dayOfWeek &&
                        dayOfWeek.contains(it.dayOfWeek)
            })
        }
        coVerify(exactly = 1) {
            alarmScheduler.schedule(
                date = match {
                    it.dayOfWeek.value in dayOfWeek
                },
                time = match { it.hour == 12 && it.minute == 20 },
                templateId = templateId
            )
        }
    }

    @Test
    fun `updateDayOfWeekTodo should update template, handle days and reschedule alarm`() = runTest {

        val instanceId = 1L
        val templateId = 2L
        val existingDayOfWeeks = listOf(
            TodoDayOfWeekDto(templateId = templateId, dayOfWeeks = listOf(1, 2), dayOfWeek = 1),
            TodoDayOfWeekDto(templateId = templateId, dayOfWeeks = listOf(1, 2), dayOfWeek = 2),
        )
        val updatedDayOfWeeks = listOf(2, 3) // 1번 요일 삭제, 3번 요일 추가
        val todoModel = sampleTodoModel.copy(
            instanceId = instanceId,
            title = "Updated Todo",
            description = "Updated Description",
            time = Time(14, 30),
            alarmType = AlarmType.NOTIFY,
            dayOfWeeks = updatedDayOfWeeks
        )

        coEvery { todoInstanceLocalDataSource.getTodoInstanceById(instanceId) } returns sampleTodoInstanceDto.copy(
            id = instanceId,
            templateId = templateId
        )
        coEvery { todoTemplateLocalDataSource.updateTodoTemplate(any()) } just Runs
        coEvery { todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(templateId) } returns existingDayOfWeeks
        coEvery { todoDayOfWeekLocalDataSource.deleteSpecificDayOfWeeks(templateId, any()) } just Runs
        coEvery { todoDayOfWeekLocalDataSource.deleteInstancesByTemplateIdAndDaysOfWeek(templateId, any()) } just Runs
        coEvery { todoDayOfWeekLocalDataSource.insertDayOfWeekTodos(any()) } just Runs
        coEvery { alarmScheduler.cancel(templateId) } just Runs
        coEvery { alarmScheduler.schedule(any(), any(), any()) } just Runs

        todoWriteRepository.updateDayOfWeekTodo(todoModel)

        coVerify(exactly = 1) {
            todoTemplateLocalDataSource.updateTodoTemplate(match {
                it.id == templateId &&
                        it.title == todoModel.title &&
                        it.description == todoModel.description &&
                        it.hour == todoModel.time?.hour &&
                        it.minute == todoModel.time?.minute &&
                        it.type == TodoTypeDto.DAY_OF_WEEK &&
                        it.alarmType == todoModel.alarmType.toDto() &&
                        it.isAlarmHasVibration == todoModel.isAlarmHasVibration &&
                        it.isAlarmHasSound == todoModel.isAlarmHasSound
            })
        }
        coVerify(exactly = 1) {
            todoDayOfWeekLocalDataSource.deleteSpecificDayOfWeeks(
                templateId,
                match { it.contains(1) && it.size == 1 }
            )
        }
        coVerify(exactly = 1) {
            todoDayOfWeekLocalDataSource.deleteInstancesByTemplateIdAndDaysOfWeek(
                templateId,
                match { it.contains(1) && it.size == 1 }
            )
        }
        coVerify(exactly = 1) {
            todoDayOfWeekLocalDataSource.insertDayOfWeekTodos(match {
                it.any { dto -> dto.dayOfWeek == 3 }
            })
        }
        coVerify(exactly = 1) { alarmScheduler.cancel(templateId) }

        coVerify(exactly = 1) {
            alarmScheduler.schedule(
                date = match {
                    it.dayOfWeek.value in updatedDayOfWeeks
                },
                time = match { it.hour == 14 && it.minute == 30 },
                templateId = templateId
            )
        }
    }
}