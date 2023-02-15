package uz.hilol.paygambarlartarixi.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE
import uz.hilol.paygambarlartarixi.data.database.model.BookmarkEntity

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookPage: BookmarkEntity)

    @Delete
    suspend fun delete(bookPage: BookmarkEntity)

    @Query("SELECT * FROM bookmarks")
    suspend fun getBookmarks(): List<BookmarkEntity>

    @Query("SELECT * FROM bookmarks")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>
}