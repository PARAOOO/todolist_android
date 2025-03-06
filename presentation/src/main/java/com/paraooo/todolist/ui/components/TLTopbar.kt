package com.paraooo.todolist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.circleClickable

@Composable
fun TLTopbar(
    modifier: Modifier = Modifier,
    title : String,
    onBackClicked : () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 12.dp)
                .width(10.dp)
                .circleClickable(20.dp) {
                    onBackClicked()
                }
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = title,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            color = Color(0xFF545454)
        )
    }
}