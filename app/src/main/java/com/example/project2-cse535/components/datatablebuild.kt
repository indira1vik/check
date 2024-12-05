package com.example.project2_cse535.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity(tableName = "game_records")
data class GameRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "date") val date: LocalDateTime,
    @ColumnInfo(name = "winner") val winner: String,
    @ColumnInfo(name = "difficulty_mode") val difficultyMode: String
)

@Dao
interface GameRecordDao {
    @Query("SELECT * FROM game_records")
    fun getAllGameRecords(): Flow<List<GameRecord>>

    @Insert
    fun insertGameRecord(gameRecord: GameRecord)

}

@Database(
    entities = [GameRecord::class],
    version = 1,
    exportSchema = false
) // Set exportSchema to false
@TypeConverters(LocalDateTimeConverter::class)
abstract class GameRecordDatabase : RoomDatabase() {
    abstract fun gameRecordDao(): GameRecordDao
}

object LocalDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { formatter.parse(it, LocalDateTime::from) }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(formatter)
    }
}


@Composable
fun CreateDataTable(gameRecordDatabase: GameRecordDatabase) {
    val gameRecordsState =
        gameRecordDatabase.gameRecordDao().getAllGameRecords().collectAsState(initial = emptyList())
    val gameRecords = gameRecordsState.value.sortedByDescending { it.date }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp, 8.dp, 32.dp, 0.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) { TextDataFunc("Date", true) }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) { TextDataFunc("Winner", true) }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) { TextDataFunc("Mode", true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp, 8.dp, 32.dp, 32.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
    ) {
        // Data Rows
        items(gameRecords) { gameRecord ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    TextDataFunc(text = gameRecord.date.format(DateTimeFormatter.ofPattern("dd MMM, yyyy")), false)

                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    TextDataFunc(text = gameRecord.winner, false)

                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    TextDataFunc(text = gameRecord.difficultyMode, false)
                }
            }
        }
    }
}

@Composable
fun TextDataFunc(text: String, yesBold: Boolean) {
    val checkFont = when (yesBold) {
        true -> FontWeight.Bold
        false -> FontWeight.Normal
    }
    Text(
        text = text,
        modifier = Modifier
            .padding(8.dp),
        textAlign = TextAlign.Center,
        fontWeight = checkFont
    )
}

