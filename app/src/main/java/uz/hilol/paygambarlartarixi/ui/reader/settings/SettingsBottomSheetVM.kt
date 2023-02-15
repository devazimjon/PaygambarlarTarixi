package uz.hilol.paygambarlartarixi.ui.reader.settings

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.hilol.paygambarlartarixi.common.pref.SettingsStorage
import uz.hilol.paygambarlartarixi.data.database.AppDatabase
import uz.hilol.paygambarlartarixi.data.database.model.BookmarkEntity
import javax.inject.Inject

class SettingsBottomSheetVM @Inject constructor(
    private val database: AppDatabase,
    private val settingsStorage: SettingsStorage
) : ViewModel(), LifecycleObserver {

    var maxPage = 10
    var currentPage = MutableLiveData(1)

    private var _isBookmarkEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val isBookmarkEnabled: LiveData<Boolean> = _isBookmarkEnabled


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onLifecycleCreate() = viewModelScope.launch {
        //observe current bookmark state
        updateBookmark()
    }

    fun updateBookmark() = viewModelScope.launch(Dispatchers.IO) {
        val bookmarks = database.bookmarkDao.getBookmarks()
        val bookmarkPage = bookmarks.find { it.page == currentPage.value }
        _isBookmarkEnabled.postValue(bookmarkPage != null)
    }

    fun onClickBookmark() = viewModelScope.launch {
        val bookmarks = database.bookmarkDao.getBookmarks()
        val bookmarkPage = bookmarks.find { it.page == currentPage.value }

        if (bookmarkPage != null) {
            _isBookmarkEnabled.postValue(false)
            database.bookmarkDao.delete(bookmarkPage)
        } else {
            _isBookmarkEnabled.postValue(true)
            database.bookmarkDao.insert(BookmarkEntity(bookId = 1, page = currentPage.value!!))
        }
    }

}