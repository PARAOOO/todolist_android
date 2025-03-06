package com.paraooo.todolist.ui.features.home.component

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.resolveDefaults
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.computeTextHeight
import com.paraooo.todolist.ui.util.computeTextLineCount
import com.paraooo.todolist.ui.util.roundedClickable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

//@Composable
//fun TodoCard(
//    modifier : Modifier = Modifier,
//    todo : TodoModel,
//    onProgressChanged : (angle : Float) -> Unit,
//    onIsToggledOpenedChanged : (isToggledOpened : Boolean) -> Unit,
//    enabled : Boolean = true
//) {
//
//    var isToggleOpened by remember { mutableStateOf(todo.isToggleOpened) }
//
//    var checked by remember { mutableStateOf(
//        when(todo.progressAngle){
//            360F -> true
//            else -> false
//        }
//    )}
//
//    val descriptionTextStyle = TextStyle(
//        fontFamily = PretendardFontFamily,
//        fontWeight = FontWeight.Normal,
//        fontSize = 10.sp,
//        color = Color(0xFFA6A6A6),
//    )
//
//
//
//    var targetSweepAngle by remember { mutableFloatStateOf(todo.progressAngle) }
//
//    val sweepAngle by animateFloatAsState(
//        targetValue = targetSweepAngle.coerceIn(0f, 359.9999f), // 애니메이션 대상 값
//        animationSpec = tween(durationMillis = 16, easing = LinearEasing) // 애니메이션 지속 시간
//    )
//
//    var isFilling by remember { mutableStateOf(
//        when(targetSweepAngle){
//            360F -> false
//            else -> true
//        }
//    ) }
//
//
//
//    val coroutineScope = rememberCoroutineScope()
//
//    var descriptionTextLineCount by remember { mutableStateOf(0) }
//
//    Log.d(TAG, "TodoDescriptionLineCount : ${descriptionTextLineCount} ")
//
////    key(todo.isToggleOpened){
//        Row(
//            modifier = modifier
//                .fillMaxWidth()
//                .background(
//                    shape = RoundedCornerShape(12.dp),
//                    color = Color.White
//                )
//                .border(
//                    width = 1.dp,
//                    color = when {
//                        checked -> Color(0xFF54C392)
//                        else -> Color(0xFFECEEEE)
//                    },
//                    shape = RoundedCornerShape(12.dp),
//                )
//                .animateContentSize()
////            .roundedClickable(12.dp) {
////                if (enabled && descriptionTextLineCount > 2) {
////                    onIsToggledOpenedChanged(!todo.isToggleOpened)
////                }
////            }
//                .pointerInput(Unit) {
////                    awaitPointerEventScope {
////                        coroutineScope.launch {
////                            awaitEachGesture {
////
////                            }
////                        }
////                    }
////                    detectTapGestures(
////                        onPress = {
////                            // 누르고 있는 동안 각도 증가
////                            try {
////                                var isPressing = true
////                                var isLongPress = false
////
////                                coroutineScope.launch {
////                                    try {
////                                        awaitRelease()
////                                    } finally {
////                                        isPressing = false
////                                        if(!isLongPress){
////                                            if (enabled && descriptionTextLineCount > 2) {
////                                                isToggleOpened = !isToggleOpened
////                                                onIsToggledOpenedChanged(isToggleOpened)
////                                            }
////                                        }
////                                        checked = when (targetSweepAngle) {
////                                            360F -> true
////                                            else -> false
////                                        }
////                                        onProgressChanged(targetSweepAngle)
////                                    }
////                                }
////
////                                delay(500)
////                                isLongPress = true
////
////                                if (isFilling) {
////                                    while (isPressing) { // 손을 떼기 전까지 반복
////                                        if (targetSweepAngle < 360f) {
////                                            targetSweepAngle += 5f // 각도 증가
////                                            delay(16) // 약 60fps 애니메이션
////                                        } else {
////                                            isFilling = false
////                                            break
////                                        }
////                                    }
////                                } else {
////                                    while (isPressing) { // 손을 떼기 전까지 반복
////                                        if (targetSweepAngle > 0f) {
////                                            targetSweepAngle -= 5f // 각도 증가
////                                            delay(16) // 약 60fps 애니메이션
////                                        } else {
////                                            isFilling = true
////                                            break
////                                        }
////                                    }
////                                }
////
////                            } catch (e: Exception) {
////                                // 예외 처리 (사용자가 손을 떼면 예외 발생 가능)
////                            }
////                        }
////                    )
//                },
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Row(
//                modifier = Modifier.padding(all = 18.dp)
//            ) {
//                Box(
//                    modifier = Modifier.padding(end = 18.dp)
//                ) {
////                CircularProgress(
////                    initialSweepAngle = todo.progressAngle,
////                    onProgressChange = { angle: Float ->
////                        checked = when (angle) {
////                            360F -> true
////                            else -> false
////                        }
////                        onProgressChanged(angle)
////                    },
////                    backgroundDrawableId = R.drawable.ic_logo_unchecked,
////                    foregroundDrawableId = R.drawable.ic_logo_checked,
////                    progressSize = 44.dp
////                )
//
//                    CircularProgress(
//                        sweepAngle = sweepAngle,
//                        backgroundDrawableId = R.drawable.ic_logo_unchecked,
//                        foregroundDrawableId = R.drawable.ic_logo_checked,
//                        progressSize = 44.dp
//                    )
//                }
//
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                ) {
//                    Text(
//                        text = when (todo.time) {
//                            null -> todo.title
//                            else -> "${String.format("%02d",todo.time!!.hour)} : ${String.format("%02d",todo.time!!.minute)} / ${todo.title}"
//                        },
//                        fontFamily = PretendardFontFamily,
//                        fontWeight = FontWeight.Medium,
//                        fontSize = 16.sp,
//                        color = when {
//                            checked -> Color(0xFF54C392)
//                            else -> Color(0xFF545454)
//                        },
//                        maxLines = 1,  // 한 줄만 표시
//                        softWrap = false,  // 줄바꿈 방지
//                        overflow = TextOverflow.Clip,
//                        modifier = Modifier
//                    )
//
//                    Spacer(modifier = Modifier.height(2.dp))
//
//                    descriptionTextLineCount = computeTextLineCount(
//                        text = todo.description ?: "",
//                        maxLines = Int.MAX_VALUE,
//                        textStyle = descriptionTextStyle,
//                        isFillMaxWidth = true
//                    )
//
//                    Text(
//                        text = todo.description ?: "",
//                        fontFamily = PretendardFontFamily,
//                        fontWeight = FontWeight.Normal,
//                        fontSize = 10.sp,
//                        color = Color(0xFFA6A6A6),
//                        maxLines = when (isToggleOpened) {
//                            true -> Int.MAX_VALUE
//                            false -> 2
//                        },
//                        softWrap = isToggleOpened,
//                    )
//                }
//            }
//        }
////    }
//
//}

@Composable
fun TodoCardSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            )
            .border(
                width = 1.dp,
                color = Color(0xFFECEEEE),
                shape = RoundedCornerShape(12.dp),
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(start = 19.dp)
                .size(40.dp)
                .background(
                    shape = CircleShape,
                    color = Color(0xFFECEEEE)
                )
        )

        Spacer(modifier = Modifier.width(19.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 18.dp, top = 20.dp, bottom = 18.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(124.dp)
                    .height(12.dp)
                    .background(
                        shape = RoundedCornerShape(100.dp),
                        color = Color(0xFFECEEEE)
                    )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .width(254.dp)
                    .height(8.dp)
                    .background(
                        shape = RoundedCornerShape(100.dp),
                        color = Color(0xFFECEEEE)
                    )
            )

            Spacer(modifier = Modifier.height(4.dp))


            Box(
                modifier = Modifier
                    .width(198.dp)
                    .height(8.dp)
                    .background(
                        shape = RoundedCornerShape(100.dp),
                        color = Color(0xFFECEEEE)
                    )
            )
        }
    }
}

//@Preview
//@Composable
//fun PreviewTodoCard() {
//    TodoCard(
//        todo = TodoModel(
//            id = 1,
//            title = "다이소에서 돌돌이 사기",
//            time = Time(12,40),
//            description = "대구 수성구 용학로 223 / 102동 406호 안녕하세요 반갑습니다 저는 최희건이라고 하고요\n전자 영수증 발급하기\nasddaf\nk",
//            progressAngle = 360F,
//            isToggleOpened = true,
//            date = LocalDate.now()
//        ),
//        onProgressChanged = {},
//        onIsToggledOpenedChanged = {}
//    )
//}