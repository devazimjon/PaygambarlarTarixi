package uz.hilol.paygambarlartarixi.common.util

import java.util.*

object Constants {
    const val BASE_APP_SHARE_URL = "https://play.google.com/store/apps/details?id="

    const val BASE_URL = ""

    val LOCALE_LATIN = Locale("uz")
    val LOCALE_CYRILL = Locale("cr")

    const val PHONE_REGEX_UZ = "\\+998\\s?9\\d\\s?\\d{3}\\s?\\d{2}\\s?\\d{2}"
    const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$"

    const val REQUEST_CAMERA: Int = 212
    const val REQUEST_GALLERY: Int = 213
}