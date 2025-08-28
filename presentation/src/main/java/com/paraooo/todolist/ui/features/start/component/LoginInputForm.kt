package com.paraooo.todolist.ui.features.start.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraooo.todolist.ui.features.start.StartUiState
import com.paraooo.todolist.ui.theme.PretendardFontFamily

@Composable
fun LoginInputForm(
    uiState: StartUiState,
    onEmailInputChanged: (email: String) -> Unit,
    onPasswordInputChanged: (password: String) -> Unit
) {

    val shape = RoundedCornerShape(12.dp)

    Column {
        BasicTextField(
            value = uiState.email,
            onValueChange = { onEmailInputChanged(it) },
            textStyle = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color(0xFF000000)
            ),
            cursorBrush = SolidColor(Color(0xFF8F8F8F)),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = shape)
                .border(1.dp, Color(0xFFECEEEE), shape)
                .padding(horizontal = 16.dp, vertical = 18.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.TopStart
                ) {
                    if (uiState.email.isEmpty()) {
                        Text(
                            text = "이메일",
                            style = TextStyle(
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color(0xFF7F7F7F)
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        BasicTextField(
            value = uiState.password,
            onValueChange = { onPasswordInputChanged(it) },
            textStyle = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color(0xFF000000)
            ),
            cursorBrush = SolidColor(Color(0xFF8F8F8F)),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = shape)
                .border(1.dp, Color(0xFFECEEEE), shape)
                .padding(horizontal = 16.dp, vertical = 18.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.TopStart
                ) {
                    if (uiState.password.isEmpty()) {
                        Text(
                            text = "비밀번호",
                            style = TextStyle(
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color(0xFF7F7F7F)
                            )
                        )
                    }
                    innerTextField()
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

            )
    }
    

}