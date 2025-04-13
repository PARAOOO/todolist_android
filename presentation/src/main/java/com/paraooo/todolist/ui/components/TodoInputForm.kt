package com.paraooo.todolist.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.room.ColumnInfo
import com.paraooo.domain.model.AlarmType
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.computeTextHeight
import com.paraooo.todolist.ui.util.dpToPx
import com.paraooo.todolist.ui.util.getDateWithDot
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

sealed class DateInputState {
    data class Date(val date : LocalDate) : DateInputState()
    data class Period(val startDate : LocalDate, val endDate : LocalDate) : DateInputState()
    data class DayOfWeek(val dayOfWeek : List<Int>) : DateInputState()
}

data class AlarmInputState (
    val alarmType : AlarmType = AlarmType.OFF
)

data class TodoInputState(
    val todoNameInputState : TodoNameInputState = TodoNameInputState(),
    val descriptionInputState : DescriptionInputState = DescriptionInputState(),
    val timeInputState: TimeInputState = TimeInputState.NoTime,
    val dateInputState : DateInputState = DateInputState.Date(LocalDate.now()),
    val alarmInputState : AlarmInputState = AlarmInputState()
)

sealed class TodoInputFormType {
    data class Add(
        val onDateInputClicked: () -> Unit,
        val onPeriodInputClicked: () -> Unit,
        val onDayOfWeekInputClicked: () -> Unit,
    ) : TodoInputFormType()

    data class Edit(
        val onDateInputClicked: () -> Unit,
    ) : TodoInputFormType()

    data class PeriodEdit(
        val onPeriodInputClicked: () -> Unit
    ) : TodoInputFormType()

    data class DayOfWeekEdit(
        val onDayOfWeekInputClicked: () -> Unit
    ) : TodoInputFormType()
}

@Composable
fun TodoInputForm(
    uiState: TodoInputState,
    onTodoNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTimeInputClicked: () -> Unit,
    onAlarmChange: (alarm : AlarmType) -> Unit,
    type : TodoInputFormType
) {

    var isPopupVisible by remember { mutableStateOf(false) }
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val alarmList = listOf(
        AlarmType.OFF, AlarmType.NOTIFY, AlarmType.POPUP
    )
//    var alarmState by remember { mutableStateOf(alarmList[0]) }


    fun getTextOfDateInput(date : DateInputState) : String {
        when(date) {
            is DateInputState.Date -> {
                return getDateWithDot(date.date)
            }
            is DateInputState.Period -> {
                return "${getDateWithDot(date.startDate)} ~ ${getDateWithDot(date.endDate)}"
            }
            is DateInputState.DayOfWeek -> {
                return getDayOfWeekText(date.dayOfWeek)
            }
        }
    }

    TLBoxRounded (
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
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
                    Text(
                        "Time",
                        fontSize = 14.sp,
                        color = Color(0xFF7F7F7F),
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                    )

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
                                is TimeInputState.Time -> "${uiState.timeInputState.hour}시 ${uiState.timeInputState.minute}분"
                            },
                            fontSize = 12.sp,
                            color = if (uiState.timeInputState is TimeInputState.Time) Color(
                                0xFF54C392
                            ) else Color(0xFF7F7F7F),
                            modifier = Modifier.padding(vertical = 10.dp),
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1F)) {
                    Text(
                        "Date",
                        fontSize = 14.sp,
                        color = Color(0xFF7F7F7F),
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                    )

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
                                when (type) {
                                    is TodoInputFormType.Add -> {
                                        isPopupVisible = !isPopupVisible
                                    }

                                    is TodoInputFormType.Edit -> {
                                        type.onDateInputClicked()
                                    }

                                    is TodoInputFormType.PeriodEdit -> {
                                        type.onPeriodInputClicked()
                                    }

                                    is TodoInputFormType.DayOfWeekEdit -> {
                                        type.onDayOfWeekInputClicked()
                                    }
                                }
                            }
                    ) {
                        Text(
                            text = getTextOfDateInput(uiState.dateInputState),
                            fontSize = 12.sp,
                            color = Color(0xFF54C392),
                            modifier = Modifier.padding(vertical = 10.dp),
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Normal,
                        )
                    }

                    if(isPopupVisible && type is TodoInputFormType.Add){

                        val popupWidth = ((screenWidthDp-24-36-8) / 2).dp
                        val popupTextHeight = computeTextHeight(
                            textStyle = TextStyle(
                                fontSize = 12.sp,
                                color = Color(0xFF54C392),
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                            ),
                            text = "샘플 텍스트",
                            maxLines = 1
                        )

                        val popupTextList = arrayListOf(
                            "날짜 지정하기", "기간 지정하기", "요일 지정하기"
                        )

                        val popupActionList = arrayListOf(
                            { type.onDateInputClicked() },
                            { type.onPeriodInputClicked() },
                            { type.onDayOfWeekInputClicked() }
                        )

                        val popupHeight = (popupTextHeight + dpToPx(20.dp) + dpToPx(2.dp)) * popupTextList.size


                        Popup (
                            alignment = Alignment.BottomCenter,
                            onDismissRequest = { isPopupVisible = false },
                            offset = IntOffset(
                                y = popupHeight,
                                x = 0
                            )
                        ) {

                            LazyColumn {
                                items(popupTextList.size) { item ->
                                    Spacer(modifier = Modifier.height(2.dp))

                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .width(popupWidth)
                                            .border(
                                                border = BorderStroke(
                                                    1.dp,
                                                    color = Color(0xFFECEEEE)
                                                ),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .background(
                                                color = Color.White,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .roundedClickable(12.dp) {
                                                isPopupVisible = false
                                                popupActionList[item]()
                                            }
                                    ) {
                                        Text(
                                            text = popupTextList[item],
                                            fontSize = 12.sp,
                                            color = Color(0xFF7F7F7F),
                                            modifier = Modifier.padding(vertical = 10.dp),
                                            fontFamily = PretendardFontFamily,
                                            fontWeight = FontWeight.Normal,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Alarm",
                    fontSize = 14.sp,
                    color = Color(0xFF7F7F7F),
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),

                ) {
                    for(alarm in alarmList){

                        val isSelected = alarm == uiState.alarmInputState.alarmType

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1F)
                                .fillMaxWidth()
                                .border(
                                    border = BorderStroke(
                                        1.dp,
                                        color = if (isSelected) Color(
                                            0xFF54C392
                                        ) else Color(0xFFECEEEE)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .roundedClickable(12.dp) {
                                    onAlarmChange(alarm)
                                }
                        ) {
                            Text(
                                text = alarm.label,
                                fontSize = 12.sp,
                                color = if (isSelected) Color(
                                    0xFF54C392
                                ) else Color(0xFF7F7F7F),
                                modifier = Modifier.padding(vertical = 10.dp),
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                            )
                        }
                    }
                }
            }
        }
    }
}
