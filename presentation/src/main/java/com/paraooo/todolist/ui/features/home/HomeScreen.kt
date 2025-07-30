package com.paraooo.todolist.ui.features.home

import android.app.ActivityManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.paraooo.domain.model.TodoModel
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.components.TLBoxRounded
import com.paraooo.todolist.ui.components.DateSelectDialog
import com.paraooo.todolist.ui.features.home.component.DateSelector
import com.paraooo.todolist.ui.features.home.component.TodoVerticalList
import com.paraooo.todolist.ui.navigation.Destinations
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.domain.util.getDateDiff
import com.paraooo.domain.util.transferMillis2LocalDate
import com.paraooo.todolist.ui.components.TLDialog
import com.paraooo.todolist.ui.features.home.component.CreateFloatingButton
import com.paraooo.todolist.ui.features.home.component.TodoVerticalListSkeleton
import com.paraooo.todolist.ui.util.circleClickable
import com.paraooo.todolist.ui.util.getDateWithDot
import com.paraooo.todolist.ui.util.roundedClickable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

const val TAG = "PARAOOO"


@Composable
fun HomeScreen(
    navController : NavController,
    viewModel : HomeViewModel = koinViewModel()
) {

    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    var selectedDateString = remember(uiState.selectedDateState.date) {
        getDateWithDot(uiState.selectedDateState.date)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(
        pageCount = { Int.MAX_VALUE },
        initialPage = Int.MAX_VALUE/2 - 3
    )

    var currentPageDebounced by remember { mutableIntStateOf(Int.MAX_VALUE/2 - 3) }

    LaunchedEffect(viewModel.effectFlow, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effectFlow.collect { effect ->
                when (effect) {
                    is HomeUiEffect.onDeleteTodoSuccess -> {
                        Toast.makeText(context, "Todo가 정상적으로 삭제되었습니다.", Toast.LENGTH_LONG).show()
                    }
                    is HomeUiEffect.onScrollToPage -> {
                        showDatePicker = false
                        pagerState.animateScrollToPage(effect.page)
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xFFF7F7F7)
                )
                .padding(horizontal = 12.dp, vertical = 30.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Image(
                    painter = painterResource(R.drawable.ic_logo_text),
                    contentDescription = "logo on top-left",
                    modifier = Modifier.width(150.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Image(
                        painter = painterResource(R.drawable.ic_routine),
                        contentDescription = "routine button",
                        modifier = Modifier
                            .size(24.dp)
                            .circleClickable(20.dp) {
                                navController.navigate(Destinations.RoutineCreate.route)
                            }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Image(
                        painter = painterResource(R.drawable.ic_setting),
                        contentDescription = "setting button",
                        modifier = Modifier
                            .size(24.dp)
                            .circleClickable(20.dp) {
                                navController.navigate(Destinations.Setting.route)
                            }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            TLBoxRounded(
                modifier = Modifier
                    .fillMaxWidth()
                    .roundedClickable(12.dp) {
                        showDatePicker = true
                    }
            ) {
                Text(
                    text = selectedDateString,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color(0xFF7F7F7F),
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                )
            }

            DateSelectDialog(
                showDialog = showDatePicker,
                onDismiss = { showDatePicker = false },
                onDateSelected = { date: Long? ->

                    viewModel.onEvent(HomeUiEvent.onDateChangedWithDialog(date))

                }
            )

            Spacer(modifier = Modifier.height(5.dp))

            DateSelector(
                parentPaddingHorizontal = 12.dp,
                onDateChange = { date: LocalDate ->
                    viewModel.onEvent(HomeUiEvent.onDateChanged(date))
                },
                pagerState = pagerState,
                currentPageDebounced = currentPageDebounced,
                setCurrentPageDebounced = { page: Int ->
                    currentPageDebounced = page
                }
            )

            when {
                uiState.todoListState.isLoading -> {
                    TodoVerticalListSkeleton()
                }

                uiState.todoListState.error.isNotEmpty() -> {
                    Text(
                        text = uiState.todoListState.error
                    )
                }

                else -> {
                    Log.d(TAG, "TodoVerticalList - else: ${uiState.todoListState.todoList}")
                    TodoVerticalList(
                        uiState.todoListState.todoList,
                        onIsSwipedChanged = { todo : TodoModel, isSwiped: Boolean ->
                            viewModel.onEvent(
                                HomeUiEvent.onIsSwipedChanged(
                                    todo, isSwiped
                                )
                            )
                        },
                        onProgressChanged = { todo : TodoModel, angle: Float ->
                            viewModel.onEvent(
                                HomeUiEvent.onTodoProgressChanged(
                                    todo, angle
                                )
                            )
                        },
                        onDeleteClicked = { todo : TodoModel ->
                            viewModel.selectedTodo.value = todo
                            showDeleteDialog = true
                        },
                        onEditClicked = { todo : TodoModel ->
                            viewModel.selectedTodo.value = todo
                            navController.navigate("${Destinations.Edit.route}/${todo.instanceId}")
                        },
                        onIsToggledOpenedChanged = { todo : TodoModel, isToggledOpened: Boolean ->
                            viewModel.onEvent(
                                HomeUiEvent.onIsToggleOpenedChanged(
                                    todo, isToggledOpened
                                )
                            )
                        }
                    )
                }
            }
        }

        if(viewModel.selectedTodo.value != null){
            TLDialog(
                showDialog = showDeleteDialog,
                onDismiss = { showDeleteDialog = false },
                content = "\"${viewModel.selectedTodo.value!!.title}\" Todo를 정말로 삭제하시겠습니까?",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
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
                            showDeleteDialog = false
                        }
                    )

                    Spacer(modifier = Modifier.width(40.dp))

                    Text(
                        text = "확인",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color(0xFFFF6E6E),
                        modifier = Modifier.circleClickable(20.dp) {
                            showDeleteDialog = false
                            viewModel.onEvent(
                                HomeUiEvent.onTodoDeleteClicked(
                                    todo = viewModel.selectedTodo.value!!
                                )
                            )
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp, bottom = 20.dp),
            contentAlignment = Alignment.BottomEnd
        ){
            CreateFloatingButton(
                onClick = {
                    navController.navigate("${Destinations.Create.route}/${uiState.selectedDateState.date.toEpochDay()}")
                }
            )
        }
    }
}

@Composable
@Preview
fun PreviewHomeScreen() {
    HomeScreen(
        rememberNavController()
    )
}