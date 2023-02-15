package uz.hilol.paygambarlartarixi.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "section")
data class SectionEntity(
    @ColumnInfo(name = "_id")
    @PrimaryKey
    val id: Int,
    val pageNumber: Int,
    val startIndex: Int,
    val name: String
)