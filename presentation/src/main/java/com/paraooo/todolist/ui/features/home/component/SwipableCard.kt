package com.paraooo.todolist.ui.features.home.component

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraooo.domain.model.TodoModel
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.computeTextLineCount
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SwipeableCard(
    backgroundContent : @Composable () -> Unit,
    scrollRatio : Float,
    isSwiped : Boolean = false,
    onIsSwipedChanged : (isSwiped : Boolean) -> Unit,
    todo : TodoModel,
    onProgressChanged : (angle : Float) -> Unit,
    onIsToggledOpenedChanged : (isToggledOpened : Boolean) -> Unit,
) {
    val swipeableWidth = remember { Animatable(
        when(isSwiped) {
            true -> scrollRatio
            false -> 1f
        }
    ) } // 초기 뷰의 크기 비율 (1 = 전체 너비)
    val coroutineScope = rememberCoroutineScope()
    val isExpanded = remember { mutableStateOf(isSwiped) } // 확장 상태를 저장

    var isToggleOpened by remember { mutableStateOf(todo.isToggleOpened) }

    var checked by remember { mutableStateOf(
        when(todo.progressAngle){
            360F -> true
            else -> false
        }
    )}

    val descriptionTextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        color = Color(0xFFA6A6A6),
    )

    var targetSweepAngle by remember { mutableFloatStateOf(todo.progressAngle) }

    val sweepAngle by animateFloatAsState(
        targetValue = targetSweepAngle.coerceIn(0f, 359.9999f), // 애니메이션 대상 값
        animationSpec = tween(durationMillis = 16, easing = LinearEasing) // 애니메이션 지속 시간
    )

    var isFilling by remember { mutableStateOf(
        when(targetSweepAngle){
            360F -> false
            else -> true
        }
    ) }

    var isDraging by remember { mutableStateOf(false) }

    var descriptionTextLineCount by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // 오른쪽에 나타날 숨겨진 뷰
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = 1f),
            contentAlignment = Alignment.Center
        ) {
            backgroundContent()
        }

        // 스와이프 가능한 메인 뷰
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(swipeableWidth.value)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            coroutineScope.launch {
                                if(!isToggleOpened){
                                    isDraging = true
                                    val newWidth = swipeableWidth.value + dragAmount / 1000f
                                    swipeableWidth.snapTo(newWidth.coerceIn(scrollRatio, 1f))
                                    change.consume()
                                }
                            }
                        },
                        onDragEnd = {
                            coroutineScope.launch {
                                isDraging = false
                                if (swipeableWidth.value <= (scrollRatio + 1f) / 2) {
                                    swipeableWidth.animateTo(scrollRatio) // 확장 상태로 고정
                                    isExpanded.value = true
                                    onIsSwipedChanged(true)
                                } else {
                                    swipeableWidth.animateTo(1f) // 초기 상태로 복귀
                                    isExpanded.value = false
                                    onIsSwipedChanged(false)
                                }
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            // 누르고 있는 동안 각도 증가
                            if (!isDraging && !isExpanded.value) {
                                try {
                                    var isPressing = true
                                    var isLongPress = false

                                    coroutineScope.launch {
                                        try {
                                            awaitRelease()
                                        } finally {
                                            isPressing = false
                                            if (!isLongPress && !isDraging && descriptionTextLineCount > 2) {
                                                isToggleOpened = !isToggleOpened
                                                onIsToggledOpenedChanged(isToggleOpened)
                                            }
                                            checked = when (targetSweepAngle) {
                                                360F -> true
                                                else -> false
                                            }
                                            onProgressChanged(targetSweepAngle)
                                        }
                                    }

                                    delay(500)
                                    isLongPress = true

                                    if (isFilling) {
                                        while (isPressing) { // 손을 떼기 전까지 반복
                                            if (targetSweepAngle < 360f) {
                                                targetSweepAngle += 5f // 각도 증가
                                                delay(16) // 약 60fps 애니메이션
                                            } else {
                                                isFilling = false
                                                break
                                            }
                                        }
                                    } else {
                                        while (isPressing) { // 손을 떼기 전까지 반복
                                            if (targetSweepAngle > 0f) {
                                                targetSweepAngle -= 5f // 각도 증가
                                                delay(16) // 약 60fps 애니메이션
                                            } else {
                                                isFilling = true
                                                break
                                            }
                                        }
                                    }

                                } catch (e: Exception) {
                                    // 예외 처리 (사용자가 손을 떼면 예외 발생 가능)
                                }
                            }
                        }
                    )
                }
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
                            color = when {
                                checked -> Color(0xFF54C392)
                                else -> Color(0xFFECEEEE)
                            },
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
                                sweepAngle = sweepAngle,
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
                                text = when (todo.time) {
                                    null -> todo.title
                                    else -> "${String.format("%02d",todo.time!!.hour)} : ${String.format("%02d",todo.time!!.minute)} / ${todo.title}"
                                },
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = when {
                                    checked -> Color(0xFF54C392)
                                    else -> Color(0xFF545454)
                                },
                                maxLines = 1,  // 한 줄만 표시
                                softWrap = false,  // 줄바꿈 방지
                                overflow = TextOverflow.Clip,
                                modifier = Modifier
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            descriptionTextLineCount = computeTextLineCount(
                                text = todo.description ?: "",
                                maxLines = Int.MAX_VALUE,
                                textStyle = descriptionTextStyle,
                                isFillMaxWidth = true
                            )

                            Text(
                                text = todo.description ?: "",
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
}

