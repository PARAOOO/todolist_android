package com.paraooo.todolist

import android.util.Log
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.todolist.ui.components.AlarmInputState
import com.paraooo.todolist.ui.components.AlarmSettingInputState
import com.paraooo.todolist.ui.components.DateInputState
import com.paraooo.todolist.ui.components.TimeInputState
import com.paraooo.todolist.ui.components.TodoInputState
import com.paraooo.todolist.ui.components.TodoNameInputState
import com.paraooo.todolist.ui.features.create.CreateUiEffect
import com.paraooo.todolist.ui.features.create.CreateUiEvent
import com.paraooo.todolist.ui.features.create.CreateUiState
import com.paraooo.todolist.ui.features.create.CreateViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CreateViewModelUnitTest {

    private lateinit var viewModel: CreateViewModel
    private lateinit var todoRepository: TodoRepository

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

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        todoRepository = mockk()
        viewModel = CreateViewModel(todoRepository)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onCreateClicked should postTodo when dateInputState is Date`() = runTest {
        val todoName = "Test Todo"
        val customViewModel = CreateViewModel(todoRepository, CreateUiState(
            todoInputState = TodoInputState(
                dateInputState = DateInputState.Date(LocalDate.now()),
                todoNameInputState = TodoNameInputState(todoName)
            ),
        ))
        coEvery { todoRepository.postTodo(any()) } just Runs

        customViewModel.onEvent(CreateUiEvent.onCreateClicked)

        val effect = customViewModel.effectFlow.first()
        advanceUntilIdle()

        assertEquals(CreateUiEffect.onPostTodoSuccess(todoName), effect)
        coVerify(exactly = 1) { todoRepository.postTodo(any()) }
    }

    @Test
    fun `onCreateClicked should postPeriodTodo when dateInputState is Period`() = runTest {
        val todoName = "Test Todo"
        val customViewModel = CreateViewModel(todoRepository, CreateUiState(
            todoInputState = TodoInputState(
                dateInputState = DateInputState.Period(LocalDate.now(), LocalDate.now()),
                todoNameInputState = TodoNameInputState(todoName)
            ),
        ))
        coEvery { todoRepository.postPeriodTodo(any(), any(), any()) } just Runs

        customViewModel.onEvent(CreateUiEvent.onCreateClicked)

        val effect = customViewModel.effectFlow.first()
        advanceUntilIdle()

        assertEquals(CreateUiEffect.onPostTodoSuccess(todoName), effect)
        coVerify(exactly = 1) { todoRepository.postPeriodTodo(any(), any(), any()) }
    }

    @Test
    fun `onCreateClicked should postDayOfWeekTodo when dateInputState is DayOfWeek`() = runTest {
        val todoName = "Test Todo"
        val customViewModel = CreateViewModel(todoRepository, CreateUiState(
            todoInputState = TodoInputState(
                dateInputState = DateInputState.DayOfWeek(listOf(1)),
                todoNameInputState = TodoNameInputState(todoName)
            ),
        ))
        coEvery { todoRepository.postDayOfWeekTodo(any(), any()) } just Runs

        customViewModel.onEvent(CreateUiEvent.onCreateClicked)

        val effect = customViewModel.effectFlow.first()
        advanceUntilIdle()

        assertEquals(CreateUiEffect.onPostTodoSuccess(todoName), effect)
        coVerify(exactly = 1) { todoRepository.postDayOfWeekTodo(any(), any()) }
    }

    @Test
    fun `onDateInputChanged should update dateInputState`() = runTest {
        val date = LocalDate.now()

        viewModel.onEvent(CreateUiEvent.onDateInputChanged(date))
        advanceUntilIdle()

        assertEquals(DateInputState.Date(date), viewModel.uiState.value.todoInputState.dateInputState)
    }

    @Test
    fun `onDescriptionInputChanged should update descriptionInputState`() = runTest {
        val description = "Test Description"

        viewModel.onEvent(CreateUiEvent.onDescriptionInputChanged(description))
        advanceUntilIdle()

        assertEquals(description, viewModel.uiState.value.todoInputState.descriptionInputState.content)
    }

    @Test
    fun `onTimeInputChanged should update timeInputState`() = runTest {
        val timeInputState = TimeInputState.Time(12, 0)

        viewModel.onEvent(CreateUiEvent.onTimeInputChanged(timeInputState))
        advanceUntilIdle()

        assertEquals(timeInputState, viewModel.uiState.value.todoInputState.timeInputState)
    }

    @Test
    fun `onTodoNameInputChanged should update todoNameInputState`() = runTest {
        val todoName = "Test Todo Name"

        viewModel.onEvent(CreateUiEvent.onTodoNameInputChanged(todoName))
        advanceUntilIdle()

        assertEquals(todoName, viewModel.uiState.value.todoInputState.todoNameInputState.content)
    }

    @Test
    fun `onSelectedDateChanged should update dateInputState`() = runTest {
        val date = LocalDate.now()

        viewModel.onEvent(CreateUiEvent.onSelectedDateChanged(date))
        advanceUntilIdle()

        assertEquals(DateInputState.Date(date), viewModel.uiState.value.todoInputState.dateInputState)
    }

    @Test
    fun `onPeriodInputChanged should update dateInputState to Period`() = runTest {
        val startDate = LocalDate.now()
        val endDate = startDate.plusDays(1)

        viewModel.onEvent(CreateUiEvent.onPeriodInputChanged(startDate, endDate))
        advanceUntilIdle()

        assertEquals(DateInputState.Period(startDate, endDate), viewModel.uiState.value.todoInputState.dateInputState)
    }

    @Test
    fun `onDayOfWeekInputChanged should update dateInputState to DayOfWeek`() = runTest {
        val daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)

        viewModel.onEvent(CreateUiEvent.onDayOfWeekInputChanged(daysOfWeek))
        advanceUntilIdle()

        assertEquals(DateInputState.DayOfWeek(daysOfWeek.map { it.value }), viewModel.uiState.value.todoInputState.dateInputState)
    }

    @Test
    fun `onAlarmInputChanged should update alarmInputState`() = runTest {
        val alarm = AlarmType.POPUP

        viewModel.onEvent(CreateUiEvent.onAlarmInputChanged(alarm))
        advanceUntilIdle()

        assertEquals(AlarmInputState(alarm), viewModel.uiState.value.todoInputState.alarmInputState)
    }

    @Test
    fun `onAlarmSettingInputChanged should update alarmSettingInputState`() = runTest {
        val vibration = true
        val sound = false

        viewModel.onEvent(CreateUiEvent.onAlarmSettingInputChanged(vibration, sound))
        advanceUntilIdle()

        assertEquals(AlarmSettingInputState(vibration, sound), viewModel.uiState.value.todoInputState.alarmSettingInputState)
    }
}