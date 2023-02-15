package uz.hilol.paygambarlartarixi.util

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.color(@ColorRes color: Int) : Int {
    return ContextCompat.getColor(requireContext(), color)
}