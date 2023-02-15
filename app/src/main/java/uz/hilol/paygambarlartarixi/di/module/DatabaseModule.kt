package uz.hilol.paygambarlartarixi.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import uz.hilol.paygambarlartarixi.data.database.AppDatabase
import uz.hilol.paygambarlartarixi.data.database.databasehelper.SqlDatabaseHelper
import uz.hilol.paygambarlartarixi.di.annotation.AppScope

@Module
object DatabaseModule {

    @AppScope
    @Provides
    internal fun provideAppDatabase(context: Application): AppDatabase {
        return AppDatabase.create(context)
    }

    @AppScope
    @Provides
    internal fun provideSqlLiteDatabase(context: Application): SqlDatabaseHelper {
        return SqlDatabaseHelper(context)
    }

}