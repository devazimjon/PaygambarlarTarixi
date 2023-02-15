package uz.hilol.paygambarlartarixi.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val bookId: Int,
    val page: Int
)