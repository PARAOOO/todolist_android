package com.paraooo.todolist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraooo.todolist.ui.theme.PretendardFontFamily

@Composable
fun TLTextField(
    text : String,
    onTextChange : (text : String) -> Unit,
    hintText : String = "",
    label : String = "",
    singleLine : Boolean = true,
) {

    val shape = when(singleLine) {
        true -> CircleShape
        false -> RoundedCornerShape(12.dp)
    }

    Column {
        if(label.isNotEmpty()) {
            Text(
                text = label,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color(0xFF7F7F7F),
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        BasicTextField(
            value = text,
            onValueChange = { onTextChange(it) },
            textStyle = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color(0xFF000000)
            ),
            singleLine = singleLine,
            cursorBrush = SolidColor(Color(0xFF7F7F7F)),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = shape)
                .border(1.dp, Color(0xFFECEEEE), shape)
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .then(
                    if(singleLine) {
                        Modifier
                    } else {
                        Modifier.height(80.dp)
                    }
                ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.TopStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = hintText,
                            style = TextStyle(
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Color(0xFF7F7F7F)
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
@Preview
fun PreviewTLTextField() {
    TLTextField(
        text = "awdfsaskdjfhaskdjfh\naksdjfhkasjdfh",
        onTextChange = {},
        hintText = "힌트를 입력해 주세요",
        label = "Todo Name",
        singleLine = false
    )
}