package uz.hilol.paygambarlartarixi.util

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.TextView

fun TextView.setHtmlText(html: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(html)
    }
}

fun Float.spToPx(): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)

fun Float.dpToPx(): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

