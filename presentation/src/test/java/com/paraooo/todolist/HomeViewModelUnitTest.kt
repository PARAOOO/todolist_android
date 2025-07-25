package com.paraooo.todolist

import android.util.Log
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.todolist.ui.features.home.HomeUiEffect
import com.paraooo.todolist.ui.features.home.HomeUiEvent
import com.paraooo.todolist.ui.features.home.HomeUiState
import com.paraooo.todolist.ui.features.home.HomeViewModel
import com.paraooo.todolist.ui.features.home.TodoListState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelUnitTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var todoWriteRepository: TodoWriteRepository
    private lateinit var todoReadRepository: TodoReadRepository

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
        todoReadRepository = mockk()
        viewModel = HomeViewModel(todoWriteRepository, todoReadRepository)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onDateChanged updates uiState with todo list`() = runTest {
        val date = LocalDate.of(2000, 1, 10)
        val todoList = listOf(
            sampleTodoModel.copy(title = "Test Todo 1"),
            sampleTodoModel.copy(title = "Test Todo 2"),
        )
        coEvery { todoReadRepository.getTodoByDate(any()) } returns flowOf(todoList)

        viewModel.onEvent(HomeUiEvent.onDateChanged(date))
        advanceUntilIdle()

        assertEquals(date, viewModel.uiState.value.selectedDateState.date)
        assertEquals(todoList, viewModel.uiState.value.todoListState.todoList)
        assertEquals(false, viewModel.uiState.value.todoListState.isLoading)
        assertEquals("", viewModel.uiState.value.todoListState.error)
    }

    @Test
    fun `onIsSwipedChanged updates todo list with isSwiped value`() = runTest {

        val todoList = listOf(
            sampleTodoModel.copy(instanceId = 1L, title = "Test Todo 1", isSwiped = false),
            sampleTodoModel.copy(instanceId = 2L, title = "Test Todo 2"),
        )
        val customViewModel = HomeViewModel(todoWriteRepository, todoReadRepository, HomeUiState(
            todoListState = TodoListState(false, todoList, "")
        ))
        val todo = sampleTodoModel.copy(instanceId = 1L, title = "Test Todo 1")
        val isSwiped = true

        customViewModel.onEvent(HomeUiEvent.onIsSwipedChanged(todo, isSwiped))
        advanceUntilIdle()

        assertEquals(isSwiped, customViewModel.uiState.value.todoListState.todoList.find { it.instanceId == todo.instanceId }?.isSwiped)
    }

    @Test
    fun `onTodoProgressChanged updates todo list with progress value`() = runTest {
        val todoList = listOf(
            sampleTodoModel.copy(instanceId = 1L, title = "Test Todo 1", progressAngle = 0F),
            sampleTodoModel.copy(instanceId = 2L, title = "Test Todo 2"),
        )
        val customViewModel = HomeViewModel(todoWriteRepository, todoReadRepository, HomeUiState(
            todoListState = TodoListState(false, todoList, "")
        ))

        val todo = sampleTodoModel.copy(instanceId = 1L, title = "Test Todo 1")
        val progress = 90F

        coEvery { todoWriteRepository.updateTodoProgress(any(), any()) } just runs

        customViewModel.onEvent(HomeUiEvent.onTodoProgressChanged(todo, progress))
        advanceUntilIdle()

        assertEquals(progress, customViewModel.uiState.value.todoListState.todoList.find { it.instanceId == todo.instanceId }?.progressAngle)
        coVerify { todoWriteRepository.updateTodoProgress(todo.instanceId, progress) }
    }

    @Test
    fun `onTodoDeleteClicked deletes todo from repository`() = runTest {
        val instanceId = 1L
        val todoList = listOf(
            sampleTodoModel.copy(instanceId = 2L, title = "Test Todo 2"),
        )

        coEvery { todoWriteRepository.deleteTodoById(any()) } just Runs
        coEvery { todoReadRepository.getTodoByDate(any()) } returns flowOf(todoList)

        viewModel.onEvent(HomeUiEvent.onTodoDeleteClicked(
            sampleTodoModel.copy(instanceId = instanceId, title = "Test Todo 1")
        ))
        val effect = viewModel.effectFlow.first()
        advanceUntilIdle()

        assertEquals(HomeUiEffect.onDeleteTodoSuccess, effect)
        assertEquals(todoList, viewModel.uiState.value.todoListState.todoList)
        coVerify { todoWriteRepository.deleteTodoById(instanceId) }
    }

    @Test
    fun `onIsToggleOpenedChanged updates todo list with isToggleOpened value`() = runTest {
        val todoList = listOf(
            sampleTodoModel.copy(instanceId = 1L, title = "Test Todo 1", isToggleOpened = false),
            sampleTodoModel.copy(instanceId = 2L, title = "Test Todo 2"),
        )
        val customViewModel = HomeViewModel(todoWriteRepository, todoReadRepository, HomeUiState(
            todoListState = TodoListState(false, todoList, "")
        ))
        val todo = sampleTodoModel.copy(instanceId = 1L, title = "Test Todo 1")
        val isToggleOpened = true

        customViewModel.onEvent(HomeUiEvent.onIsToggleOpenedChanged(todo, isToggleOpened))

        advanceUntilIdle()

        assertEquals(isToggleOpened, customViewModel.uiState.value.todoListState.todoList.find { it.instanceId == todo.instanceId }?.isToggleOpened)
    }


}