package com.example.project2_cse535.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.project2_cse535.modules.BottomNavItem

@Composable
fun BottomBarNavFunction(navController: NavController, selectedItemIndex: Int) {
    val listScreens = listOf(
        BottomNavItem.Home,
        BottomNavItem.History,
        BottomNavItem.Settings
    )
    NavigationBar {
        listScreens.forEachIndexed { index, bottomNavItem ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    navController.navigate(bottomNavItem.route) {
                        popUpTo("home_screen")
                        launchSingleTop = true
                    }
                },
                label = {
                    Text(text = bottomNavItem.title)
                },
                icon = {
                    Icon(
                        imageVector = bottomNavItem.navIcon,
                        contentDescription = bottomNavItem.title
                    )
                }
            )
        }
    }
}