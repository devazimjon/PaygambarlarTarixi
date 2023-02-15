package uz.hilol.paygambarlartarixi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.hilol.paygambarlartarixi.data.database.model.FinishedBook

@Dao
interface FinishedBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookPage: FinishedBook)

    @Query("SELECT * FROM finished_books")
    suspend fun getAllFinishedBooks(): List<FinishedBook>
}