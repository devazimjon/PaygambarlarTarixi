package uz.hilol.paygambarlartarixi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.hilol.paygambarlartarixi.data.database.model.SectionEntity

@Dao
interface SectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sections: List<SectionEntity>)

    @Query("SELECT * FROM section")
    fun getAllContent(): Flow<List<SectionEntity>>
}