package com.paraooo.todolist.ui.features.routine_create

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.paraooo.domain.model.RootRoutineModel
import com.paraooo.domain.model.RoutineAlarmType
import com.paraooo.domain.model.RoutineColorModel
import com.paraooo.domain.model.RoutineIconModel
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.components.DateSelectDialog
import com.paraooo.todolist.ui.components.TodoInputForm
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.domain.util.transferMillis2LocalDate
import com.paraooo.todolist.ui.components.DayOfWeekSelectDialog
import com.paraooo.todolist.ui.components.PeriodSelectDialog
import com.paraooo.todolist.ui.components.TLDialog
import com.paraooo.todolist.ui.components.TLTopbar
import com.paraooo.todolist.ui.components.TimeInputState
import com.paraooo.todolist.ui.components.TimePickerDialog
import com.paraooo.todolist.ui.components.TodoInputFormType
import com.paraooo.todolist.ui.features.routine_create.component.RootRoutineCard
import com.paraooo.todolist.ui.util.circleClickable
import com.paraooo.todolist.ui.util.roundedClickable
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.LocalDate


const val TAG = "PARAOOO"

@Composable
fun RoutineCreateScreen(
    navController: NavController
) {

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

        RootRoutineCard(
            RootRoutineModel(
                id = 1,
                name = "아침 운동",
                startTime = 1696444800000L,
                color = RoutineColorModel.RED,
                icon = RoutineIconModel.ICON1,
                subRoutines = listOf(),
                dayOfWeek = listOf(1, 2, 3),
                alarm = RoutineAlarmType.Off
            )
        )

    }
}

@Composable
@Preview
fun PreviewCreateScreen() {
    RoutineCreateScreen(
        rememberNavController(),
    )
}