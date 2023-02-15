package uz.hilol.paygambarlartarixi.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content_content")
data class ContentEntity(
    @ColumnInfo(name = "docId")
    @PrimaryKey
    val id: Int?,
    @ColumnInfo(name = "c0book_id")
    val bookId: Int?,
    @ColumnInfo(name = "c1page_id")
    val pageId: Int?,
    @ColumnInfo(name = "c2text")
    val text: String?
)