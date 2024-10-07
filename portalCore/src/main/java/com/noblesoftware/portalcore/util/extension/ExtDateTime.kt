package com.noblesoftware.portalcore.util.extension

import android.text.format.DateFormat
import com.noblesoftware.portalcore.util.DateTimeHelper
import java.text.SimpleDateFormat
import java.util.*

/**
 * Return date in yyyy-MM-dd format.
 *
 * Note: Month is 1 to 12.
 */
fun Date?.getDateTime(format: String = "yyyy-MM-dd'T'HH:mm:ss'Z'"): String {
    if (this == null) {
        return ""
    }

    return try {
        return DateFormat.format(format, this).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun Date?.getYYYYMMDD(): String {
    if (this == null) {
        return ""
    }

    return try {
        return DateFormat.format("yyyy-MM-dd", this).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun Date?.getHumanReadableDate(): String {
    if (this == null) {
        return ""
    }

    val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH)
    return try {
        simpleDateFormat.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun Date?.getHumanReadableDateTime(): String {
    if (this == null) {
        return ""
    }

    val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy, hh:mm a", Locale.ENGLISH)
    return try {
        simpleDateFormat.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun Date.formatDisplay(): String = DateTimeHelper.format(
    this, DateTimeHelper.FORMAT_DATE_TIME_DMY_SHORT_MONTH_WITH_SPACE
)

fun Date.formatDefault(): String = DateTimeHelper.format(
    this, DateTimeHelper.FORMAT_DATE_YMD
)
