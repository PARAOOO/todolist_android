package com.paraooo.todolist.ui.features.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.paraooo.todolist.ui.components.TLTopbar
import com.paraooo.todolist.ui.features.signup.component.SignUpTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFF7F7F7)
            )
            .padding(horizontal = 12.dp, vertical = 30.dp)
    ) {
        TLTopbar(
            title = "회원가입",
            onBackClicked = {
                navController.popBackStack()
            }
        )

        Column(
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(150.dp))

            SignUpTextField(
                value = uiState.nickname,
                hintText = "닉네임을 입력해 주세요",
                onChange = { viewModel.onEvent(SignUpUiEvent.onNicknameInputChanged(it)) },
                label = "닉네임",
                errorMessage = uiState.nicknameErrorMessage
            )

            Spacer(modifier = Modifier.height(30.dp))

            SignUpTextField(
                value = uiState.email,
                hintText = "이메일을 입력해 주세요",
                onChange = { viewModel.onEvent(SignUpUiEvent.onEmailInputChanged(it)) },
                label = "이메일",
                errorMessage = uiState.emailErrorMessage
            )

            Spacer(modifier = Modifier.height(30.dp))

            SignUpTextField(
                value = uiState.password,
                hintText = "비밀번호를 입력해 주세요",
                onChange = { viewModel.onEvent(SignUpUiEvent.onPasswordInputChanged(it)) },
                label = "비밀번호",
                errorMessage = uiState.passwordErrorMessage
            )

            Spacer(modifier = Modifier.height(30.dp))

            SignUpTextField(
                value = uiState.passwordCheck,
                hintText = "비밀번호를 다시 한번 입력해 주세요",
                onChange = { viewModel.onEvent(SignUpUiEvent.onPasswordCheckInputChanged(it)) },
                label = "비밀번호 확인",
                errorMessage = uiState.passwordCheckErrorMessage
            )

        }
    }

}