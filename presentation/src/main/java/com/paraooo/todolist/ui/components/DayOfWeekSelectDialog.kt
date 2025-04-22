package com.paraooo.todolist.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.paraooo.todolist.ui.features.create.TAG
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.roundedClickable
import kotlinx.coroutines.launch
import java.time.DayOfWeek


data class DayOfWeekState(
    val label : String,
    val value : DayOfWeek,
    val isSelected : Boolean
)

fun getDayOfWeekText(selectedDays: List<Int>): String {
    val weekdays = setOf(
        DayOfWeek.MONDAY.value,
        DayOfWeek.TUESDAY.value,
        DayOfWeek.WEDNESDAY.value,
        DayOfWeek.THURSDAY.value,
        DayOfWeek.FRIDAY.value
    )
    val weekends = setOf(DayOfWeek.SATURDAY.value, DayOfWeek.SUNDAY.value)

    return when {
        selectedDays.isEmpty() -> "Select day of week"
        selectedDays.toSet() == weekdays + weekends -> "Everyday"
        selectedDays.toSet() == weekdays -> "Weekday"
        selectedDays.toSet() == weekends -> "Weekend"
        else -> selectedDays.joinToString(", ") {
            DayOfWeek.of(it).name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        }
    }
}

@Composable
fun DayOfWeekSelectDialog(
    onDismiss: () -> Unit,
    onDaysOfWeekSelected : (daysOfWeek : List<DayOfWeek>) -> Unit,
    showDialog : Boolean
) {

    var daysOfWeekList by remember {
        mutableStateOf(
            listOf(
                DayOfWeekState("Mon", DayOfWeek.MONDAY, false),
                DayOfWeekState("Tue", DayOfWeek.TUESDAY, false),
                DayOfWeekState("Wed", DayOfWeek.WEDNESDAY, false),
                DayOfWeekState("Thu", DayOfWeek.THURSDAY, false),
                DayOfWeekState("Fri", DayOfWeek.FRIDAY, false),
                DayOfWeekState("Sat", DayOfWeek.SATURDAY, false),
                DayOfWeekState("Sun", DayOfWeek.SUNDAY, false),
            )
        )
    }


    if(showDialog){
        Dialog(
            onDismissRequest = {
                onDismiss()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 20.dp)
                ) {
                    Text(
                        "Select days of week",
                        fontSize = 14.sp,
                        color = Color(0xFF4D4D4D),
                        fontWeight = FontWeight.Medium,
                        fontFamily = PretendardFontFamily
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        items(daysOfWeekList) { item: DayOfWeekState ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = when {
                                            item.isSelected -> Color(0xFFE7F6EF)
                                            else -> Color.White
                                        },
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = when {
                                            item.isSelected -> Color(0xFF54C392)
                                            item.value.value == 6 -> Color(0xFFC0C5FF)
                                            item.value.value == 7 -> Color(0xFFFFD4D4)
                                            else -> Color(0xFFF2F2F2)
                                        },
                                        shape = CircleShape
                                    )
                                    .size(44.dp)
                                    .roundedClickable(100.dp) {
                                        daysOfWeekList = daysOfWeekList.map { day ->
                                            if (day.value == item.value) day.copy(isSelected = !day.isSelected)
                                            else day
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.label,
                                    fontSize = 12.sp,
                                    color = when {
                                        item.isSelected -> Color(0xFF54C392)
                                        item.value.value == 6 -> Color(0xFF707CFF)
                                        item.value.value == 7 -> Color(0xFFFF8484)
                                        else -> Color(0xFF777777)
                                    },
                                    fontWeight = when {
                                        item.isSelected -> FontWeight.Medium
                                        else -> FontWeight.Normal
                                    },
                                    fontFamily = PretendardFontFamily,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = getDayOfWeekText(
                            daysOfWeekList
                            .filter { it.isSelected }
                            .map { it.value.value }
                        ),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = {
                            Text(
                                text = "Day of Week",
                                fontFamily = PretendardFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF878787)
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 12.sp,
                            color = when {
                                daysOfWeekList.none { it.isSelected } -> Color(0xFF878787)
                                else -> Color(0xFF4D4D4D)
                            } ,
                            fontWeight = FontWeight.Normal,
                            fontFamily = PretendardFontFamily
                        ),
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFF2F2F2),

                        )
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "SELECT",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4D4D4D),
                        fontFamily = PretendardFontFamily,
                        modifier = Modifier
                            .padding(bottom = 12.dp, end = 10.dp)
                            .roundedClickable(24.dp) {
                                if (!daysOfWeekList.none { it.isSelected }) {
                                    onDaysOfWeekSelected(
                                        daysOfWeekList.filter { it.isSelected }.map { it.value }
                                    )
                                }
                            }
                            .padding(vertical = 8.dp, horizontal = 10.dp)
                    )
                }
            }
        }
    }
}