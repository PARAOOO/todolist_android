package com.paraooo.todolist.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.paraooo.todolist.ui.features.create.CreateScreen
import com.paraooo.todolist.ui.features.edit.EditScreen
import com.paraooo.todolist.ui.features.home.HomeScreen
import com.paraooo.todolist.ui.features.privacy_policy.PrivacyPolicyScreen
import com.paraooo.todolist.ui.features.routine_create.RoutineCreateScreen
import com.paraooo.todolist.ui.features.setting.SettingScreen
import com.paraooo.todolist.ui.features.signup.SignUpScreen
import com.paraooo.todolist.ui.features.splash.SplashScreen
import com.paraooo.todolist.ui.features.start.StartScreen
import com.paraooo.todolist.ui.util.pxToDp
import java.time.LocalDate


@Composable
fun AppNavGraph(navController: NavHostController) {

    val insets = WindowInsets.systemBars
    val statusBarHeight = pxToDp(insets.getTop(LocalDensity.current))
    val navigationBarHeight = pxToDp(insets.getBottom(LocalDensity.current))

    NavHost(
        navController = navController,
        startDestination = Destinations.Splash.route,
        modifier = Modifier.padding(
            top = statusBarHeight,
            bottom = navigationBarHeight
        )
    ) {
        composable(Destinations.Home.route) {
            HomeScreen(
                navController = navController
            )
        }

        composable(Destinations.Splash.route) {
            SplashScreen(
                navController = navController
            )
        }

        composable(Destinations.Start.route) {
            StartScreen(
                navController = navController
            )
        }

        composable(Destinations.SignUp.route) {
            SignUpScreen(
                navController = navController
            )
        }

        composable(
            route = "${Destinations.Create.route}/{selectedDate}",
            arguments = listOf(navArgument("selectedDate") { type = NavType.LongType })
        ) { backStackEntry ->
            val epochDay = backStackEntry.arguments?.getLong("selectedDate") ?: 0
            val selectedDate = LocalDate.ofEpochDay(epochDay)
            CreateScreen(
                navController = navController,
                selectedDate = selectedDate
            )
        }

        composable(
            route = "${Destinations.Edit.route}/{instanceId}",
            arguments = listOf(
                navArgument("instanceId") { type = NavType.LongType },
            )
        ) { backStackEntry ->
            val instanceId = backStackEntry.arguments?.getLong("instanceId") ?: 0

            EditScreen(
                navController = navController,
                instanceId = instanceId,
            )
        }

        composable(Destinations.RoutineCreate.route) {
            RoutineCreateScreen(
                navController = navController
            )
        }

        composable(Destinations.Setting.route) {
            SettingScreen(
                navController = navController
            )
        }

        composable(Destinations.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                navController = navController
            )
        }


    }
}