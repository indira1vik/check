package com.example.project2_cse535.modules

import com.example.project2_cse535.modules.InitialConstants.EMPTY
import com.example.project2_cse535.modules.InitialConstants.PLAYER_O
import com.example.project2_cse535.modules.InitialConstants.PLAYER_X
import com.example.project2_cse535.modules.WinnerString.AI_WINS
import com.example.project2_cse535.modules.WinnerString.HUMAN_WINS
import kotlin.random.Random

fun isMovesLeft(board: Array<CharArray>): Boolean {
    return board.any { row -> row.any { cell -> cell == EMPTY[0] } }
}

fun evaluate(board: Array<CharArray>): Int {
    for (row in 0 until 3) {
        if (board[row].all { it == PLAYER_X[0] }) return +10
        if (board[row].all { it == PLAYER_O[0] }) return -10
    }

    for (col in 0 until 3) {
        if ((0 until 3).all { row -> board[row][col] == PLAYER_X[0] }) return +10
        if ((0 until 3).all { row -> board[row][col] == PLAYER_O[0] }) return -10
    }

    if ((0 until 3).all { i -> board[i][i] == PLAYER_X[0] }) return +10
    if ((0 until 3).all { i -> board[i][i] == PLAYER_O[0] }) return -10

    if ((0 until 3).all { i -> board[i][2 - i] == PLAYER_X[0] }) return +10
    if ((0 until 3).all { i -> board[i][2 - i] == PLAYER_O[0] }) return -10

    return 0
}

fun minimizer(board: Array<CharArray>, depth: Int, alpha: Int, beta: Int): Int {
    val score = evaluate(board)

    if (score == 10) return score - depth
    if (score == -10) return score + depth
    if (!isMovesLeft(board)) return 0

    var best = Int.MAX_VALUE
    var currentBeta = beta

    for (i in 0 until 3) {
        for (j in 0 until 3) {
            if (board[i][j] == EMPTY[0]) {
                board[i][j] = PLAYER_O[0]

                best = minOf(best, maximizer(board, depth + 1, alpha, currentBeta))

                board[i][j] = EMPTY[0]

                currentBeta = minOf(currentBeta, best)
                if (currentBeta <= alpha) return best
            }
        }
    }

    return best
}

fun maximizer(board: Array<CharArray>, depth: Int, alpha: Int, beta: Int): Int {
    val score = evaluate(board)

    if (score == 10) return score - depth
    if (score == -10) return score + depth
    if (!isMovesLeft(board)) return 0

    var best = Int.MIN_VALUE
    var currentAlpha = alpha

    for (i in 0 until 3) {
        for (j in 0 until 3) {
            if (board[i][j] == EMPTY[0]) {
                board[i][j] = PLAYER_X[0]

                best = maxOf(best, minimizer(board, depth + 1, currentAlpha, beta))

                board[i][j] = EMPTY[0]

                currentAlpha = maxOf(currentAlpha, best)
                if (currentAlpha >= beta) return best
            }
        }
    }

    return best
}

fun getRandomMove(board: List<List<String>>): Pair<Int, Int> {
    val availableMoves = mutableListOf<Pair<Int, Int>>()

    for (i in board.indices) {
        for (j in board[i].indices) {
            if (board[i][j].isEmpty()) { // Assuming empty spaces are represented by an empty string
                availableMoves.add(Pair(i, j))
            }
        }
    }

    return if (availableMoves.isNotEmpty()) {
        availableMoves.random() // Randomly select an available move
    } else {
        Pair(-1, -1) // No moves left
    }
}

fun mediumAI(board: List<List<String>>): Pair<Int, Int> {
    return if (Random.nextDouble() < 0.5) {
        getRandomMove(board)
    } else {
        // 50% chance to use Minimax
        var bestVal = Int.MIN_VALUE
        var bestMove = Pair(-1, -1)

        // Create a mutable copy of the board
        val charBoard = Array(3) { row ->
            CharArray(3) { col ->
                if (board[row][col].isNotEmpty()) board[row][col][0] else ' ' // or whatever your EMPTY representation is
            }
        }

        for (i in charBoard.indices) {
            for (j in charBoard[i].indices) {
                if (charBoard[i][j] == ' ') {
                    charBoard[i][j] = PLAYER_X[0] // Make the move
                    val moveVal = minimizer(charBoard, 0, Int.MIN_VALUE, Int.MAX_VALUE) // Use Minimax
                    charBoard[i][j] = ' ' // Undo the move

                    if (moveVal > bestVal) {
                        bestVal = moveVal
                        bestMove = Pair(i, j)
                    }
                }
            }
        }
        bestMove
    }
}



fun getAvailableMoves(board: Array<CharArray>): List<Pair<Int, Int>> {
    val moves = mutableListOf<Pair<Int, Int>>()
    for (i in board.indices) {
        for (j in board[i].indices) {
            if (board[i][j] == ' ') {
                moves.add(Pair(i, j))
            }
        }
    }
    return moves
}

fun makeMove(board: Array<CharArray>, move: Pair<Int, Int>, player: Char) {
    board[move.first][move.second] = player
}

fun undoMove(board: Array<CharArray>, move: Pair<Int, Int>) {
    board[move.first][move.second] = ' '
}

fun findBestMove(board: List<List<String>>): Pair<Int, Int> {
    if (board.size != 3 || board.any { it.size != 3 }) {
        throw IllegalArgumentException("Invalid board size")
    }

    val charBoard = Array(3) { row ->
        CharArray(3) { col ->
            if (board[row][col].isNotEmpty()) board[row][col][0] else EMPTY[0]
        }
    }

    var bestVal = Int.MIN_VALUE
    var bestMove = Pair(-1, -1)

    for (i in 0 until 3) {
        for (j in 0 until 3) {
            if (charBoard[i][j] == EMPTY[0]) {
                charBoard[i][j] = PLAYER_X[0]
                val moveVal = minimizer(charBoard, 0, Int.MIN_VALUE, Int.MAX_VALUE)
                charBoard[i][j] = EMPTY[0]
                if (moveVal > bestVal) {
                    bestMove = Pair(i, j)
                    bestVal = moveVal
                }
            }
        }
    }
    return bestMove
}


fun checkWinnerGame(board: List<List<String>>): String {

    for (row in board) {
        if (row.all { it == PLAYER_X }) return AI_WINS
        if (row.all { it == PLAYER_O }) return HUMAN_WINS
    }


    for (col in 0 until 3) {
        if (board.all { it[col] == PLAYER_X }) return AI_WINS
        if (board.all { it[col] == PLAYER_O }) return HUMAN_WINS
    }


    if ((0 until 3).all { board[it][it] == PLAYER_X }) return AI_WINS
    if ((0 until 3).all { board[it][it] == PLAYER_O }) return HUMAN_WINS
    if ((0 until 3).all { board[it][2 - it] == PLAYER_X }) return AI_WINS
    if ((0 until 3).all { board[it][2 - it] == PLAYER_O }) return HUMAN_WINS

    return ""
}