package com.paraooo.todolist.ui.features.start

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.features.create.CreateUiEvent
import com.paraooo.todolist.ui.features.home.HomeUiEffect
import com.paraooo.todolist.ui.features.start.component.LoginInputForm
import com.paraooo.todolist.ui.navigation.Destinations
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.roundedClickable

@Composable
fun StartScreen(
    navController: NavController,
    viewModel : StartViewModel = koinViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.effectFlow, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effectFlow.collect { effect ->
                when (effect) {
                    StartUiEffect.onLoginSuccess -> {
                        navController.navigate(Destinations.Home.route){
                            popUpTo(Destinations.Start.route){inclusive = true}
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFFFFFFF)
            )
            .padding(horizontal = 12.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            Image(
                modifier = Modifier
                    .width(250.dp)
                    .height(38.dp),
                contentScale = ContentScale.Fit,
                painter = painterResource(R.drawable.ic_logo_text),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    color = Color(0xFF54C392),
                    blendMode = BlendMode.SrcIn
                ),
            )

            Spacer(modifier = Modifier.height(120.dp))

            LoginInputForm(
                uiState = uiState,
                onEmailInputChanged = { viewModel.onEvent(StartUiEvent.onEmailInputChanged(it)) },
                onPasswordInputChanged = { viewModel.onEvent(StartUiEvent.onPasswordInputChanged(it)) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp)
                    .background(
                        shape = RoundedCornerShape(12.dp),
                        color = when (uiState.isLoginButtonEnabled) {
                            true -> Color(0xFF54C392)
                            false -> Color(0xFF7F7F7F)
                        }
                    )
                    .roundedClickable(12.dp) {
                        if (uiState.isLoginButtonEnabled) {
                            viewModel.onEvent(StartUiEvent.onLoginButtonClicked)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "로그인",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PretendardFontFamily,
                    color = Color.White
                )
            }

            uiState.loginErrorMessage?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PretendardFontFamily,
                    color = Color(0xFFFF6E6E),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "기존 계정이 없으신가요? 회원가입",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = PretendardFontFamily,
                color = Color(0xFF8F8F8F),
                modifier = Modifier.roundedClickable(12.dp) {
                    navController.navigate(Destinations.SignUp.route)
                }
            )
        }

        Text(
            text = "비밀번호를 잊으셨나요?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = PretendardFontFamily,
            color = Color(0xFF8F8F8F)
        )
    }
}