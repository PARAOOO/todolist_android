package com.paraooo.todolist.ui.features.routine_create

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.paraooo.domain.model.RootRoutineModel
import com.paraooo.domain.model.RoutineAlarmType
import com.paraooo.domain.model.RoutineColorModel
import com.paraooo.domain.model.SubRoutineModel
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.components.DayOfWeekSelectDialog
import com.paraooo.todolist.ui.components.TLTopbar
import com.paraooo.todolist.ui.components.TimePickerDialog
import com.paraooo.todolist.ui.features.create.CreateUiEvent
import com.paraooo.todolist.ui.features.edit.EditUiEffect
import com.paraooo.todolist.ui.features.edit.EditUiEvent
import com.paraooo.todolist.ui.features.routine_create.component.RootRoutineDialog
import com.paraooo.todolist.ui.features.routine_create.component.RoutineList
import com.paraooo.todolist.ui.features.routine_create.component.SubRoutineCard
import com.paraooo.todolist.ui.features.routine_create.component.SubRoutineDialog
import com.paraooo.todolist.ui.util.roundedClickable
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime


const val TAG = "PARAOOO"

@Composable
fun RoutineCreateScreen(
    navController: NavController,
    viewModel : RoutineCreateViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current

//    LaunchedEffect(Unit) {
//        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//            viewModel.effectFlow.collect { effect ->
//                when (effect) {
//                }
//            }
//        }
//    }

    var showRootRoutineDialog by remember { mutableStateOf(false) }
    var showSubRoutineDialog by remember { mutableStateOf(false) }
    var showDayOfWeekPicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val routineList = RootRoutineModel(
        id = 1,
        name = "아침 운동",
        startTime = LocalTime.of(13,20),
        color = RoutineColorModel.COLOR_9977B4,
        icon = 1,
        subRoutines = listOf(
            SubRoutineModel(
                id = 1,
                name = "스쿼트",
                icon = 1,
                time = Duration.ofMinutes(10),
                alarm = RoutineAlarmType.OFF
            ),
            SubRoutineModel(
                id = 2,
                name = "런지",
                icon = 2,
                time = Duration.ofMinutes(20),
                alarm = RoutineAlarmType.OFF
            ),
            SubRoutineModel(
                id = 3,
                name = "팔굽혀펴기",
                icon = 3,
                time = Duration.ofMinutes(5),
                alarm = RoutineAlarmType.OFF
            ),
        ),
        dayOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
        alarm = RoutineAlarmType.OFF
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFF7F7F7)
            )
            .padding(horizontal = 12.dp, vertical = 30.dp)
    ) {
        TLTopbar(
            title = "루틴 생성하기",
            onBackClicked = { navController.popBackStack() }
        )
        
        Spacer(modifier = Modifier.height(28.dp))

        RoutineList(
            routine = uiState.routine
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp)
                .background(
                    shape = RoundedCornerShape(12.dp),
                    color = when (true) {
                        true -> if (uiState.routine == null) Color(0xFF54C392) else Color(uiState.routine!!.color.color)
                        false -> Color(0xFF7F7F7F)
                    }
                )
                .roundedClickable(12.dp) {
                    if(uiState.routine == null){
                        showRootRoutineDialog = true
                    } else {
                        showSubRoutineDialog = true
                    }


                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
        }

    }

    RootRoutineDialog(
        uiState = uiState,
        onDismiss = { showRootRoutineDialog = false },
        showDialog = showRootRoutineDialog,
        onRoutineNameChanged = { routineName ->
            viewModel.onEvent(RoutineCreateUiEvent.onRootNameChanged(routineName))
        },
        onDayOfWeekClicked = { showDayOfWeekPicker = true },
        onTimeClicked = { showTimePicker = true },
        onAlarmChanged = { alarm ->
            viewModel.onEvent(RoutineCreateUiEvent.onRootAlarmChanged(alarm))
        },
        onColorChanged = { color ->
            viewModel.onEvent(RoutineCreateUiEvent.onRootColorChanged(color))
        },
        onIconChanged = { icon ->
            viewModel.onEvent(RoutineCreateUiEvent.onRootIconChanged(icon))
        },
        onCreateClicked = {
            viewModel.onEvent(RoutineCreateUiEvent.onRootCreateClicked)
            showRootRoutineDialog = false
        }
    )

    SubRoutineDialog(
        uiState = uiState,
        onDismiss = { showSubRoutineDialog = false },
        showDialog = showSubRoutineDialog,
        onRoutineNameChanged = {
            viewModel.onEvent(RoutineCreateUiEvent.onSubNameChanged(it))
        },
        onTimeClicked = {
            showTimePicker = true
        },
        onAlarmChanged = {
            viewModel.onEvent(RoutineCreateUiEvent.onSubAlarmChanged(it))
        },
        onIconChanged = {
            viewModel.onEvent(RoutineCreateUiEvent.onSubIconChanged(it))
        },
        onCreateClicked = {
            viewModel.onEvent(RoutineCreateUiEvent.onSubCreateClicked)
            showSubRoutineDialog = false
        }
    )

    DayOfWeekSelectDialog(
        showDialog = showDayOfWeekPicker,
        onDismiss = { showDayOfWeekPicker = false },
        onDaysOfWeekSelected = { daysOfWeek : List<DayOfWeek> ->
            showDayOfWeekPicker = false
            viewModel.onEvent(RoutineCreateUiEvent.onRootDayOfWeekChanged(daysOfWeek))
        },
        color = uiState.rootRoutineInput.color.color
    )

    TimePickerDialog(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        showDialog = showTimePicker,
        onDismiss = { showTimePicker = false },
        onConfirm = { result : LocalTime? ->
            showTimePicker = false
            viewModel.onEvent(RoutineCreateUiEvent.onRootTimeChanged(result))
        },
        color = uiState.rootRoutineInput.color.color
    )
}

@Composable
@Preview
fun PreviewCreateScreen() {
    RoutineCreateScreen(
        rememberNavController(),
    )
}