package com.paraooo.todolist.ui.features.signup.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraooo.todolist.ui.theme.PretendardFontFamily

@Composable
fun SignUpTextField(
    value: String,
    hintText: String,
    onChange: (String) -> Unit,
    label: String,
    errorMessage: String? = null,
    isEnabled: Boolean = true,
) {


    Column {

        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = PretendardFontFamily,
            color = Color(0xFF7F7F7F)
        )

        Spacer(modifier = Modifier.height(6.dp))

        BasicTextField(
            value = value,
            onValueChange = { onChange(it) },
            textStyle = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color(0xFF545454)
            ),
            enabled = isEnabled,
            cursorBrush = SolidColor(Color(0xFF8F8F8F)),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.TopStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hintText,
                            style = TextStyle(
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                color = Color(0xFF7F7F7F)
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFECEEEE)
        )

        errorMessage?.let {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = PretendardFontFamily,
                color = Color(0xFFFF3B3B)
            )
        }
    }
}