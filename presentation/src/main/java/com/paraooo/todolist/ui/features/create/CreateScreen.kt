package com.paraooo.todolist.ui.features.create

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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.components.DateSelectDialog
import com.paraooo.todolist.ui.components.TodoInputForm
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.domain.util.transferMillis2LocalDate
import com.paraooo.todolist.ui.components.PeriodSelectDialog
import com.paraooo.todolist.ui.components.TLDialog
import com.paraooo.todolist.ui.components.TLTopbar
import com.paraooo.todolist.ui.components.TimeInputState
import com.paraooo.todolist.ui.components.TimePickerDialog
import com.paraooo.todolist.ui.components.TodoInputFormType
import com.paraooo.todolist.ui.util.circleClickable
import com.paraooo.todolist.ui.util.roundedClickable
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate


const val TAG = "PARAOOO"

@Composable
fun CreateScreen(
    navController: NavController,
    selectedDate : LocalDate,
    viewModel : CreateViewModel = koinViewModel()
) {

    LaunchedEffect(selectedDate) {
        viewModel.onEvent(CreateUiEvent.onSelectedDateChanged(selectedDate))
    }

    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showPeriodPicker by remember { mutableStateOf(false) }
    var showBackDialog by remember { mutableStateOf(false) }

    var snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effectFlow.collectLatest { effect ->
            when (effect) {
                is CreateUiEffect.onPostTodoSuccess -> {
                    navController.popBackStack()
                    Toast.makeText(context, "\"${effect.todoTitle}\" Todo 생성이 정상적으로 완료되었습니다.", Toast.LENGTH_LONG).show()
//                    snackbarHostState.showSnackbar("\"${effect.todoTitle}\" Todo 생성이 정상적으로 완료되었습니다.")
                }
            }
        }
    }

    BackHandler {
        showBackDialog = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFF7F7F7)
            )
            .padding(horizontal = 12.dp, vertical = 30.dp)
    ) {
        TLTopbar(
            title = "Todo 생성하기",
            onBackClicked = {showBackDialog = true}
        )
        
        Spacer(modifier = Modifier.height(28.dp))

        TodoInputForm(
            uiState = uiState.todoInputState,
            onTodoNameChange = { viewModel.onEvent(CreateUiEvent.onTodoNameInputChanged(it)) },
            onDescriptionChange = { viewModel.onEvent(CreateUiEvent.onDescriptionInputChanged(it)) },
            onTimeInputClicked = { showTimePicker = true },
            type = TodoInputFormType.Add(
                onDateInputClicked = { showDatePicker = true },
                onPeriodInputClicked = { showPeriodPicker = true },
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp)
                .background(
                    shape = RoundedCornerShape(12.dp),
                    color = when (uiState.createButtonState.isValid && uiState.createButtonState.isEnable) {
                        true -> Color(0xFF54C392)
                        false -> Color(0xFF7F7F7F)
                    }
                )
                .roundedClickable(12.dp) {
                    if(uiState.createButtonState.isValid && uiState.createButtonState.isEnable){
                        viewModel.onEvent(CreateUiEvent.onCreateClicked)
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

        TimePickerDialog(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            showDialog = showTimePicker,
            onDismiss = { showTimePicker = false },
            onConfirm = { result : TimeInputState ->
                showTimePicker = false
                viewModel.onEvent(CreateUiEvent.onTimeInputChanged(result))
            }
        )

        DateSelectDialog(
            showDialog = showDatePicker,
            onDismiss = { showDatePicker = false },
            onDateSelected = { date: Long? ->
                showDatePicker = false
                viewModel.onEvent(CreateUiEvent.onDateInputChanged(transferMillis2LocalDate(date)))
            }
        )
        
        PeriodSelectDialog(
            showDialog = showPeriodPicker,
            onDismiss = { showPeriodPicker = false },
            onPeriodSelected = { startDate : Long?, endDate : Long? ->
                showPeriodPicker = false
                viewModel.onEvent(CreateUiEvent.onPeriodInputChanged(
                    transferMillis2LocalDate(startDate),
                    transferMillis2LocalDate(endDate)
                ))
            }
        )

        TLDialog(
            showDialog = showBackDialog,
            onDismiss = { showBackDialog = false },
            content = "확인을 누르면 이전 화면으로 돌아갑니다.\n작성 중인 Todo는 저장되지 않습니다.",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "취소",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF545454),
                    modifier = Modifier.circleClickable(20.dp) {
                        showBackDialog = false
                    }
                )

                Spacer(modifier = Modifier.width(40.dp))

                Text(
                    text = "확인",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF545454),
                    modifier = Modifier.circleClickable(20.dp) {
                        showBackDialog = false
                        navController.popBackStack()
                    }
                )
            }
        }

//        TLSnackbarHost(snackbarHostState)
    }
}

@Composable
@Preview
fun PreviewCreateScreen() {
    CreateScreen(
        rememberNavController(),
        selectedDate = LocalDate.now()
    )
}