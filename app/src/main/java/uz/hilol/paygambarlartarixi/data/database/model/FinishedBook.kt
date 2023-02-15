package uz.hilol.paygambarlartarixi.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "finished_books")
data class FinishedBook(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fontSize: Float = 20f,
    val name: String
)