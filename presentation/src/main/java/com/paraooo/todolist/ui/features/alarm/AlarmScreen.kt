package com.paraooo.todolist.ui.features.alarm

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.features.create.CreateViewModel
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.roundedClickable
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AlarmScreen(
    instanceId: Long,
    onDismiss: () -> Unit,
    viewModel : AlarmViewModel = koinViewModel(),
    onVibrate : () -> Unit,
    onSound : () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentDateTime = remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentDateTime.value = LocalDateTime.now()
            val delayMillis = 1000L
            delay(delayMillis)
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.vibration) {
            onVibrate()
        }
        if (uiState.sound) {
            onSound()
        }
    }

    LaunchedEffect(Unit) {

        viewModel.onEvent(AlarmUiEvent.onInit(instanceId))

        delay(1000L * 30) // 30초 (1000ms * 30)
        onDismiss()
    }

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // 날짜 (2024년 5월 12일 (월))
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E)", Locale.KOREAN)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF54C392)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(com.paraooo.data.R.drawable.bg_todolist_notification),
                contentDescription = "알람 이미지",
                Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = currentDateTime.value.format(timeFormatter),
                fontSize = 80.sp,
                color = Color.White,
                fontWeight = FontWeight.ExtraLight,
                fontFamily = PretendardFontFamily
            )

            Text(
                text = currentDateTime.value.format(dateFormatter),
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontFamily = PretendardFontFamily
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = uiState.todoName,
                fontSize = 30.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = PretendardFontFamily
            )

            Spacer(modifier = Modifier.height(100.dp))

            Box(
                modifier = Modifier.background(
                    color = Color.White,
                    shape = RoundedCornerShape(100.dp)
                ).shadow(
                    elevation = 40.dp,
                    shape = RoundedCornerShape(100.dp),
                    clip = false,
                    ambientColor = Color.White,
                    spotColor = Color.White
                ).roundedClickable(
                    cornerRadius = 100.dp,
                    onClick = onDismiss
                )
            ) {
                Text(
                    text = "알람 해제",
                    fontSize = 20.sp,
                    color = Color(0xFF54C392),
                    fontWeight = FontWeight.Light,
                    fontFamily = PretendardFontFamily,
                    modifier = Modifier.padding(
                        horizontal = 30.dp, vertical = 12.dp
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewAlarmScreen() {
    AlarmScreen(0, onDismiss = {}, onVibrate = {}, onSound = {})
}