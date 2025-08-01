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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.paraooo.domain.model.RoutineColorModel
import com.paraooo.todolist.ui.features.routine_create.RoutineCreateUiState
import com.paraooo.todolist.ui.util.dpToPx
import com.paraooo.todolist.ui.util.getRoutineIconDrawableIdList
import com.paraooo.todolist.ui.util.roundedClickable

@Composable
fun IconPickerPopup(
    onDismissRequest: () -> Unit,
    isIconPopupVisible: Boolean,
    numberOfRows: Int = 3,
    onIconSelected : (icon : Int) -> Unit,
    color : Long = Color.Gray.value.toLong()
) {

    val popupHeight = ((32.dp + 12.dp) * numberOfRows) + (8.dp * (numberOfRows - 1))

    val iconList = getRoutineIconDrawableIdList()

    if(isIconPopupVisible){
        Popup(
            onDismissRequest = { onDismissRequest() },
            offset = IntOffset(0, -dpToPx(popupHeight)),
        ) {
            Box(
                modifier = Modifier
                    .height(popupHeight)
                    .background(
                        Color.White, RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp, Color(0xFFECEEEE), RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(numberOfRows),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(iconList) { index, icon  ->
                        Box(
                            modifier = Modifier.size(32.dp).background(Color.Transparent).roundedClickable(100.dp) {
                                onIconSelected(index+1)
                            },
                            contentAlignment = Alignment.Center
                        ){
                            Image(
                                painter = painterResource(icon),
                                null,
                                modifier = Modifier.size(22.dp),
                                colorFilter = ColorFilter.tint(
                                    Color(color),
                                    androidx.compose.ui.graphics.BlendMode.SrcIn
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}