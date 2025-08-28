package com.paraooo.todolist.ui.features.splash

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.navigation.Destinations
import kotlinx.coroutines.delay

const val SPLASH_DELAY_MS = 2000L

@Composable
fun SplashScreen(
    navController : NavHostController
) {

    LaunchedEffect(Unit) {
        delay(SPLASH_DELAY_MS)

        navController.navigate(Destinations.Start.route){
            popUpTo(Destinations.Splash.route){inclusive = true}
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