package uz.hilol.paygambarlartarixi.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "indexed_book")
data class IndexedBookPage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fontSize: Float,
    val contentIndex: Int,
    val text: String
)