package com.paraooo.todolist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.paraooo.domain.model.RoutineColorModel
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.roundedClickable
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun TimePickerDialog(
    modifier: Modifier,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: ( result : LocalTime? ) -> Unit,
    color : Long = 0xFF54C392
) {

    if(showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            val selectedHourState = remember { mutableStateOf(LocalTime.now().hour) }
            val selectedMinuteState = remember { mutableStateOf(LocalTime.now().minute) }

            Column(
                modifier = modifier.background(
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp)
                )
            ){

                Spacer(modifier = Modifier.height(60.dp))

                TLTimePicker(
                    initialHour = selectedHourState.value,
                    initialMinute = selectedMinuteState.value,
                    onHourChange = { newHour -> selectedHourState.value = newHour },
                    onMinuteChange = { newMinute -> selectedMinuteState.value = newMinute }
                )

                Spacer(modifier = Modifier.height(60.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 12.dp)
                        .height(53.dp)
                        .background(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFFFFFF)
                        ).border(
                            width = 1.dp,
                            color = Color(0xFF7F7F7F),
                            shape = RoundedCornerShape(12.dp)
                        ).roundedClickable(12.dp) {
                            onConfirm(null)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "시간 설정하지 않음",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color(0xFF7F7F7F)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 12.dp)
                        .height(53.dp)
                        .background(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(color)
                        ).roundedClickable(12.dp) {
                            onConfirm(LocalTime.of(
                                selectedHourState.value,
                                selectedMinuteState.value
                            ))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${selectedHourState.value}시 ${selectedMinuteState.value}분으로 시간 설정하기",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color(0xFFFFFFFF)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}