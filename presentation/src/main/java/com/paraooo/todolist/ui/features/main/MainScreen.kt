package com.paraooo.todolist.ui.features.main

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.paraooo.todolist.ui.features.home.HomeUiEffect
import com.paraooo.todolist.ui.navigation.AppNavGraph
import com.paraooo.todolist.ui.navigation.Destinations
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = koinViewModel(),
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.navEventFlow.collect { event ->
            when (event) {
                MainUiEvent.NavigateToLogin -> {
                    navController.navigate(Destinations.Start.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    AppNavGraph(navController = navController)
}