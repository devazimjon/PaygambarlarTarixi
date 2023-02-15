package uz.hilol.paygambarlartarixi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.hilol.paygambarlartarixi.data.database.model.IndexedBookPage
import uz.hilol.paygambarlartarixi.data.database.model.IndexedContentEntity

@Dao
interface IndexedContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(page: IndexedContentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pages: List<IndexedContentEntity>)

    @Query("SELECT * FROM indexed_content")
    fun getAllContent(): Flow<List<IndexedContentEntity>>
}