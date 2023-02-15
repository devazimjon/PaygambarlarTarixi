package uz.hilol.paygambarlartarixi.common.extension

import android.graphics.Bitmap
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.drawToBitmap
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import uz.hilol.paygambarlartarixi.common.util.Constants.PHONE_REGEX_UZ


fun View.toBitmap(): Bitmap {
    return this.drawToBitmap()
}

fun ImageView.loadImage(url: String){
    //TODO: Add Coil
}

fun ImageView.setTint(color: Int) {
    this.drawable.colorFilter =
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
}

var TextInputEditText.parentError: String
    get() = "someMessage"
    set(value) {
        (this.parent.parent as TextInputLayout).error = value
    }

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}


fun EditText.isPhoneNumberValid(): Boolean {
    return Regex(PHONE_REGEX_UZ).matches("${text.replace("\\s+".toRegex(), "")}")
}

fun String.isStringMatchesPhoneNumber(): Boolean {
    return Regex(PHONE_REGEX_UZ).matches("${this.replace("\\s+".toRegex(), "")}")
}