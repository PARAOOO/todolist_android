package com.paraooo.todolist.ui.features.home.component

import android.graphics.Bitmap
import android.graphics.PorterDuff
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
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

class PieShape(private val sweepAngle: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            // 중심점으로 이동
            moveTo(size.width / 2f, size.height / 2f)
            // -90도(12시 방향)에서 sweepAngle 만큼의 호를 그림
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(0f, 0f, size.width, size.height),
                startAngleDegrees = -90f,
                sweepAngleDegrees = sweepAngle,
                forceMoveTo = false
            )
            // 경로를 닫아 완벽한 부채꼴 모양 생성
            close()
        }
        return Outline.Generic(path)
    }
}


@Composable
fun CircularProgress(
    sweepAngle: Float,
    drawableId: Int,
    progressSize: Dp,
    foregroundColor: Long? = null,
    backgroundColor: Long = 0xFFFFFFFF
) {
    Box(
        modifier = Modifier.size(progressSize)
    ) {
        // 1. 배경 이미지: 전체 모양을 배경색으로 칠합니다.
        Image(
            painter = painterResource(drawableId),
            contentDescription = "progress background",
            modifier = Modifier.size(progressSize),
            colorFilter = ColorFilter.tint(
                color = Color(backgroundColor),
                blendMode = BlendMode.SrcIn
            )
        )

        // 2. 전경 이미지: foregroundColor로 칠하고, 위에서 만든 PieShape으로 잘라냅니다.
        if (foregroundColor != null) {
            Image(
                painter = painterResource(drawableId),
                contentDescription = "progress foreground",
                modifier = Modifier
                    .size(progressSize)
                    .clip(PieShape(sweepAngle = sweepAngle)), // <<< 여기가 마법이 일어나는 곳입니다!
                colorFilter = ColorFilter.tint(
                    color = Color(foregroundColor),
                    blendMode = BlendMode.SrcIn
                )
            )
        }
    }
}