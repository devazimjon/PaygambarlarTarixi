package uz.hilol.paygambarlartarixi.common.util

import android.content.res.Resources

fun dpToPx(dp: Int): Int {
    val metrics = Resources.getSystem().displayMetrics
    return (dp * metrics.density).toInt()
}