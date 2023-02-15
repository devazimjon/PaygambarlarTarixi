package uz.hilol.paygambarlartarixi.ui.reader.contentpicker

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import uz.hilol.paygambarlartarixi.data.database.AppDatabase
import javax.inject.Inject

class ContentPickerFragmentVM @Inject constructor(
    private var database: AppDatabase
) : ViewModel(), LifecycleObserver {

    val sections
        get() = database.sectionDao.getAllContent()
    val bookmarks
        get() = database.bookmarkDao.getAllBookmarks()

    var currentContentState: MutableStateFlow<ContentPickerType> =
        MutableStateFlow(ContentPickerType.SECTION)

    private var _content = currentContentState.flatMapLatest {
        if (it == ContentPickerType.SECTION)
            sections
        else
            bookmarks
    }
    val content = _content.asLiveData()


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {

    }

    enum class ContentPickerType {
        SECTION,
        BOOKMARK
    }
}

