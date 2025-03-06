package com.paraooo.todolist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TLSnackbarHost(
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(hostState = snackbarHostState) { data ->

//        LaunchedEffect(data) {
//            delay(2000) // 5초 동안 유지 후 닫기
//            data.dismiss()
//        }

        Snackbar(
            modifier = Modifier
                .background(Color(0xFF333333), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            content = {
                Text(
                    text = data.visuals.message,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        )
    }
}