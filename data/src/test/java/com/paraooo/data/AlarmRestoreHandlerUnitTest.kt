//package com.paraooo.data
//
//import androidx.work.ListenableWorker
//import com.paraooo.local.datasourceimpl.TodoDayOfWeekLocalDataSourceImpl
//import com.paraooo.local.datasourceimpl.TodoPeriodLocalDataSourceImpl
//import com.paraooo.local.datasourceimpl.TodoTemplateLocalDataSourceImpl
//import com.paraooo.data.dto.AlarmTypeDto
//import com.paraooo.data.dto.TodoDayOfWeekDto
//import com.paraooo.data.dto.TodoDayOfWeekWithTimeDto
//import com.paraooo.data.dto.TodoDto
//import com.paraooo.data.dto.TodoInstanceDto
//import com.paraooo.data.dto.TodoPeriodDto
//import com.paraooo.data.dto.TodoPeriodWithTimeDto
//import com.paraooo.data.dto.TodoTemplateDto
//import com.paraooo.data.dto.TodoTypeDto
//import com.paraooo.data.platform.alarm.AlarmSchedulerImpl
//import com.paraooo.data.platform.handler.AlarmRestoreHandler
//import com.paraooo.domain.model.AlarmType
//import com.paraooo.domain.model.TodoModel
//import com.paraooo.domain.util.transferLocalDateToMillis
//import com.paraooo.domain.util.transferMillis2LocalDate
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.mockk
//import junit.framework.Assert.assertEquals
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import java.time.LocalDate
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class AlarmRestoreHandlerUnitTest {
//
//    private val todoTemplateLocalDataSourceImpl = mockk<TodoTemplateLocalDataSourceImpl>()
//    private val todoPeriodLocalDataSourceImpl = mockk<TodoPeriodLocalDataSourceImpl>()
//    private val todoDayOfWeekLocalDataSourceImpl = mockk<TodoDayOfWeekLocalDataSourceImpl>()
//    private val alarmScheduler = mockk<AlarmSchedulerImpl>(relaxed = true) // alarm 호출 무시
//
//    private val sampleTodoModel = TodoModel(
//        instanceId = 1L,
//        title = "sample title",
//        time = null,
//        progressAngle = 0F,
//        date = LocalDate.of(2000, 1, 1),
//        description = "sample description",
//        alarmType = AlarmType.OFF,
//        isAlarmHasVibration = false,
//        isAlarmHasSound = false
//    )
//
//    private val sampleTodoDto = TodoDto(
//        templateId = 1L,
//        instanceId = 1L,
//        title = "sample title",
//        hour = null,
//        minute = null,
//        progressAngle = 0F,
//        date = transferLocalDateToMillis(LocalDate.of(2000, 1, 1)),
//        description = "sample description",
//        alarmType = AlarmTypeDto.OFF,
//        isAlarmHasVibration = false,
//        isAlarmHasSound = false
//    )
//
//    private val sampleTodoTemplateDto = TodoTemplateDto(
//        id = 1L,
//        title = "sample title",
//        hour = null,
//        minute = null,
//        description = "sample description",
//        type = TodoTypeDto.GENERAL,
//        alarmType = AlarmTypeDto.OFF,
//        isAlarmHasVibration = false,
//        isAlarmHasSound = false
//    )
//
//    private val sampleTodoInstanceDto = TodoInstanceDto(
//        id = 1L,
//        templateId = 1L,
//        date = transferLocalDateToMillis(LocalDate.of(2000, 1, 1)),
//        progressAngle = 0F
//    )
//
//    private val sampleTodoPeriodDto = TodoPeriodDto(
//        templateId = 1L,
//        startDate = transferLocalDateToMillis(LocalDate.of(2000, 1, 1)),
//        endDate = transferLocalDateToMillis(LocalDate.of(2000, 1, 2))
//    )
//
//    private val sampleTodoDayOfWeekDto = TodoDayOfWeekDto(
//        templateId = 1L,
//        dayOfWeek = 1,
//        dayOfWeeks = listOf(1, 2)
//    )
//
//    private val sampleTodoPeriodWithTimeDto = TodoPeriodWithTimeDto(
//        templateId = 1L,
//        startDate = transferLocalDateToMillis(LocalDate.of(2000, 1, 1)),
//        endDate = transferLocalDateToMillis(LocalDate.of(2000, 1, 2)),
//        hour = 12,
//        minute = 0
//    )
//
//    private val sampleTodoDayOfWeekWithTimeDto = TodoDayOfWeekWithTimeDto(
//        templateId = 1L,
//        dayOfWeeks = listOf(1, 2),
//        hour = 12,
//        minute = 0
//    )
//
//    private lateinit var alarmRestoreHandler: AlarmRestoreHandler
//
//    private val testDispatcher = StandardTestDispatcher()
//
//    @Before
//    fun setUp() {
//        Dispatchers.setMain(testDispatcher)
//
//        alarmRestoreHandler = AlarmRestoreHandler(
//            alarmScheduler,
//            todoTemplateLocalDataSourceImpl,
//            todoPeriodLocalDataSourceImpl,
//            todoDayOfWeekLocalDataSourceImpl,
//        )
//
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `schedule alarm`() = runTest {
//
//        val today = LocalDate.now()
//
//        val alarmTodos = listOf(
//            sampleTodoDto.copy(
//                templateId = 1L,
//                date = transferLocalDateToMillis(today.plusDays(1)),
//                hour = 12, minute = 30
//            )
//        )
//        val alarmPeriodTodos = listOf(
//            sampleTodoPeriodWithTimeDto.copy(
//                templateId = 2L,
//                hour = 12, minute = 0,
//                startDate = transferLocalDateToMillis(today.plusDays(1)),
//                endDate = transferLocalDateToMillis(today.plusDays(3))
//            )
//        )
//        val alarmDayOfWeekTodos = listOf(
//            sampleTodoDayOfWeekWithTimeDto.copy(
//                templateId = 3,
//                hour = 12, minute = 0,
//                dayOfWeeks = listOf(1,2)
//            )
//        )
//
//        coEvery { todoTemplateLocalDataSourceImpl.getAlarmTodos(any()) } returns alarmTodos
//        coEvery { todoPeriodLocalDataSourceImpl.getAlarmPeriodTodos(any()) } returns alarmPeriodTodos
//        coEvery { todoDayOfWeekLocalDataSourceImpl.getAlarmDayOfWeekTodos() } returns alarmDayOfWeekTodos
//
//        val result = alarmRestoreHandler.handleAlarm()
//
//        coVerify(exactly = 1) { alarmScheduler.schedule(
//            transferMillis2LocalDate(alarmTodos.first().date), any(), alarmTodos.first().templateId)
//        }
//        coVerify(exactly = 1) { alarmScheduler.schedule(
//            transferMillis2LocalDate(alarmPeriodTodos.first().startDate), any(), alarmPeriodTodos.first().templateId)
//        }
//        coVerify(exactly = 1) { alarmScheduler.schedule( any(), any(), alarmDayOfWeekTodos.first().templateId ) }
//
//        assertEquals(ListenableWorker.Result.success(), result)
//    }
//}