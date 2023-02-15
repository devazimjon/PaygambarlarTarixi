package uz.hilol.paygambarlartarixi.data.database.databasehelper

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.hilol.paygambarlartarixi.data.database.model.ContentEntity
import uz.hilol.paygambarlartarixi.data.database.model.SectionEntity
import java.io.*

class SqlDatabaseHelper(private val ctx: Context) :
    SQLiteOpenHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION) {

    private val databasePath: String
        private get() = (ctx.applicationInfo.dataDir.toString() + DB_PATH_SUFFIX
                + DATABASE_NAME)

    val content: ArrayList<ContentEntity>
        get() {
            val db = this.readableDatabase
            val contList: ArrayList<ContentEntity> = ArrayList()
            val cursor: Cursor? = db.rawQuery("SELECT * FROM content_content", null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val cont =
                        ContentEntity(
                            id = cursor.getInt(0),
                            bookId = cursor.getInt(1),
                            pageId = cursor.getInt(2),
                            text = cursor.getString(3)
                        )
                    contList.add(cont)
                }
                cursor.close()
                db.close()
            }
            return contList
        }

    val section: ArrayList<SectionEntity>
        get() {
            val db = this.readableDatabase
            val sections: ArrayList<SectionEntity> = ArrayList()
            val cursor: Cursor? = db.rawQuery("SELECT * FROM section", null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val cont =
                        SectionEntity(
                            id = cursor.getInt(0),
                            pageNumber = cursor.getInt(1),
                            startIndex = cursor.getInt(2),
                            name = cursor.getString(3)
                        )
                    sections.add(cont)
                }
                cursor.close()
                db.close()
            }
            return sections
        }

    @Throws(IOException::class)
    private fun copyDataBaseFromAsset() {
        val myInput: InputStream = ctx.assets.open("database/$DATABASE_NAME")
        val outFileName = databasePath

// if the path doesn't exist first, create it
        val f = File(ctx.applicationInfo.dataDir.toString() + DB_PATH_SUFFIX)
        if (!f.exists()) f.mkdir()

// Open the empty db as the output stream
        val myOutput: OutputStream = FileOutputStream(outFileName)

// transfer bytes from the inputfile to the outputfile
        val buffer = ByteArray(1024)
        var length: Int
        while (myInput.read(buffer).also { length = it } > 0) {
            myOutput.write(buffer, 0, length)
        }
        // Close the streams
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }

    @Throws(SQLException::class)
    fun openDataBase(): SQLiteDatabase {
        val dbFile: File = ctx.getDatabasePath(DATABASE_NAME)
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAsset()
                println("Copying sucess from Assets folder")
            } catch (e: IOException) {
                throw RuntimeException("Error creating source database", e)
            }
        }
        return SQLiteDatabase.openDatabase(
            dbFile.path,
            null,
            SQLiteDatabase.NO_LOCALIZED_COLLATORS or SQLiteDatabase.CREATE_IF_NECESSARY
        )
    }

    override fun onCreate(db: SQLiteDatabase) {}
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "masnaviy-1-1.db"
        private const val DB_PATH_SUFFIX = "/databases/"

    }
}