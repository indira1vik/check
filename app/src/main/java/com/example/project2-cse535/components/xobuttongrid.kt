package com.example.project2_cse535.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project2_cse535.modules.CustomCoroutineScope
import com.example.project2_cse535.modules.GameDatabase
import com.example.project2_cse535.modules.InitialConstants
import com.example.project2_cse535.modules.InitialConstants.PLAYER_O
import com.example.project2_cse535.modules.InitialConstants.PLAYER_X
import com.example.project2_cse535.modules.ModeManager
import com.example.project2_cse535.modules.ModeManager.diffModeOption
import com.example.project2_cse535.modules.WinnerString.DRAW
import com.example.project2_cse535.modules.checkWinnerGame
import com.example.project2_cse535.modules.findBestMove
import com.example.project2_cse535.modules.getRandomMove
import com.example.project2_cse535.modules.isMovesLeft
import com.example.project2_cse535.modules.mediumAI
import com.example.project2_cse535.screens.emptyBoard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@Composable
fun XOButtonGrid(
    gameBtn: MutableState<Boolean>,
    gameBoard: MutableState<List<List<String>>>,
    resetBtn: MutableState<Boolean>,
    startBtn: MutableState<Boolean>,
    currentPlayerState: MutableState<String>
) {
    val coroutineScope = CustomCoroutineScope
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .padding(24.dp)
            .border(2.dp, Color.DarkGray, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            gameBoard.value.forEachIndexed { rowInd, rows ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rows.forEachIndexed { colInd, cellValue ->
                        XOButton( // Use XOButton composable
                            cellValue = cellValue,
                            onButtonClick = {
                                clickGamePlayBtn(
                                    cellValue,
                                    gameBoard,
                                    rowInd,
                                    colInd,
                                    currentPlayerState,
                                    startBtn,
                                    resetBtn,
                                    gameBtn,
                                    context,
                                    coroutineScope
                                )
                            },
                            enabled = gameBtn.value && cellValue.isEmpty()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun XOButton(
    cellValue: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val textColor = when (cellValue) {
        PLAYER_X -> Color.Red
        PLAYER_O -> Color.Blue
        else -> Color.LightGray
    }
    Button(
        onClick = onButtonClick,
        modifier = modifier
            .size(64.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray
        ),
        enabled = enabled
    ) {
        Text(
            text = cellValue,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    }
}


fun clickGamePlayBtn(
    cellValue: String,
    gameBoard: MutableState<List<List<String>>>,
    rowInd: Int,
    colInd: Int,
    currentPlayerState: MutableState<String>,
    startBtn: MutableState<Boolean>,
    resetBtn: MutableState<Boolean>,
    gameBtn: MutableState<Boolean>,
    context: Context,
    coroutineScope: CoroutineScope
) {
    val gameRecordDao = GameDatabase.getGameRecordDao()
    if (cellValue.isEmpty()) {
        gameBoard.value = gameBoard.value.mapIndexed { currentIndex, rowList ->
            if (currentIndex == rowInd) {
                rowList.mapIndexed { columnIndex, eachColumn ->
                    if (columnIndex == colInd) {
                        currentPlayerState.value // Current player (O)
                    } else eachColumn
                }
            } else rowList
        }

        val playerResult = checkWinnerGame(gameBoard.value)
        if (playerResult.isNotEmpty()) {
            coroutineScope.launch {
                gameRecordDao.insertGameRecord(
                    GameRecord(
                        date = LocalDateTime.now(),
                        winner = "Human", // Or "Player" as appropriate
                        difficultyMode = ModeManager.diffModeOption
                    )
                )
            }
            Toast.makeText(context, playerResult, Toast.LENGTH_LONG).show()
            emptyBoard(gameBoard)
            startBtn.value = !startBtn.value
            resetBtn.value = !resetBtn.value
            gameBtn.value = !gameBtn.value
            return
        }

        var bestMove = findBestMove(gameBoard.value)
        if(diffModeOption =="Easy"){
            bestMove = getRandomMove(gameBoard.value)

        }else if(diffModeOption =="Medium") {
            bestMove = mediumAI(gameBoard.value)
        }
        gameBoard.value = gameBoard.value.mapIndexed { currentIndex, rowList ->
            if (currentIndex == bestMove.first) {
                rowList.mapIndexed { columnIndex, eachColumn ->
                    if (columnIndex == bestMove.second) {
                        PLAYER_X // AI's move (X)
                    } else eachColumn
                }
            } else rowList
        }

        val aiResult = checkWinnerGame(gameBoard.value)
        if (aiResult.isNotEmpty()) {
            Toast.makeText(context, aiResult, Toast.LENGTH_LONG).show()
            coroutineScope.launch {
            gameRecordDao.insertGameRecord(
                GameRecord(
                    date = LocalDateTime.now(),
                    winner = "Robot",
                    difficultyMode = ModeManager.diffModeOption
                )
            ) }
            emptyBoard(gameBoard)
            startBtn.value = !startBtn.value
            resetBtn.value = !resetBtn.value
            gameBtn.value = !gameBtn.value
            return
        }

        val charBoard = Array(3) { row ->
            CharArray(3) { col ->
                if (gameBoard.value[row][col].isNotEmpty()) gameBoard.value[row][col][0] else InitialConstants.EMPTY[0]
            }
        }

        if (!isMovesLeft(charBoard)) {
            Toast.makeText(context, DRAW, Toast.LENGTH_LONG).show()
            coroutineScope.launch {
                gameRecordDao.insertGameRecord(
                    GameRecord(
                        date = LocalDateTime.now(),
                        winner = "Draw", // Or "Player" as appropriate
                        difficultyMode = ModeManager.diffModeOption
                    )
                )
            }
            emptyBoard(gameBoard)
            startBtn.value = !startBtn.value
            resetBtn.value = !resetBtn.value
            gameBtn.value = !gameBtn.value
            return
        }
    }
}