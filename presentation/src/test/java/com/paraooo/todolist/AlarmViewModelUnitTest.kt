package com.paraooo.todolist

import android.util.Log
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.todolist.ui.features.alarm.AlarmUiEvent
import com.paraooo.todolist.ui.features.alarm.AlarmViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AlarmViewModelUnitTest {

    private lateinit var viewModel: AlarmViewModel
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
        todoReadRepository = mockk()
        viewModel = AlarmViewModel(todoReadRepository)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onInit should update uiState with todoTemplate data`() = runTest {
        val instanceId = 1L
        val todo = sampleTodoModel.copy(
            instanceId = instanceId, title = "Test Todo"
        )

        coEvery { todoReadRepository.findTodoById(any()) } returns todo
        viewModel.onEvent(AlarmUiEvent.onInit(instanceId))
        advanceUntilIdle()

        coVerify { todoReadRepository.findTodoById(instanceId) }
        assertEquals(viewModel.uiState.value.todoName, todo.title)
    }

}