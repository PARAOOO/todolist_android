package com.paraooo.todolist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TLTimePicker(
    initialHour: Int = LocalTime.now().hour, // 기본값: 현재 시간의 시
    initialMinute: Int = LocalTime.now().minute, // 기본값: 현재 시간의 분
    onHourChange: (Int) -> Unit = {}, // 시 변경 콜백
    onMinuteChange: (Int) -> Unit = {} // 분 변경 콜백
) {
    val hours = List(1000) { it % 24 } // 0~23 반복
    val minutes = List(1000) { it % 60 } // 0~59 반복

    // 초기 스크롤 위치
    val initialHourIndex = (500 / 24) * 24 + initialHour
    val initialMinuteIndex = (500 / 60) * 60 + initialMinute

    val hourListState = rememberLazyListState(initialHourIndex)
    val minuteListState = rememberLazyListState(initialMinuteIndex)

    // 현재 선택된 값 감지 & 변경 이벤트 발생
    val selectedHour by remember { derivedStateOf { hours[hourListState.firstVisibleItemIndex] } }
    val selectedMinute by remember { derivedStateOf { minutes[minuteListState.firstVisibleItemIndex] } }

    // 선택된 값이 변경될 때 콜백 호출
    LaunchedEffect(selectedHour) { onHourChange(selectedHour) }
    LaunchedEffect(selectedMinute) { onMinuteChange(selectedMinute) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        WheelPicker(
            modifier = Modifier.weight(1F),
            listState = hourListState,
            items = hours,
            selectedItem = selectedHour,
            unit = "시"
        )
        Spacer(modifier = Modifier.width(16.dp))
        WheelPicker(
            modifier = Modifier.weight(1F),
            listState = minuteListState,
            items = minutes,
            selectedItem = selectedMinute,
            unit = "분"
        )
    }
}

@Composable
fun WheelPicker(
    modifier: Modifier,
    listState: LazyListState,
    items: List<Int>,
    selectedItem: Int,
    unit: String,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(vertical = 80.dp),
        flingBehavior = rememberSnapFlingBehavior(listState),
        modifier = modifier.height(200.dp)
    ) {
        items(items) { item ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = "$item$unit",
                    fontSize = 23.sp,
                    fontWeight = if (item == selectedItem) FontWeight.Medium else FontWeight.Normal,
                    color = if (item == selectedItem) Color.Black else Color(0xFFD0D0D0)
                )
            }
        }
    }
}
