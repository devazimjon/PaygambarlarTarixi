package uz.hilol.paygambarlartarixi.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "indexed_content")
data class IndexedContentEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,
    val startPage: Int,
    val startIndex: Int,
    val endPage: Int,
    val endIndex: Int,
    val fontSize: Float
)