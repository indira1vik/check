package com.example.project2_cse535.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project2_cse535.modules.Screens
import com.example.project2_cse535.screens.HistoryScreen
import com.example.project2_cse535.screens.HomeScreen
import com.example.project2_cse535.screens.SettingsScreen
import com.example.project2_cse535.screens.SplashScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable(Screens.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(Screens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screens.HistoryScreen.route) {
            HistoryScreen(navController = navController)
        }
        composable(Screens.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }
    }
}