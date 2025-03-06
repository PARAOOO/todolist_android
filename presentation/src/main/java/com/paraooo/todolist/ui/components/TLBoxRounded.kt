package com.paraooo.todolist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TLBoxRounded(
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit
) {

    Box(
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            )
            .border(
                width = 1.dp,
                color = Color(0xFFECEEEE),
                shape = RoundedCornerShape(12.dp),
            ).then(
                modifier
            ),
        contentAlignment = Alignment.Center
    ){
        content()
    }
}
