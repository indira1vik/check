package com.example.project2_cse535.modules

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem (
    val route: String,
    val title: String,
    val navIcon: ImageVector
) {
    object Home: BottomNavItem(
        route = "home_screen",
        title = "Home",
        navIcon = Icons.Rounded.Home
    )
    object History: BottomNavItem(
        route = "history_screen",
        title = "History",
        navIcon = Icons.Rounded.Refresh
    )
    object Settings: BottomNavItem(
        route = "settings_screen",
        title = "Settings",
        navIcon = Icons.Rounded.Settings
    )
}