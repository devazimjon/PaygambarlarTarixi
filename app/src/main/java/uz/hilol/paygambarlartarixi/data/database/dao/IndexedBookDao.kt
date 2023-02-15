package uz.hilol.paygambarlartarixi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.hilol.paygambarlartarixi.data.database.model.IndexedBookPage

@Dao
interface IndexedBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(bookPage: IndexedBookPage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPage(bookPage: List<IndexedBookPage>)

    @Query("SELECT * FROM indexed_book")
    fun getAllPages(): Flow<List<IndexedBookPage>>
}