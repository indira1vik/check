package com.example.project2_cse535.modules

import android.view.Display.Mode
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import com.example.project2_cse535.screens.emptyBoard
import com.example.project2_cse535.components.GameRecordDao
import com.example.project2_cse535.components.GameRecord
import com.example.project2_cse535.modules.GameDatabase
import java.time.LocalDateTime


fun checkWinner(
    gameBoard: MutableState<List<List<String>>>,
    startBtn: MutableState<Boolean>,
    resetBtn: MutableState<Boolean>,
    gameBtn: MutableState<Boolean>
): String {
    val gameRecordDao = GameDatabase.getGameRecordDao()
    val score: Int = checkScore(gameBoard)
    if (score == 10) {
        println("Result: Robot Won")
        startBtn.value = !startBtn.value
        resetBtn.value = !resetBtn.value
        gameBtn.value = !gameBtn.value
        gameBoard.value = emptyBoard(gameBoard)
        gameRecordDao.insertGameRecord(
            GameRecord(
                date = LocalDateTime.now(),
                winner = "Robot", // Or "Player" as appropriate
                difficultyMode = ModeManager.diffModeOption
            )
        )
        return "Robot Won the Game!!"
    } else if (score == -10) {
        println("Result: Human Won")
        startBtn.value = !startBtn.value
        resetBtn.value = !resetBtn.value
        gameBtn.value = !gameBtn.value
        gameBoard.value = emptyBoard(gameBoard)
        gameRecordDao.insertGameRecord(
            GameRecord(
                date = LocalDateTime.now(),
                winner = "Human", // Or "Player" as appropriate
                difficultyMode = ModeManager.diffModeOption
            )
        )
        return "Human Won the Game!!"
    }
    if (gameBoard.value.flatten().all {
        it.isNotEmpty()
    } && score == 0) {
        println("Result: It's a Draw")
        startBtn.value = !startBtn.value
        resetBtn.value = !resetBtn.value
        gameBtn.value = !gameBtn.value
        gameBoard.value = emptyBoard(gameBoard)
        gameRecordDao.insertGameRecord(
            GameRecord(
                date = LocalDateTime.now(),
                winner = "Draw", // Or "Player" as appropriate
                difficultyMode = ModeManager.diffModeOption
            )
        )
        return "It's a Draw Match!!"
    }
    return ""
}

fun checkScore(gameBoard: MutableState<List<List<String>>>): Int {
    for(row in 0..2) {
        if (gameBoard.value[row][0] === gameBoard.value[row][1] && gameBoard.value[row][1] === gameBoard.value[row][2]) {
            if (gameBoard.value[row][0] == "X" && gameBoard.value[row][0].isNotEmpty()) {
                return +1
            } else if (gameBoard.value[row][0] == "O" && gameBoard.value[row][0].isNotEmpty()) {
                return -1
            }
        }
    }
    for(col in 0..2) {
        if (gameBoard.value[0][col] === gameBoard.value[1][col] && gameBoard.value[1][col] === gameBoard.value[2][col]) {
            if (gameBoard.value[0][col] == "X" && gameBoard.value[0][col].isNotEmpty()) {
                return +1
            } else if (gameBoard.value[0][col] == "O" && gameBoard.value[0][col].isNotEmpty()) {
                return -1
            }
        }
    }
    if (gameBoard.value[0][0] === gameBoard.value[1][1] && gameBoard.value[1][1] === gameBoard.value[2][2]) {
        if (gameBoard.value[0][0] == "X" && gameBoard.value[0][0].isNotEmpty()) {
            return +1
        } else if (gameBoard.value[0][0] == "O" && gameBoard.value[0][0].isNotEmpty()) {
            return -1
        }
    }
    if (gameBoard.value[0][2] === gameBoard.value[1][1] && gameBoard.value[1][1] === gameBoard.value[2][0]) {
        if (gameBoard.value[0][2] == "X" && gameBoard.value[0][2].isNotEmpty()) {
            return +1
        } else if (gameBoard.value[0][2] == "O" && gameBoard.value[0][2].isNotEmpty()) {
            return -1
        }
    }
    return 0
}

