package com.paraooo.todolist

import android.util.Log
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoWriteRepository
import com.paraooo.todolist.ui.components.AlarmInputState
import com.paraooo.todolist.ui.components.AlarmSettingInputState
import com.paraooo.todolist.ui.components.DateInputState
import com.paraooo.todolist.ui.components.TimeInputState
import com.paraooo.todolist.ui.components.TodoInputState
import com.paraooo.todolist.ui.components.TodoNameInputState
import com.paraooo.todolist.ui.features.edit.EditUiEffect
import com.paraooo.todolist.ui.features.edit.EditUiEvent
import com.paraooo.todolist.ui.features.edit.EditUiState
import com.paraooo.todolist.ui.features.edit.EditViewModel
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
class EditViewModelUnitTest {
    private lateinit var viewModel: EditViewModel
    private lateinit var todoWriteRepository: TodoWriteRepository

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
        todoWriteRepository = mockk()
        viewModel = EditViewModel(todoWriteRepository)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onInit should fetch todo and update UI state`() = runTest{
        val instanceId = 1L
        val todo = sampleTodoModel.copy(title = "Test Todo")
        coEvery { todoWriteRepository.findTodoById(instanceId) } returns todo

        viewModel.onEvent(EditUiEvent.onInit(instanceId))
        advanceUntilIdle()

        assertEquals(viewModel.selectedTodo.value, todo)
        assertEquals(viewModel.uiState.value.todoInputState.todoNameInputState.content, todo.title)
    }

    @Test
    fun `onEditClicked should updateTodo when dateInputState is Date`() = runTest {
        val todoName = "Test Todo"
        val instanceId = 1L
        val customViewModel = EditViewModel(todoWriteRepository, EditUiState(
            todoInputState = TodoInputState(
                dateInputState = DateInputState.Date(LocalDate.now()),
                todoNameInputState = TodoNameInputState(todoName)
            ),
        ))
        coEvery { todoWriteRepository.updateTodo(any()) } just Runs

        customViewModel.selectedTodo.value = sampleTodoModel.copy(
            instanceId = instanceId, title = todoName
        )
        customViewModel.onEvent(EditUiEvent.onEditClicked(instanceId))

        val effect = customViewModel.effectFlow.first()
        advanceUntilIdle()

        assertEquals(EditUiEffect.onUpdateTodoSuccess(todoName), effect)
        coVerify(exactly = 1) { todoWriteRepository.updateTodo(any()) }
    }

    @Test
    fun `onEditClicked should UpdatePeriodTodo when dateInputState is Period`() = runTest {

        val todo = sampleTodoModel.copy(
            instanceId = 1L, title = "Test Todo", startDate = LocalDate.now(), endDate = LocalDate.now()
        )
        val customViewModel = EditViewModel(todoWriteRepository, EditUiState(
            todoInputState = TodoInputState(
                dateInputState = DateInputState.Period(todo.startDate!!, todo.endDate!!),
                todoNameInputState = TodoNameInputState(todo.title)
            ),
        ))
        coEvery { todoWriteRepository.updatePeriodTodo(any()) } just Runs

        customViewModel.selectedTodo.value = todo
        customViewModel.onEvent(EditUiEvent.onEditClicked(todo.instanceId))

        val effect = customViewModel.effectFlow.first()
        advanceUntilIdle()

        assertEquals(EditUiEffect.onUpdateTodoSuccess(todo.title), effect)
        coVerify(exactly = 1) { todoWriteRepository.updatePeriodTodo(any()) }
    }

    @Test
    fun `onEditClicked should UpdateDayOfWeekTodo when dateInputState is DayOfWeek`() = runTest {
        val todo = sampleTodoModel.copy(
            instanceId = 1L, title = "Test Todo", dayOfWeeks = listOf(1)
        )
        val customViewModel = EditViewModel(todoWriteRepository, EditUiState(
            todoInputState = TodoInputState(
                dateInputState = DateInputState.DayOfWeek(todo.dayOfWeeks!!),
                todoNameInputState = TodoNameInputState(todo.title)
            ),
        ))
        coEvery { todoWriteRepository.updateDayOfWeekTodo(any()) } just Runs

        customViewModel.selectedTodo.value = todo
        customViewModel.onEvent(EditUiEvent.onEditClicked(todo.instanceId))

        val effect = customViewModel.effectFlow.first()
        advanceUntilIdle()

        assertEquals(EditUiEffect.onUpdateTodoSuccess(todo.title), effect)
        coVerify(exactly = 1) { todoWriteRepository.updateDayOfWeekTodo(any()) }
    }

    @Test
    fun `onTodoNameInputChanged should update todoNameInputState`() = runTest {
        val todoName = "Test Todo Name"

        viewModel.onEvent(EditUiEvent.onTodoNameInputChanged(todoName))
        advanceUntilIdle()

        assertEquals(todoName, viewModel.uiState.value.todoInputState.todoNameInputState.content)
    }
    @Test

    fun `onDateInputChanged should update UI state`() = runTest {
        val newDate = LocalDate.of(2023, 1, 1)

        viewModel.onEvent(EditUiEvent.onDateInputChanged(newDate))
        advanceUntilIdle()

        assertEquals(viewModel.uiState.value.todoInputState.dateInputState, DateInputState.Date(newDate))

    }

    @Test
    fun `onDescriptionInputChanged should update descriptionInputState`() = runTest {
        val description = "Test Description"

        viewModel.onEvent(EditUiEvent.onDescriptionInputChanged(description))
        advanceUntilIdle()

        assertEquals(description, viewModel.uiState.value.todoInputState.descriptionInputState.content)
    }

    @Test
    fun `onTimeInputChanged should update timeInputState`() = runTest {
        val timeInputState = TimeInputState.Time(12, 0)

        viewModel.onEvent(EditUiEvent.onTimeInputChanged(timeInputState))
        advanceUntilIdle()

        assertEquals(timeInputState, viewModel.uiState.value.todoInputState.timeInputState)
    }

    @Test
    fun `onPeriodInputChanged should update dateInputState to Period`() = runTest {
        val startDate = LocalDate.now()
        val endDate = startDate.plusDays(1)

        viewModel.onEvent(EditUiEvent.onPeriodInputChanged(startDate, endDate))
        advanceUntilIdle()

        assertEquals(DateInputState.Period(startDate, endDate), viewModel.uiState.value.todoInputState.dateInputState)
    }

    @Test
    fun `onDayOfWeekInputChanged should update dateInputState to DayOfWeek`() = runTest {
        val daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)

        viewModel.onEvent(EditUiEvent.onDayOfWeekInputChanged(daysOfWeek))
        advanceUntilIdle()

        assertEquals(DateInputState.DayOfWeek(daysOfWeek.map { it.value }), viewModel.uiState.value.todoInputState.dateInputState)
    }

    @Test
    fun `onAlarmInputChanged should update alarmInputState`() = runTest {
        val alarm = AlarmType.POPUP

        viewModel.onEvent(EditUiEvent.onAlarmInputChanged(alarm))
        advanceUntilIdle()

        assertEquals(AlarmInputState(alarm), viewModel.uiState.value.todoInputState.alarmInputState)
    }

    @Test
    fun `onAlarmSettingInputChanged should update alarmSettingInputState`() = runTest {
        val vibration = true
        val sound = false

        viewModel.onEvent(EditUiEvent.onAlarmSettingInputChanged(vibration, sound))
        advanceUntilIdle()

        assertEquals(AlarmSettingInputState(vibration, sound), viewModel.uiState.value.todoInputState.alarmSettingInputState)
    }

}