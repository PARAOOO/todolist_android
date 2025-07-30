package com.paraooo.todolist.ui.features.routine_create.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraooo.domain.model.RootRoutineModel
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.features.home.component.CircularProgress
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.computeTextLineCount
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RootRoutineCard(
    routine : RootRoutineModel
) {

    var descriptionTextLineCount by remember { mutableStateOf(0) }

    val descriptionTextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        color = Color(0xFFA6A6A6),
    )

    var isToggleOpened by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    )
                    .border(
                        width = 1.dp,
                        color = Color(routine.color.color),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .animateContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.padding(all = 18.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(end = 18.dp)
                    ) {
                        CircularProgress(
                            sweepAngle = 360F,
                            backgroundDrawableId = R.drawable.ic_logo_unchecked,
                            foregroundDrawableId = R.drawable.ic_logo_checked,
                            progressSize = 44.dp
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = routine.name,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = when {
//                                checked -> Color(0xFF54C392)
                                else -> Color(0xFF545454)
                            },
                            maxLines = 1,  // 한 줄만 표시
                            softWrap = false,  // 줄바꿈 방지
                            overflow = TextOverflow.Clip,
                            modifier = Modifier
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        descriptionTextLineCount = computeTextLineCount(
                            text = "",
                            maxLines = Int.MAX_VALUE,
                            textStyle = descriptionTextStyle,
                            isFillMaxWidth = true
                        )

                        Text(
                            text = "설명설명설명",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp,
                            color = Color(0xFFA6A6A6),
                            maxLines = when (isToggleOpened) {
                                true -> Int.MAX_VALUE
                                false -> 2
                            },
                            softWrap = isToggleOpened,
                        )
                    }
                }
            }
        }
    }
}