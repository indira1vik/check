package com.example.project2_cse535.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.room.Room
import com.example.project2_cse535.components.BottomBarNavFunction
import com.example.project2_cse535.components.CreateDataTable
import com.example.project2_cse535.components.GameRecordDatabase
import com.example.project2_cse535.components.PageTitleHeader
import com.example.project2_cse535.modules.GameDatabase

@Composable
fun HistoryScreen(navController: NavController){
    val gameRecordDatabase = remember { GameDatabase.database }
    Scaffold(
        bottomBar = { BottomBarNavFunction(navController, 1) }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PageTitleHeader("Game History")
            CreateDataTable(gameRecordDatabase)
        }
    }
}