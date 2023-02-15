package uz.hilol.paygambarlartarixi.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.hilol.paygambarlartarixi.common.pref.SettingsStorage
import uz.hilol.paygambarlartarixi.common.util.Connectivity
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val settingsStorage: SettingsStorage,
    private val connectivity: Connectivity,
) : ViewModel() {

    private val _bookOrientation = MutableLiveData(settingsStorage.orientation)
    val bookOrientation: LiveData<Int> = _bookOrientation

    private val _fontSize = MutableLiveData(settingsStorage.fontSize)
    val fontSize: LiveData<Float> = _fontSize

    private val eventChannel = Channel<SingleEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun changeBookOrientation(@ViewPager2.Orientation orientation: Int) {
        settingsStorage.orientation = orientation
        _bookOrientation.postValue(orientation)
    }

    fun increaseFontSize() {
        val newFontSize = settingsStorage.fontSize + 2
        if (newFontSize <= MAX_FONT_SIZE) {
            settingsStorage.fontSize = newFontSize
            _fontSize.postValue(newFontSize)
            fontSizeChanged()
        }
    }

    fun decreaseFontSize() {
        val newFontSize = settingsStorage.fontSize - 2
        if (newFontSize >= MIN_FONT_SIZE) {
            settingsStorage.fontSize = newFontSize
            _fontSize.postValue(newFontSize)
            fontSizeChanged()
        }
    }

    fun fontSizeChanged() = viewModelScope.launch {
        eventChannel.send(SingleEvent.FontChanged)
    }


    sealed class SingleEvent {
        object FontChanged : SingleEvent()
    }

    companion object {
        const val MIN_FONT_SIZE = 18
        const val MAX_FONT_SIZE = 24
    }
}