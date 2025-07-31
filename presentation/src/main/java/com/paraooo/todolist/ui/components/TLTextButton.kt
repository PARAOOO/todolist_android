package com.paraooo.todolist.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.roundedClickable

@Composable
fun TLTextButton(
    modifier: Modifier = Modifier,
    labelText : String? = null,
    isActive : Boolean = false,
    activeColor : Long = 0xFF54C392,
    clickable : () -> Unit = {},
    text : String,
    content : @Composable () -> Unit = {}
) {
    Column(modifier = modifier) {
        if (labelText != null){
            Text(
                labelText,
                fontSize = 14.sp,
                color = Color(0xFF7F7F7F),
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = BorderStroke(
                        1.dp,
                        color = if (isActive) Color(
                            activeColor
                        ) else Color(0xFFECEEEE)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .roundedClickable(12.dp) {
                    clickable()
                }
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                color = if (isActive) Color(
                    activeColor
                ) else Color(0xFF7F7F7F),
                modifier = Modifier.padding(vertical = 10.dp),
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
            )
        }

        content()
    }
}