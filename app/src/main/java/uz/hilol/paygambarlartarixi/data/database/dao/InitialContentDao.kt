package uz.hilol.paygambarlartarixi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.hilol.paygambarlartarixi.data.database.model.ContentEntity

@Dao
interface InitialContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contents: List<ContentEntity>)

    @Query("SELECT c2text FROM content_content")
    fun getAllContent(): Flow<List<String>>
}