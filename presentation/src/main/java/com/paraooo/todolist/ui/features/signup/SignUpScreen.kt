package com.paraooo.todolist.ui.features.signup

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.paraooo.todolist.ui.components.TLTopbar
import com.paraooo.todolist.ui.features.home.HomeUiEffect
import com.paraooo.todolist.ui.features.signup.component.SignUpTextField
import com.paraooo.todolist.ui.features.start.StartUiEvent
import com.paraooo.todolist.ui.navigation.Destinations
import com.paraooo.todolist.ui.theme.PretendardFontFamily
import com.paraooo.todolist.ui.util.roundedClickable
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current

    LaunchedEffect(viewModel.effectFlow, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effectFlow.collect { effect ->
                when (effect) {
                    SignUpUiEffect.onSendVerificationCodeSuccess -> {
                        Toast.makeText(context, "인증 코드가 이메일로 발송되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    SignUpUiEffect.onVerifyCodeSuccess -> {
                        Toast.makeText(context, "이메일 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    SignUpUiEffect.onSignUpFailed ->  {
                        Toast.makeText(context, "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                    SignUpUiEffect.onSignUpSuccess -> {
                        navController.navigate(Destinations.Start.route){
                            popUpTo(Destinations.SignUp.route){inclusive = true}
                        }
                        Toast.makeText(context, "회원가입이 성공적으로 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFF7F7F7)
            )
            .padding(horizontal = 12.dp, vertical = 30.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TLTopbar(
                title = "회원가입",
                onBackClicked = {
                    navController.popBackStack()
                }
            )

            Column(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Spacer(modifier = Modifier.height(40.dp))

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
                    isEnabled = uiState.verificationState == VerificationState.NONE,
                    errorMessage = uiState.emailErrorMessage
                )

                if (uiState.verificationState != VerificationState.NONE) {
                    Spacer(modifier = Modifier.height(30.dp))

                    SignUpTextField(
                        value = uiState.verificationCode,
                        hintText = "인증 코드를 입력해 주세요",
                        onChange = {
                            viewModel.onEvent(
                                SignUpUiEvent.onVerificationCodeInputChanged(
                                    it
                                )
                            )
                        },
                        label = "인증 코드",
                        isEnabled = uiState.verificationState != VerificationState.SUCCESS,
                        errorMessage = uiState.verificationCodeErrorMessage
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            shape = RoundedCornerShape(12.dp),
                            color = when (uiState.isGetVerificationCodeButtonEnabled) {
                                true -> Color(0xFF54C392)
                                false -> Color(0xFF7F7F7F)
                            }
                        )
                        .roundedClickable(12.dp) {
                            if (uiState.isGetVerificationCodeButtonEnabled) {
                                viewModel.onEvent(SignUpUiEvent.onGetVerificationCodeButtonClicked)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (uiState.verificationState) {
                            VerificationState.NONE -> "인증 코드 받기"
                            is VerificationState.WAITING -> "이메일 인증하기 (${getDisplayTextFromSeconds((uiState.verificationState as VerificationState.WAITING).seconds)} 뒤 만료)"
                            VerificationState.SUCCESS -> "이메일 인증 완료"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PretendardFontFamily,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 14.dp)
                    )
                }


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


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(12.dp),
                    color = when (uiState.isSignUpButtonEnabled) {
                        true -> Color(0xFF54C392)
                        false -> Color(0xFF7F7F7F)
                    }
                )
                .roundedClickable(12.dp) {
                    if (uiState.isSignUpButtonEnabled) {
                        viewModel.onEvent(SignUpUiEvent.onSignUpClicked)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "회원가입",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = PretendardFontFamily,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }

}



@SuppressLint("DefaultLocale")
fun getDisplayTextFromSeconds(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)

}