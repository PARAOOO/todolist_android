package com.paraooo.todolist.ui.features.home.component

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import androidx.compose.ui.graphics.Color
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
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.paraooo.domain.model.RoutineColorModel
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
    sweepAngle : Float,
    backgroundDrawableId : Int,
    foregroundDrawableId : Int,
    progressSize : Dp,
    color : RoutineColorModel? = null
) {

    val context = LocalContext.current
//    val drawable = remember(context, foregroundDrawableId) {
//        AppCompatResources.getDrawable(context,foregroundDrawableId)
//    }

    val drawableResource: Drawable = ContextCompat.getDrawable(context, foregroundDrawableId)!!
    val drawable = DrawableCompat.wrap(drawableResource).mutate()
    DrawableCompat.setTint(drawable, ContextCompat.getColor(context, Color(color.color)))


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

    Box(
        modifier = Modifier.size(progressSize)
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