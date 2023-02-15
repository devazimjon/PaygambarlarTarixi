package uz.hilol.paygambarlartarixi.ui.reader

import android.util.Log
import android.view.MotionEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.hilol.paygambarlartarixi.common.pref.SettingsStorage
import uz.hilol.paygambarlartarixi.data.database.AppDatabase
import uz.hilol.paygambarlartarixi.data.database.model.SectionEntity
import javax.inject.Inject

class ReaderFragmentVM @Inject constructor(
    private val appDatabase: AppDatabase,
    private val settingsStorage: SettingsStorage
) : ViewModel() {

    private var contentLoadJob: Job? = null

    private var isTouchDown = false
        set(value) {
            field = value
            dragStartPosition = -1f
            dragEndPosition = -1f
        }
    private var dragVelocity = 0f
    private var dragStartPosition = 0f
    private var dragEndPosition = 0f

    val fontSize: Float
        get() = settingsStorage.fontSize

    var maxPage: Int = 10
    var currentPage: MutableLiveData<Int> = MutableLiveData(0)


    private var _isContentPickerOpened: MutableLiveData<Boolean> = MutableLiveData(false)
    val isContentPickerOpened: LiveData<Boolean> = _isContentPickerOpened

    private var _sections: MutableLiveData<List<SectionEntity>> = MutableLiveData()
    val sections: LiveData<List<SectionEntity>> = _sections

    private var _content: MutableLiveData<List<String>> = MutableLiveData()
    val content: LiveData<List<String>> = _content

    fun openContentPicker() {
        _isContentPickerOpened.value = true
    }

    fun openBook() = viewModelScope.launch {
        contentLoadJob?.cancel()
        val finishedFontBooks = appDatabase.finishedBooksDao.getAllFinishedBooks()
        val hasIndexedContent = finishedFontBooks.map { it.fontSize }.contains(fontSize)

        if (hasIndexedContent.not()) {
            contentLoadJob = launch {
                appDatabase.initialContentDao.getAllContent().collect {
                    maxPage = it.size
                    _content.postValue(it)
                }
            }
        } else {
            contentLoadJob = launch {
                appDatabase.indexedContentDao.getAllContent().collect {
//                    maxPage = it.size
//                    _content.postValue(it)
                }
            }
        }
    }

    fun loadSections() = viewModelScope.launch {
        appDatabase.sectionDao.getAllContent().collect {
            _sections.postValue(it)
        }
    }

    fun touchAction(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouchDown = true
            }
            MotionEvent.ACTION_UP -> {
                isTouchDown = false
                if (dragStartPosition == -1f)
                    closeContentPicker()
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("TOUCH", "ACTION_MOVE")
            }
            MotionEvent.ACTION_CANCEL -> {
                isTouchDown = false
            }
        }
    }

    fun closeContentPicker() {
        _isContentPickerOpened.value = false
    }
}