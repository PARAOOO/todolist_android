package com.paraooo.todolist.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.paraooo.domain.model.AlarmType
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.roundedClickable

@Composable
fun <T> TLTextRadioButton(
    labelText : String,
    radioList : List<T>,
    currentRadioItem : T,
    clickable : (radio : T) -> Unit,
    activeColor : Long = 0xFF54C392,
    getText : (radio : T) -> String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            labelText,
            fontSize = 14.sp,
            color = Color(0xFF7F7F7F),
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),

            ) {
            for(radioItem in radioList){

                val isSelected = radioItem == currentRadioItem

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(
                                1.dp,
                                color = if (isSelected) Color(
                                    activeColor
                                ) else Color(0xFFECEEEE)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .roundedClickable(12.dp) {
                            clickable(radioItem)
                        }
                ) {
                    Text(
                        text = getText(radioItem),
                        fontSize = 12.sp,
                        color = if (isSelected) Color(
                            activeColor
                        ) else Color(0xFF7F7F7F),
                        modifier = Modifier.padding(vertical = 10.dp),
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }
        }
    }
}