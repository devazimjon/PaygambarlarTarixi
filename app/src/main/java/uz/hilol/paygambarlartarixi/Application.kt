package uz.hilol.paygambarlartarixi

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.yariksoffice.lingver.Lingver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader
import uz.hilol.paygambarlartarixi.common.util.Constants
import uz.hilol.paygambarlartarixi.data.database.AppDatabase
import uz.hilol.paygambarlartarixi.data.database.model.FinishedBook
import uz.hilol.paygambarlartarixi.di.AppComponent
import uz.hilol.paygambarlartarixi.util.reader.BookPageIndexer
import java.io.InputStream
import java.nio.charset.Charset

class Application : MultiDexApplication() {

    private lateinit var appComponent: AppComponent
    private var indexerJob = Job()
    private var databaseConverterJob = Job()

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        appComponent = AppComponent.create(this)
        appComponent.inject(this)
        configureDatabase(appComponent)

        var language = appComponent.settingsStorage.language
        if (language.isEmpty()) {
            appComponent.settingsStorage.language = Constants.LOCALE_LATIN.language
            language = Constants.LOCALE_LATIN.language
        }
        Lingver.init(this, language)

//        indexing()
    }

    private fun configureDatabase(appComponent: AppComponent) =
        GlobalScope.launch(context = Dispatchers.Default + databaseConverterJob) {
            val settings = appComponent.settingsStorage

            if (!settings.isDatabaseInitialized) {
                val database = appComponent.database
                val dbHelper = appComponent.dbHelper.apply { openDataBase() }
                database.initialContentDao.insert(dbHelper.content)
                database.sectionDao.insert(dbHelper.section)
                settings.isDatabaseInitialized = true
            }

        }

    private fun indexing(appComponent: AppComponent) {
        GlobalScope.launch(context = Dispatchers.Default + indexerJob) {
            val database: AppDatabase = appComponent.database
            val bookName = "book3.epub"

            database.finishedBooksDao.getAllFinishedBooks().let {
                if (it.isEmpty() || it.find { it.name == bookName } == null) {
                    val contentList = openBookContent(bookName)

                    BookPageIndexer(baseContext, database).startIndexer(contentList)
                    database.finishedBooksDao.insert(FinishedBook(name = bookName))

                }
            }
        }
    }

    private fun openBookContent(bookName: String): List<String> {
        val epubInputStream: InputStream = assets
            .open(bookName)

        val book: Book = EpubReader().readEpub(epubInputStream)

        return book.contents.map { String(it.data, Charset.forName(it.inputEncoding)) }
    }

    override fun onTerminate() {
        indexerJob.cancel()
        databaseConverterJob.cancel()
        super.onTerminate()
    }
}