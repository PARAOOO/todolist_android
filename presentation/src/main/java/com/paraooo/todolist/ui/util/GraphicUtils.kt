package com.paraooo.todolist.ui.util

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.createRippleModifierNode
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.resolveDefaults
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import kotlin.math.ceil
import kotlin.math.min

@Composable
fun pxToDp(px: Int): Dp {
    val density = LocalDensity.current.density // 화면 밀도
    return (px / density).dp // Dp로 변환
}

@Composable
fun dpToPx(dpValue: Dp): Int {
    val density = LocalDensity.current.density
    return with(LocalDensity.current) { dpValue.toPx().toInt() }
}

@Composable
fun Modifier.roundedClickable(
    cornerRadius : Dp,
    onClick : () -> Unit,

): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this
        .clip(RoundedCornerShape(cornerRadius)) // 둥근 모서리 적용
        .clickable(
            interactionSource = interactionSource,
            indication = ripple(), // ripple 효과 추가
            onClick = onClick
        )
}

@Composable
fun Modifier.circleClickable(
    circleRadius : Dp,
    onClick : () -> Unit,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this
//        .clip(RoundedCornerShape(cornerRadius)) // 둥근 모서리 적용
        .clickable(
            interactionSource = interactionSource,
            indication = ripple(
                radius = circleRadius,
                bounded = false
            ), // ripple 효과 추가
            onClick = onClick
        )
}

@Composable
fun computeTextLineCount(
    text: String,
    textStyle: TextStyle,
    maxWidth: Dp = 0.dp,
    maxLines: Int,
    isFillMaxWidth: Boolean = false
) : Int {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    var lineCount by remember { mutableStateOf(0) }

    if(isFillMaxWidth) {
        BoxWithConstraints {
            val maxWidthPx = constraints.maxWidth

            lineCount = remember(text, textStyle, maxLines, maxWidthPx) {
                val measured = textMeasurer.measure(
                    text = text,
                    style = textStyle,
                    constraints = Constraints(
                        maxWidth = maxWidthPx // 최대 너비 적용
                    )
                )
                min(measured.lineCount, maxLines) // 최대 줄 수 제한
            }
        }
    } else {
        lineCount = remember(text, textStyle, maxWidth, maxLines) {
            with(density) {
                val measured = textMeasurer.measure(
                    text = text,
                    style = textStyle,
                    constraints = Constraints(
                        maxWidth = maxWidth.roundToPx() // Dp → Px 변환
                    )
                )
                min(measured.lineCount, maxLines) // 최대 줄 수 제한 적용
            }
        }
    }

    return lineCount

}


@Composable
fun computeTextHeight(
    textStyle: TextStyle,
    text: String,
    maxLines: Int
) : Int {

    val density = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val layoutDirection = LocalLayoutDirection.current

    val resolvedStyle = remember(textStyle, layoutDirection) {
        resolveDefaults(textStyle, layoutDirection)
    }

    val descriptionTextHeight = with(density){
        computeSizeForDefaultText(
            style = resolvedStyle,
            density = density,
            fontFamilyResolver = fontFamilyResolver,
            text = text,
            maxLines = maxLines
        ).height
    }

    return descriptionTextHeight

}

private val EmptyTextReplacement = "H".repeat(10)
private fun computeSizeForDefaultText(
    style: TextStyle,
    density: Density,
    fontFamilyResolver: FontFamily.Resolver,
    text: String = EmptyTextReplacement,
    maxLines: Int = 1
) : IntSize {
    val paragraph = Paragraph(
        text = text,
        style = style,
        spanStyles = listOf(),
        maxLines = maxLines,
        ellipsis = false,
        density = density,
        fontFamilyResolver = fontFamilyResolver,
        constraints = Constraints()
    )
    return IntSize(paragraph.minIntrinsicWidth.ceilToIntPx(), paragraph.height.ceilToIntPx())
}

fun Float.ceilToIntPx(): Int = ceil(this).fastRoundToInt()