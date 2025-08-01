package com.paraooo.todolist.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.paraooo.domain.model.RoutineColorModel
import com.paraooo.todolist.ui.features.routine_create.RoutineCreateUiState
import com.paraooo.todolist.ui.util.dpToPx
import com.paraooo.todolist.ui.util.roundedClickable

fun adjustAlphaFromInt(color: Long, newAlpha: Int): Long {
    val clampedAlpha = newAlpha.coerceIn(0, 255)
    val originalRgb = color and 0x00FFFFFFL
    val newAlphaComponent = clampedAlpha.toLong() shl 24
    return newAlphaComponent or originalRgb
}

fun adjustAlphaFromFloat(color: Long, newAlpha: Float): Long {
    val clampedAlphaFloat = newAlpha.coerceIn(0.0f, 1.0f)
    val alphaInt = (clampedAlphaFloat * 255).toInt()
    return adjustAlphaFromInt(color, alphaInt)
}

fun longToColorHexString(color: Long): String {
    val rgb = color and 0xFFFFFFL
    val hexString = String.format("%06X", rgb)

    return "#$hexString"
}

@Composable
fun ColorPickerPopup(
    currentColor: RoutineColorModel,
    onDismissRequest: () -> Unit,
    isColorPopupVisible: Boolean,
    numberOfRows: Int = 2,
    onColorSelected : (color : RoutineColorModel) -> Unit
) {

    val popupHeight = ((32.dp + 12.dp) * numberOfRows) + (8.dp * (numberOfRows - 1))

    val colorList = RoutineColorModel.entries.toList()

    if(isColorPopupVisible){
        Popup(
            onDismissRequest = { onDismissRequest() },
            offset = IntOffset(0, -dpToPx(popupHeight)),
        ) {
            Box(
                modifier = Modifier
                    .height(popupHeight)
                    .background(
                        Color.White, RoundedCornerShape(12.dp)
                    ).border(
                        1.dp, Color(0xFFECEEEE), RoundedCornerShape(12.dp)
                    ).padding(12.dp)
            ) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(numberOfRows),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(colorList) { color ->
                        Box(
                            modifier = Modifier.background(
                                color = Color.Transparent,
                                shape = CircleShape
                            ).size(32.dp)
                                .border(
                                    1.dp,
                                    if (currentColor == color) Color(color.color) else Color.Transparent,
                                    CircleShape
                                ).roundedClickable(100.dp) {
                                    if (currentColor == color) {
                                        onDismissRequest()
                                    }
                                    onColorSelected(color)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier.background(
                                    color = Color(color.color),
                                    shape = CircleShape
                                ).size(26.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}