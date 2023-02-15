package uz.hilol.paygambarlartarixi.common.pref

import android.content.SharedPreferences
import android.os.Build
import kotlin.reflect.KProperty

class PrefStringDelegate(val preferences: SharedPreferences) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return preferences.getString(property.name, "")!!
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        preferences.edit().putString(property.name, value).apply()
    }
}

class PrefLongDelegate(val preferences: SharedPreferences) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Long {
        return preferences.getLong(property.name, -1)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
        preferences.edit().putLong(property.name, value).apply()
    }
}

class PrefIntDelegate(val preferences: SharedPreferences, val defVal: Int) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return preferences.getInt(property.name, defVal)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        preferences.edit().putInt(property.name, value).apply()
    }
}

class PrefFloatDelegate(private val preferences: SharedPreferences, private val defVal: Float) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Float {
        return preferences.getFloat(property.name, defVal)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
        preferences.edit().putFloat(property.name, value).apply()
    }
}

class PrefBooleanDelegate(val preferences: SharedPreferences) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        return preferences.getBoolean(property.name, false)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        preferences.edit().putBoolean(property.name, value).apply()
    }
}

class PrefEncrypedStringDelegate(
    private val preferences: SharedPreferences,
    private val encryptedPreferences: SharedPreferences
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val oldToken = preferences.getString(property.name, "")!!
            if (oldToken.isNotEmpty()) {
                encryptedPreferences.edit().putString(property.name, oldToken).apply()
                preferences.edit().remove(property.name)
                return oldToken
            }
            encryptedPreferences.getString(property.name, "")!!
        } else {
            preferences.getString(property.name, "")!!
        }
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            encryptedPreferences.edit().putString(property.name, value).apply()
        } else {
            preferences.edit().putString(property.name, value).apply()
        }

    }
}

class PrefEncrypedIntDelegate(
    private val preferences: SharedPreferences,
    private val encryptedPreferences: SharedPreferences
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val oldValue = preferences.getInt(property.name,-1)
            if (oldValue != -1) {
                encryptedPreferences.edit().putInt(property.name, oldValue).apply()
                preferences.edit().remove(property.name)
                return oldValue
            }
            encryptedPreferences.getInt(property.name, -1)
        } else {
            preferences.getInt(property.name, -1)
        }
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            encryptedPreferences.edit().putInt(property.name, value).apply()
        } else {
            preferences.edit().putInt(property.name, value).apply()
        }
    }
}

class PrefEncrypedTokenDelegate(
    val preferences: SharedPreferences,
    val encryptedPreferences: SharedPreferences
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val oldToken = preferences.getString(property.name, "")!!
            if (oldToken.isNotEmpty()) {
                encryptedPreferences.edit().putString(property.name, oldToken).apply()
                preferences.edit().remove(property.name)
                return oldToken
            }
            encryptedPreferences.getString(property.name, "")!!
        } else {
            preferences.getString(property.name, "")!!
        }
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            encryptedPreferences.edit().putString(property.name, value).apply()
        } else {
            preferences.edit().putString(property.name, value).apply()
        }

    }
}