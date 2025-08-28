package com.paraooo.todolist.ui.features.routine_create.component

import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.paraooo.domain.model.RoutineColorModel
import com.paraooo.domain.model.SubRoutineModel
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.computeTextLineCount
import com.paraooo.todolist.ui.util.getRoutineIconDrawableId
import java.time.Duration
import kotlin.math.abs

fun getFormattedDuration(duration: Duration): String {

    val totalSeconds = abs(duration.seconds)

    if (totalSeconds == 0L) {
        return "0초"
    }

    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    val parts = mutableListOf<String>()

    if (hours > 0) {
        parts.add("${hours}시간")
    }
    if (minutes > 0) {
        parts.add("${minutes}분")
    }
    if (seconds > 0) {
        parts.add("${seconds}초")
    }

    return parts.joinToString(" ")
}

@Composable
fun SubRoutineCard(
    routine : SubRoutineModel,
    color : RoutineColorModel
) {

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
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
                    color = Color(color.color),
                    shape = RoundedCornerShape(12.dp),
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                modifier = Modifier.padding(vertical = 11.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.padding(end = 8.dp, start = 14.dp)
                ) {
                    Image(
                        painter = painterResource(getRoutineIconDrawableId(routine.icon)),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            color = Color(color.color),
                            BlendMode.SrcIn
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "${routine.name} / ${getFormattedDuration(routine.time)}",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = when {
//                                checked -> Color(0xFF54C392)
                        else -> Color(0xFF545454)
                    },
                    maxLines = 1,  // 한 줄만 표시
                    softWrap = false,  // 줄바꿈 방지
                    overflow = TextOverflow.Clip,
                    modifier = Modifier
                )
            }
        }

    }
}
