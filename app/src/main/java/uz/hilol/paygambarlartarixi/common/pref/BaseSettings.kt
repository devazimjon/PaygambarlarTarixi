package uz.hilol.paygambarlartarixi.common.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

open class BaseSettings(val context: Context) {

    val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            "settings",
            Context.MODE_PRIVATE
        );
    }

    fun prefString() = PrefStringDelegate(preferences)
//    fun prefLong(defVal: Long = 0L) = PrefLongDelegate(preferences)
    fun prefInt(defVal: Int = -1) = PrefIntDelegate(preferences, defVal)
    fun prefFloat(defVal: Float = -1f) = PrefFloatDelegate(preferences, defVal)
    fun prefBoolean() = PrefBooleanDelegate(preferences)
}