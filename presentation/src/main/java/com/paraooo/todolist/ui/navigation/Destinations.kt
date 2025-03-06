package com.paraooo.todolist.ui.navigation

sealed class Destinations(val route: String) {
    data object Home : Destinations("home")
    data object Splash : Destinations("splash")
    data object Create : Destinations("create")
    data object Edit : Destinations("edit")
    data object Setting : Destinations("setting")
    data object PrivacyPolicy : Destinations("privacy_policy")
}