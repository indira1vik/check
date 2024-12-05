package com.example.project2_cse535.modules
import com.example.project2_cse535.components.GameRecordDatabase
import com.example.project2_cse535.components.GameRecordDao
import com.example.project2_cse535.components.GameRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

object ModeManager {
    var diffModeOption: String = "Easy"
}

object InitialConstants {
    const val PLAYER_X: String = "X"
    const val PLAYER_O: String = "O"
    const val EMPTY: String = " "
}

object WinnerString {
    const val AI_WINS: String = "AI Wins!!"
    const val HUMAN_WINS: String = "Human Wins!!"
    const val DRAW: String = "It's a Draw!!"
}
object GameDatabase {
    lateinit var database: GameRecordDatabase
    fun getGameRecordDao(): GameRecordDao = database.gameRecordDao()
}

object CustomCoroutineScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob() // Use Dispatchers.IO
}