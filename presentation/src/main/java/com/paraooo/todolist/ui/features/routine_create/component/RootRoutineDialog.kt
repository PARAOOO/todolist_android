package com.paraooo.todolist.ui.features.routine_create.component

import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.RoutineAlarmType
import com.paraooo.domain.model.RoutineColorModel
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.components.ColorPickerPopup
import com.paraooo.todolist.ui.components.IconPickerPopup
import com.paraooo.todolist.ui.components.TLTextButton
import com.paraooo.todolist.ui.components.TLTextField
import com.paraooo.todolist.ui.components.TLTextRadioButton
import com.paraooo.todolist.ui.components.TodoInputFormType
import com.paraooo.todolist.ui.components.getDayOfWeekText
import com.paraooo.todolist.ui.components.longToColorHexString
import com.paraooo.todolist.ui.features.home.component.vectorToBitmap
import com.paraooo.todolist.ui.features.routine_create.RoutineCreateUiEvent
import com.paraooo.todolist.ui.features.routine_create.RoutineCreateUiState
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.computeTextHeight
import com.paraooo.todolist.ui.util.dpToPx
import com.paraooo.todolist.ui.util.getRoutineIconDrawableId
import com.paraooo.todolist.ui.util.roundedClickable
import com.paraooo.todolist.ui.util.toDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootRoutineDialog(
    uiState: RoutineCreateUiState,
    onDismiss: () -> Unit,
    showDialog : Boolean,
    onRoutineNameChanged: (String) -> Unit,
    onDayOfWeekClicked : () -> Unit,
    onTimeClicked : () -> Unit,
    onAlarmChanged : (alarm : RoutineAlarmType) -> Unit,
    onColorChanged : (color : RoutineColorModel) -> Unit,
    onIconChanged : (icon : Int) -> Unit,
    onCreateClicked : () -> Unit
) {

    val alarmList = listOf(
        RoutineAlarmType.OFF, RoutineAlarmType.VIBRATION, RoutineAlarmType.SOUND
    )

    val context = LocalContext.current

    var isColorPopupVisible by remember { mutableStateOf(false) }
    var isIconPopupVisible by remember { mutableStateOf(false) }

    var lastColorClickTime = 0L

    if (showDialog) {

        ModalBottomSheet (
            onDismissRequest = onDismiss,
            dragHandle = null
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
                        text = uiState.rootRoutineInput.routineName,
                        onTextChange = onRoutineNameChanged,
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
                            isActive = uiState.rootRoutineInput.time != null,
                            clickable = {
                                onTimeClicked()
                            },
                            text = when (uiState.rootRoutineInput.time) {
                                null -> "시간 설정하지 않음"
                                else -> "${uiState.rootRoutineInput.time.hour}시 ${uiState.rootRoutineInput.time.minute}분"
                            },
                            activeColor = uiState.rootRoutineInput.color.color
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        TLTextButton(
                            modifier = Modifier.weight(1F),
                            labelText = "DayOfWeek",
                            isActive = true,
                            clickable = {
                                onDayOfWeekClicked()
                            },
                            text = getDayOfWeekText(uiState.rootRoutineInput.dayOfWeek.map { it.value }),
                            activeColor = uiState.rootRoutineInput.color.color
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TLTextRadioButton<RoutineAlarmType>(
                        labelText = "Alarm",
                        radioList = alarmList,
                        currentRadioItem = uiState.rootRoutineInput.alarmType,
                        clickable = { radio ->
                            onAlarmChanged(radio)
                        },
                        getText = { radio -> radio.label },
                        activeColor = uiState.rootRoutineInput.color.color
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        ColorPickerPopup(
                            currentColor = uiState.rootRoutineInput.color,
                            onDismissRequest = {
                                isColorPopupVisible = false
                                lastColorClickTime = System.currentTimeMillis()
                            },
                            isColorPopupVisible = isColorPopupVisible,
                            numberOfRows = 2,
                            onColorSelected = { color ->
                                onColorChanged(color)
                            },
                        )

                        TLTextButton(
                            modifier = Modifier.weight(1F),
                            labelText = "Color",
                            isActive = true,
                            clickable = {
                                if(System.currentTimeMillis() - lastColorClickTime > 100){
                                    isColorPopupVisible = true
                                }
                            },
                            text = longToColorHexString(uiState.rootRoutineInput.color.color),
                            activeColor = uiState.rootRoutineInput.color.color
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1F)) {

                            IconPickerPopup(
                                isIconPopupVisible = isIconPopupVisible,
                                onDismissRequest = {
                                    isIconPopupVisible = false
                                    lastColorClickTime = System.currentTimeMillis()
                                },
                                onIconSelected = { icon ->
                                    onIconChanged(icon)
                                    isIconPopupVisible = false
                                },
                                color = uiState.rootRoutineInput.color.color
                            )

                            Text(
                                "Icon",
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
                                            color = Color(uiState.rootRoutineInput.color.color)
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .roundedClickable(12.dp) {
                                        if(System.currentTimeMillis() - lastColorClickTime > 100){
                                            isIconPopupVisible = true
                                        }
                                    }
                            ) {
                                Image(
                                    painter = painterResource(getRoutineIconDrawableId(uiState.rootRoutineInput.icon)),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(
                                        Color(uiState.rootRoutineInput.color.color),
                                        BlendMode.SrcIn
                                    ),
                                    modifier = Modifier
                                        .padding(vertical = 5.dp)
                                        .size(13.sp.toDp() + 10.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                shape = RoundedCornerShape(12.dp),
                                color = when (true) {
                                    true -> Color(uiState.rootRoutineInput.color.color)
                                    false -> Color(0xFF7F7F7F)
                                }
                            )
                            .roundedClickable(12.dp) {
                                onCreateClicked()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}