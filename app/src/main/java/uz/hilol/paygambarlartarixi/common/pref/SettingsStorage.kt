package uz.hilol.paygambarlartarixi.common.pref

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import javax.inject.Inject

class SettingsStorage @Inject constructor(
    context: Context
) : BaseSettings(context) {
    var isDatabaseInitialized by prefBoolean()
    var language by prefString()

    //todo temporary values
    var userId by prefInt()
    var userName by prefString()
    var userEmail by prefString()
    var userPhone by prefString()
    var userGender by prefBoolean()
    var maxImageLoad by prefInt(8)
    var fontSize by prefFloat(20f)
    var orientation by prefInt(ViewPager2.ORIENTATION_HORIZONTAL)
    var nightMode by prefInt(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    fun logout() {
        language = "uz"
        userName = ""
        userId = -1
    }
}