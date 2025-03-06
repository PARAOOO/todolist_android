package com.paraooo.todolist.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraooo.domain.util.getDateWithDot
import com.paraooo.todolist.ui.util.roundedClickable
import java.time.LocalDate


data class TodoNameInputState (
    val content : String = "",
    val isValid : Boolean = false
)

data class DescriptionInputState (
    val content : String = ""
)

sealed class TimeInputState {
    data class Time(val hour : Int, val minute : Int) : TimeInputState()
    data object NoTime : TimeInputState()
}

data class DateInputState (
    val date : LocalDate = LocalDate.now()
)

data class TodoUiState(
    val todoNameInputState : TodoNameInputState = TodoNameInputState(),
    val descriptionInputState : DescriptionInputState = DescriptionInputState(),
    val timeInputState: TimeInputState = TimeInputState.NoTime,
    val dateInputState : DateInputState = DateInputState(),
)

@Composable
fun TodoInputForm(
    uiState: TodoUiState,
    onTodoNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTimeInputClicked: () -> Unit,
    onDateInputClicked: () -> Unit
) {
    TLBoxRounded (
        modifier = Modifier.fillMaxWidth().padding(18.dp)
    ){
        Column(modifier = Modifier.fillMaxWidth()) {
            TLTextField(
                text = uiState.todoNameInputState.content,
                onTextChange = onTodoNameChange,
                hintText = "Todo 이름을 입력해주세요",
                label = "Todo Name"
            )

            Spacer(modifier = Modifier.height(8.dp))

            TLTextField(
                text = uiState.descriptionInputState.content,
                onTextChange = onDescriptionChange,
                hintText = "Todo 설명을 입력해주세요",
                label = "Description",
                singleLine = false,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1F)) {
                    Text("Time", fontSize = 14.sp, color = Color(0xFF7F7F7F))

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                border = BorderStroke(
                                    1.dp,
                                    color = if (uiState.timeInputState is TimeInputState.Time) Color(
                                        0xFF54C392
                                    ) else Color(0xFFECEEEE)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .roundedClickable(12.dp) {
                                onTimeInputClicked()
                            }
                    ) {
                        Text(
                            text = when (uiState.timeInputState) {
                                is TimeInputState.NoTime -> "시간 설정하지 않음"
                                is TimeInputState.Time -> "${(uiState.timeInputState as TimeInputState.Time).hour}시 ${(uiState.timeInputState as TimeInputState.Time).minute}분"
                            },
                            fontSize = 12.sp,
                            color = if (uiState.timeInputState is TimeInputState.Time) Color(
                                0xFF54C392
                            ) else Color(0xFF7F7F7F),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1F)) {
                    Text("Date", fontSize = 14.sp, color = Color(0xFF7F7F7F))

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                border = BorderStroke(1.dp, color = Color(0xFF54C392)),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .roundedClickable(12.dp) {
                                onDateInputClicked()
                            }
                    ) {
                        Text(
                            text = getDateWithDot(uiState.dateInputState.date),
                            fontSize = 12.sp,
                            color = Color(0xFF54C392),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}
