package com.paraooo.data

import android.content.Context
import android.content.Intent
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
import com.paraooo.data.platform.alarm.AlarmScheduler
import com.paraooo.data.platform.alarm.IntentProvider
import com.paraooo.data.platform.alarm.NotificationHelper
import com.paraooo.data.platform.handler.AlarmHandler
import com.paraooo.data.repository.TodoRepositoryImpl
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.util.transferLocalDateToMillis
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import androidx.work.ListenableWorker.Result
import com.paraooo.domain.model.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AlarmHandlerUnitTest {

    private val todoTemplateLocalDataSource = mockk<TodoTemplateLocalDataSource>()
    private val todoInstanceLocalDataSource = mockk<TodoInstanceLocalDataSource>()
    private val todoPeriodLocalDataSource = mockk<TodoPeriodLocalDataSource>()
    private val todoDayOfWeekLocalDataSource = mockk<TodoDayOfWeekLocalDataSource>()
    private val alarmScheduler = mockk<AlarmScheduler>(relaxed = true) // alarm 호출 무시
    private val notificationHelper = mockk<NotificationHelper>(relaxed = true)
    private val intentProvider = mockk<IntentProvider>(relaxed = true)
    private val context = mockk<Context>(relaxed = true)

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

    private lateinit var alarmHandler: AlarmHandler

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        alarmHandler = AlarmHandler(
            alarmScheduler,
            notificationHelper,
            todoTemplateLocalDataSource,
            todoInstanceLocalDataSource,
            todoPeriodLocalDataSource,
            todoDayOfWeekLocalDataSource,
            intentProvider
        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `do not schedule alarm when type is GENERAL`() = runTest {

        val todoTemplate = sampleTodoTemplateDto.copy(
            type = TodoTypeDto.GENERAL
        )

        coEvery { todoInstanceLocalDataSource.getInstancesByTemplateId(1L) } returns listOf(sampleTodoInstanceDto)
        coEvery { todoTemplateLocalDataSource.getTodoTemplateById(1L) } returns todoTemplate
        coEvery { todoPeriodLocalDataSource.getTodoPeriodByTemplateId(1L) } returns null
        coEvery { todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(1L) } returns listOf()

        val result = alarmHandler.handleAlarm(1L, context)

        coVerify(exactly = 0) { alarmScheduler.schedule(any(), any(), any()) }
        assertEquals(Result.success(), result)
    }

    @Test
    fun `schedule alarm when type is PERIOD`() = runTest {

        val today = LocalDate.now()
        val now = LocalTime.now()

        val todoTemplate = sampleTodoTemplateDto.copy(
            type = TodoTypeDto.PERIOD,
            hour = now.hour, minute = now.minute
        )
        val todoPeriod = sampleTodoPeriodDto.copy(
            startDate = transferLocalDateToMillis(today.plusDays(-1)),
            endDate = transferLocalDateToMillis(today.plusDays(1))
        )

        coEvery { todoInstanceLocalDataSource.getInstancesByTemplateId(1L) } returns listOf(sampleTodoInstanceDto)
        coEvery { todoTemplateLocalDataSource.getTodoTemplateById(1L) } returns todoTemplate
        coEvery { todoPeriodLocalDataSource.getTodoPeriodByTemplateId(1L) } returns todoPeriod
        coEvery { todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(1L) } returns listOf()

        val result = alarmHandler.handleAlarm(1L, context)

        coVerify { alarmScheduler.schedule(
            today.plusDays(1), Time(todoTemplate.hour!!, todoTemplate.minute!!), 1L
        ) }
        assertEquals(Result.success(), result)
    }

    @Test
    fun `schedule alarm when type is DAY_OF_WEEK`() = runTest {

        val todoTemplate = sampleTodoTemplateDto.copy(
            type = TodoTypeDto.DAY_OF_WEEK,
            hour = 9,
            minute = 0
        )
        val todoDayOfWeek = sampleTodoDayOfWeekDto.copy(
            dayOfWeeks = listOf(1, 2) // 월, 화요일 알람 예약
        )

        coEvery { todoInstanceLocalDataSource.getInstancesByTemplateId(1L) } returns listOf(sampleTodoInstanceDto)
        coEvery { todoTemplateLocalDataSource.getTodoTemplateById(1L) } returns todoTemplate
        coEvery { todoPeriodLocalDataSource.getTodoPeriodByTemplateId(1L) } returns null
        coEvery { todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(1L) } returns listOf(todoDayOfWeek)

        val result = alarmHandler.handleAlarm(1L, context)

        coVerify { alarmScheduler.schedule(
            any(), Time(todoTemplate.hour!!, todoTemplate.minute!!), 1L
        ) }
        assertEquals(Result.success(), result)
    }

    @Test
    fun `show notification when alarm type is NOTIFY`() = runTest {

        val todoTemplate = sampleTodoTemplateDto.copy(
            type = TodoTypeDto.GENERAL,
            alarmType = AlarmTypeDto.NOTIFY,
            hour = 9,
            minute = 0
        )
        val todoInstance = sampleTodoInstanceDto.copy(
            date = transferLocalDateToMillis(LocalDate.now())
        )

        coEvery { todoInstanceLocalDataSource.getInstancesByTemplateId(1L) } returns listOf(todoInstance)
        coEvery { todoTemplateLocalDataSource.getTodoTemplateById(1L) } returns todoTemplate
        coEvery { todoPeriodLocalDataSource.getTodoPeriodByTemplateId(1L) } returns null
        coEvery { todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(1L) } returns listOf()

        val result = alarmHandler.handleAlarm(1L, context)

        coVerify { notificationHelper.showNotification(context, todoInstance, todoTemplate) }
        assertEquals(Result.success(), result)
    }

    @Test
    fun `show popup when alarm type is POPUP`() = runTest {

        val todoTemplate = sampleTodoTemplateDto.copy(
            type = TodoTypeDto.GENERAL,
            alarmType = AlarmTypeDto.POPUP,
            hour = 9,
            minute = 0
        )
        val todoInstance = sampleTodoInstanceDto.copy(
            date = transferLocalDateToMillis(LocalDate.now())
        )

        coEvery { todoInstanceLocalDataSource.getInstancesByTemplateId(1L) } returns listOf(todoInstance)
        coEvery { todoTemplateLocalDataSource.getTodoTemplateById(1L) } returns todoTemplate
        coEvery { todoPeriodLocalDataSource.getTodoPeriodByTemplateId(1L) } returns null
        coEvery { todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(1L) } returns listOf()

        val result = alarmHandler.handleAlarm(1L, context)

        coVerify {
            val intent = intentProvider.getPopupIntent(context)
            intent.putExtra("instanceId", todoInstance.id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        assertEquals(Result.success(), result)
    }

    @Test
    fun `do not schedule alarm when period end date has passed`() = runTest {
        val today = LocalDate.now()
        val todoTemplate = sampleTodoTemplateDto.copy(
            type = TodoTypeDto.PERIOD,
            hour = 9,
            minute = 0
        )
        val todoPeriod = sampleTodoPeriodDto.copy(
            startDate = transferLocalDateToMillis(today.plusDays(-1)),
            endDate = transferLocalDateToMillis(today.minusDays(1)) // 이미 끝난 기간
        )

        coEvery { todoInstanceLocalDataSource.getInstancesByTemplateId(1L) } returns listOf(sampleTodoInstanceDto)
        coEvery { todoTemplateLocalDataSource.getTodoTemplateById(1L) } returns todoTemplate
        coEvery { todoPeriodLocalDataSource.getTodoPeriodByTemplateId(1L) } returns todoPeriod
        coEvery { todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(1L) } returns listOf()

        val result = alarmHandler.handleAlarm(1L, context)

        coVerify(exactly = 0) { alarmScheduler.schedule(any(), any(), any()) }
        assertEquals(Result.success(), result)
    }

}