package com.paraooo.todolist.ui.features.home.component

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.paraooo.todolist.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun vectorToBitmap(vectorDrawable: VectorDrawable): Bitmap {
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return bitmap
}

@Composable
fun CircularProgress(
//    initialSweepAngle: Float,
//    onProgressChange: (Float) -> Unit,
    sweepAngle : Float,
    backgroundDrawableId : Int,
    foregroundDrawableId : Int,
    progressSize : Dp
) {

    val context = LocalContext.current
    val drawable = remember(context, foregroundDrawableId) {
        AppCompatResources.getDrawable(context,foregroundDrawableId)
    }

    val coroutineScope = rememberCoroutineScope()
    val bitmap = remember(drawable) {
        when (drawable) {
            is VectorDrawable -> vectorToBitmap(drawable)
            is BitmapDrawable -> drawable.bitmap
            else -> throw IllegalArgumentException("Unsupported drawable type")
        }
    }

    val imageBitmap = remember(bitmap) {
        bitmap.asImageBitmap()
    }

//    var targetSweepAngle by remember { mutableFloatStateOf(initialSweepAngle) }

//    val sweepAngle by animateFloatAsState(
//        targetValue = targetSweepAngle.coerceIn(0f, 359.9999f), // 애니메이션 대상 값
//        animationSpec = tween(durationMillis = 16, easing = LinearEasing) // 애니메이션 지속 시간
//    )

//    var isFilling by remember { mutableStateOf(
//        when(targetSweepAngle){
//            360F -> false
//            else -> true
//        }
//    ) }

    Box(
        modifier = Modifier
            .size(progressSize)
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onPress = {
//                        // 누르고 있는 동안 각도 증가
//                        try {
//                            var isPressing = true
//
//                            coroutineScope.launch {
//                                try{
//                                    awaitRelease()
//                                } finally {
//                                    isPressing = false
//                                    onProgressChange(targetSweepAngle)
//                                }
//                            }
//
//                            delay(500)
//
//                            if (isFilling) {
//                                while (isPressing) { // 손을 떼기 전까지 반복
//                                    if (targetSweepAngle < 360f) {
//                                        targetSweepAngle += 5f // 각도 증가
//                                        delay(16) // 약 60fps 애니메이션
//                                    } else {
//                                        isFilling = false
//                                        break
//                                    }
//                                }
//                            } else {
//                                while (isPressing) { // 손을 떼기 전까지 반복
//                                    if (targetSweepAngle > 0f) {
//                                        targetSweepAngle -= 5f // 각도 증가
//                                        delay(16) // 약 60fps 애니메이션
//                                    } else {
//                                        isFilling = true
//                                        break
//                                    }
//                                }
//                            }
//
//                        } catch (e: Exception) {
//                            // 예외 처리 (사용자가 손을 떼면 예외 발생 가능)
//                        }
//                    }
//                )
//            }
    ){

        Image(
            painter = painterResource(backgroundDrawableId)
            , contentDescription = "checkbox of todo card",
            modifier = Modifier.size(progressSize)
        )

        Canvas(
            modifier = Modifier.size(progressSize)
        ){

            val canvasWidth = size.width
            val canvasHeight = size.height
            val center = Offset(canvasWidth / 2, canvasHeight / 2)

            val arcPath = Path().apply {
                moveTo(center.x, center.y)
                arcTo(
                    rect = Rect(
                        left = 0F,
                        top = 0F,
                        right = canvasWidth,
                        bottom = canvasHeight
                    ),
                    startAngleDegrees = -90f, // 12시 방향
                    sweepAngleDegrees = sweepAngle,
                    forceMoveTo = false
                )
            }

            // Arc 모양으로 이미지 잘라서 그리기
            clipPath(arcPath) {
                drawImage(
                    image = imageBitmap,
                    srcSize = IntSize(bitmap.width, bitmap.height),
                    dstSize = IntSize(size.width.toInt(), size.height.toInt())
                )
            }
        }
    }

}

//@Composable
//fun CircularProgress(
//    initialSweepAngle: Float,
//    onProgressChange: (Float) -> Unit,
//    backgroundDrawableId : Int,
//    foregroundDrawableId : Int,
//    progressSize : Dp
//) {
//
//    val context = LocalContext.current
//    val drawable = remember(context, foregroundDrawableId) {
//        AppCompatResources.getDrawable(context,foregroundDrawableId)
//    }
//
//    val coroutineScope = rememberCoroutineScope()
//    val bitmap = remember(drawable) {
//        when (drawable) {
//            is VectorDrawable -> vectorToBitmap(drawable)
//            is BitmapDrawable -> drawable.bitmap
//            else -> throw IllegalArgumentException("Unsupported drawable type")
//        }
//    }
//
//    val imageBitmap = remember(bitmap) {
//        bitmap.asImageBitmap()
//    }
//
//    var targetSweepAngle by remember { mutableFloatStateOf(initialSweepAngle) }
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
//    Box(
//        modifier = Modifier
//            .size(progressSize)
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onPress = {
//                        // 누르고 있는 동안 각도 증가
//                        try {
//                            var isPressing = true
//
//                            coroutineScope.launch {
//                                try{
//                                    awaitRelease()
//                                } finally {
//                                    isPressing = false
//                                    onProgressChange(targetSweepAngle)
//                                }
//                            }
//
//                            delay(500)
//
//                            if (isFilling) {
//                                while (isPressing) { // 손을 떼기 전까지 반복
//                                    if (targetSweepAngle < 360f) {
//                                        targetSweepAngle += 5f // 각도 증가
//                                        delay(16) // 약 60fps 애니메이션
//                                    } else {
//                                        isFilling = false
//                                        break
//                                    }
//                                }
//                            } else {
//                                while (isPressing) { // 손을 떼기 전까지 반복
//                                    if (targetSweepAngle > 0f) {
//                                        targetSweepAngle -= 5f // 각도 증가
//                                        delay(16) // 약 60fps 애니메이션
//                                    } else {
//                                        isFilling = true
//                                        break
//                                    }
//                                }
//                            }
//
//                        } catch (e: Exception) {
//                            // 예외 처리 (사용자가 손을 떼면 예외 발생 가능)
//                        }
//                    }
//                )
//            }
//    ){
//
//        Image(
//            painter = painterResource(backgroundDrawableId)
//            , contentDescription = "checkbox of todo card",
//            modifier = Modifier.size(progressSize)
//        )
//
//        Canvas(
//            modifier = Modifier.size(progressSize)
//        ){
//
//            val canvasWidth = size.width
//            val canvasHeight = size.height
//            val center = Offset(canvasWidth / 2, canvasHeight / 2)
//
//            val arcPath = Path().apply {
//                moveTo(center.x, center.y)
//                arcTo(
//                    rect = Rect(
//                        left = 0F,
//                        top = 0F,
//                        right = canvasWidth,
//                        bottom = canvasHeight
//                    ),
//                    startAngleDegrees = -90f, // 12시 방향
//                    sweepAngleDegrees = sweepAngle,
//                    forceMoveTo = false
//                )
//            }
//
//            // Arc 모양으로 이미지 잘라서 그리기
//            clipPath(arcPath) {
//                drawImage(
//                    image = imageBitmap,
//                    srcSize = IntSize(bitmap.width, bitmap.height),
//                    dstSize = IntSize(size.width.toInt(), size.height.toInt())
//                )
//            }
//        }
//    }
//
//}

//@Preview
//@Composable
//fun PreviewCircularProgress() {
//    CircularProgress(
//        50F,
//        {},
//        R.drawable.ic_logo_unchecked,
//        R.drawable.ic_logo_checked,
//        44.dp
//    )
//}