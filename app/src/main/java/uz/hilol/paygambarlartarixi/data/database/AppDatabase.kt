package uz.hilol.paygambarlartarixi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.hilol.paygambarlartarixi.data.database.AppDatabase.Companion.DATABASE_VERSION
import uz.hilol.paygambarlartarixi.data.database.dao.*
import uz.hilol.paygambarlartarixi.data.database.model.*

@Database(
    entities = [
        FinishedBook::class,
        IndexedBookPage::class,
        IndexedContentEntity::class,
        ContentEntity::class,
        SectionEntity::class,
        BookmarkEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val indexedContentDao: IndexedContentDao
    abstract val finishedBooksDao: FinishedBookDao
    abstract val indexedBookDao: IndexedBookDao
    abstract val initialContentDao: InitialContentDao
    abstract val sectionDao: SectionDao
    abstract val bookmarkDao: BookmarkDao

    companion object {
        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        private const val DATABASE_NAME: String = "app_database"
        const val DATABASE_VERSION: Int = 1
    }
}