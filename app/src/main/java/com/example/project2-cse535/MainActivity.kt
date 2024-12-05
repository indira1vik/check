package com.example.project2_cse535

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import com.example.project2_cse535.navigation.NavGraph
import com.example.project2_cse535.ui.theme.Project2CSE535Theme
import com.example.project2_cse535.components.GameRecordDatabase
import com.example.project2_cse535.modules.GameDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GameDatabase.database = Room.databaseBuilder(
            applicationContext,
            GameRecordDatabase::class.java,
            "game_record_database"
        ).build()
        enableEdgeToEdge()
        setContent {
            Project2CSE535Theme {
                NavGraph()
            }
        }
    }
}