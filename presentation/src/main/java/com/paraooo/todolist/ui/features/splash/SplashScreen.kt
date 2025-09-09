package com.paraooo.todolist.ui.features.splash

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.features.signup.SignUpUiEffect
import com.paraooo.todolist.ui.navigation.Destinations
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    navController : NavHostController,
    viewModel: SplashViewModel = koinViewModel()
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.effectFlow, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effectFlow.collect { effect ->
                when (effect) {
                    SplashUiEffect.NaviagateToHome -> {
                        navController.navigate(Destinations.Home.route){
                            popUpTo(Destinations.Splash.route){inclusive = true}
                        }
                    }
                    SplashUiEffect.NaviagateToLogin -> {
                        navController.navigate(Destinations.Start.route){
                            popUpTo(Destinations.Splash.route){inclusive = true}
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.ic_logo_splash
            ),
            contentDescription = "logo of splash screen",
            modifier = Modifier.width(250.dp),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
@Preview
fun PreviewSplashScreen() {
    SplashScreen(
        navController = rememberNavController()
    )
}