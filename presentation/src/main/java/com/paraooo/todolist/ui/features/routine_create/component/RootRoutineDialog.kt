package com.paraooo.todolist.ui.features.routine_create.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.RoutineAlarmType
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.components.TLTextButton
import com.paraooo.todolist.ui.components.TLTextField
import com.paraooo.todolist.ui.components.TLTextRadioButton
import com.paraooo.todolist.ui.components.TimeInputState
import com.paraooo.todolist.ui.components.TodoInputFormType
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.computeTextHeight
import com.paraooo.todolist.ui.util.dpToPx
import com.paraooo.todolist.ui.util.roundedClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootRoutineDialog(
    onDismiss: () -> Unit,
    showDialog : Boolean
) {

    val alarmList = listOf(
        RoutineAlarmType.OFF, RoutineAlarmType.VIBRATION, RoutineAlarmType.SOUND
    )

    if (showDialog) {

        BottomSheetDialog (
            onDismissRequest = onDismiss,
            properties = BottomSheetDialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                dismissWithAnimation = true
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = "루틴 생성하기",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color(0xFF545454)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    TLTextField(
                        text = "",
                        onTextChange = {},
                        hintText = "루틴 이름을 입력해주세요",
                        label = "Routine Name"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TLTextButton(
                            modifier = Modifier.weight(1F),
                            labelText = "Time",
                            isActive = false,
                            clickable = {

                            },
                            text = "No Time"
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        TLTextButton(
                            modifier = Modifier.weight(1F),
                            labelText = "DayOfWeek",
                            isActive = false,
                            clickable = {

                            },
                            text = "No Time"
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TLTextRadioButton<RoutineAlarmType>(
                        labelText = "Alarm",
                        radioList = alarmList,
                        currentRadioItem = RoutineAlarmType.SOUND,
                        clickable = { radio ->

                        },
                        getText = { radio -> radio.label }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TLTextButton(
                            modifier = Modifier.weight(1F),
                            labelText = "Color",
                            isActive = false,
                            clickable = {

                            },
                            text = "No Time"
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        TLTextButton(
                            modifier = Modifier.weight(1F),
                            labelText = "Icon",
                            isActive = false,
                            clickable = {

                            },
                            text = "No Time"
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                shape = RoundedCornerShape(12.dp),
                                color = when (true) {
                                    true -> Color(0xFF54C392)
                                    false -> Color(0xFF7F7F7F)
                                }
                            )
                            .roundedClickable(12.dp) {

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = null,
                            modifier = Modifier.padding(vertical = 12.dp).size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}