package com.example.project2_cse535.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project2_cse535.components.BottomBarNavFunction
import com.example.project2_cse535.components.PageTitleHeader
import com.example.project2_cse535.components.XOButtonGrid


@Composable
fun HomeScreen(navController: NavController) {
    val startBtn = remember { mutableStateOf(true) }
    val resetBtn = remember { mutableStateOf(false) }
    val gameBtn = remember { mutableStateOf(false) }
    val gameBoard = remember { mutableStateOf(List(3) { List(3) { "" } }) }
    val currentPlayerState = remember { mutableStateOf("O") }

    Scaffold(
        bottomBar = { BottomBarNavFunction(navController, 0) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PageTitleHeader("3T17")
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.64f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(36.dp))
                            .background(Color.LightGray)
                            .padding(16.dp, 8.dp)

                    ) {
                        Text(text = "Human (O)")
                    }
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(36.dp))
                            .background(Color.LightGray)
                            .padding(16.dp, 8.dp)

                    ) {
                        Text(text = "Robot (X)")
                    }
                }
                XOButtonGrid(gameBtn, gameBoard, resetBtn, startBtn, currentPlayerState)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(0.64f),
                        onClick = {
                            startBtn.value = !startBtn.value
                            resetBtn.value = !resetBtn.value
                            gameBtn.value = !gameBtn.value
                            currentPlayerState.value = "O"
                        },
                        enabled = startBtn.value
                    ) {
                        Text(text = "Play")
                    }
                    Button(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(0.64f),
                        onClick = {
                            startBtn.value = !startBtn.value
                            resetBtn.value = !resetBtn.value
                            gameBtn.value = !gameBtn.value
                            gameBoard.value = emptyBoard(gameBoard)
                        },
                        enabled = resetBtn.value
                    ) {
                        Text(text = "Reset")
                    }
                }
            }
        }
    }
}

fun emptyBoard(gameBoard: MutableState<List<List<String>>>): List<List<String>> {
    gameBoard.value = List(3) { List(3) { "" } }
    return gameBoard.value
}


@Preview
@Composable
fun TicPreviewOne() {
    HomeScreen(navController = rememberNavController())
}