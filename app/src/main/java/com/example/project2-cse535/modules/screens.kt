package com.example.project2_cse535.modules

sealed class Screens(val route: String) {
    object SplashScreen: Screens("splash_screen")
    object HomeScreen: Screens("home_screen")
    object HistoryScreen: Screens("history_screen")
    object SettingsScreen: Screens("settings_screen")
}