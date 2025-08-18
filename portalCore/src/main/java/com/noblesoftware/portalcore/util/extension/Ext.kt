package com.noblesoftware.portalcore.util.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Picture
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.google.gson.GsonBuilder
import com.noblesoftware.portalcore.R

/**
 * Created by fizhu on 22,May,2020
 * Hvyz.anbiya@gmail.com
 */

fun Any.toJsonPretty(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

fun <T> ArrayList<T>?.orArrayListEmpty(): ArrayList<T> = this ?: arrayListOf()

inline fun <reified T : Parcelable> Bundle.parcelize(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this.getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") this.getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelizeList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this.getParcelableArrayList(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") this.getParcelableArrayList(key)
}

inline fun <reified T : Parcelable> Intent.parcelize(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this.getParcelableExtra(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") this.getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Intent.parcelizeList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this.getParcelableArrayListExtra(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") this.getParcelableArrayListExtra(key)
}

inline fun <reified T : java.io.Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this.getSerializable(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") this.getSerializable(key) as? T
}

inline fun <reified T : java.io.Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this.getSerializableExtra(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") this.getSerializableExtra(key) as? T
}

fun Picture.asBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        this.width,
        this.height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.WHITE)
    canvas.drawPicture(this)
    return bitmap
}

fun Modifier.shimmerEffect(): Modifier = composed {
    val size = remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX = transition.animateFloat(
        initialValue = -2 * size.value.width.toFloat(),
        targetValue = 2 * size.value.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "shimmer"
    )

    val bgColor = MaterialTheme.colorScheme.background
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                bgColor.blendWithLuminance(0.1f),
                bgColor.blendWithLuminance(0.2f),
                bgColor.blendWithLuminance(0.1f),
            ),
            start = Offset(startOffsetX.value, 0f),
            end = Offset(
                startOffsetX.value + size.value.width.toFloat(),
                size.value.height.toFloat()
            )
        )
    )
        .onGloballyPositioned {
            size.value = it.size
        }
}

fun Color.blendWithLuminance(ratio: Float): Color {
    val isDark = this.luminance() < 0.5f
    return Color(
        ColorUtils.blendARGB(
            this.toArgb(),
            (if (isDark) Color.White else Color.Black).toArgb(),
            ratio
        )
    )
}

/**
 * Open given [url] to a browser.
 */
fun Context.openUrl(url: String) {
    kotlin.runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }.onFailure {
        it.printStackTrace()
        longToast("Cannot open the link!")
    }
}


/**
 * to get activity from compose context
 */
fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

/**
 * Intent to app settings
 */
fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

fun Activity.openLocationSettings() =
    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).also(::startActivity)

fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
    permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

fun Context.getDrawableCompat(@DrawableRes drawableResId: Int) =
    ContextCompat.getDrawable(this, drawableResId)

/**
 * to get color compat
 * @param [colorResId] color resource id
 */
fun Context.getColorCompat(@ColorRes colorResId: Int) =
    ContextCompat.getColor(this, colorResId)

fun Context.getColorStateListCompat(@ColorRes colorResId: Int) =
    ContextCompat.getColorStateList(this, colorResId)

/**
 * to check status success in api response
 */
fun String?.isSuccess() = this?.equals("success").orFalse()

fun String?.orStrip() = if (!this.isNullOrBlank()) this else "-"
fun String?.isStrip() = this == "-"
fun String?.isNotStrip() = this.isStrip().isFalse()

/**
 * convert string value null to zero
 */
fun String?.orzero() = if (!this.isNullOrBlank()) this else "0"

fun String?.toGender(): Int = when (this?.lowercase()) {
    "male" -> R.string.male
    "female" -> R.string.female
    else -> R.string.strip
}

fun String.toDayResId(): Int = when (this) {
    "Sunday" -> R.string.sunday
    "Monday" -> R.string.monday
    "Tuesday" -> R.string.tuesday
    "Wednesday" -> R.string.wednesday
    "Thursday" -> R.string.thursday
    "Friday" -> R.string.friday
    "Saturday" -> R.string.saturday
    else -> R.string.strip
}

fun String?.formatErrorMessage(context: Context): String {
    this?.let {
        if (httpErrors.any { error -> it.trim().lowercase().contains(error, true) }) {
            return context.getString(R.string.something_went_wrong)
        } else if (timeoutErrors.any { error -> it.trim().lowercase().contains(error, true) }) {
            return context.getString(R.string.failed_to_load_please_try_again_later)
        } else {
            return it
        }
    }
    return context.getString(R.string.something_went_wrong)
}

val httpErrors = listOf(
    "http",
    "https",
    "exception",
    "null",
    "port",
    "backend",
    "invalid pk",
    "400",
    "401",
    "402",
    "403",
    "404",
    "443",
    "500",
    "501",
    "503",
)

val timeoutErrors = listOf(
    "timeout"
)

fun String.toMathEqFormatted(): String {
    var newData = this
    val regexOverallFormat = Regex("""<span class=(.*math.*)>(.*?)</span>""")
    val regexForSplit = Regex("""<span[^>]*>(?:[^<]|(?!</span>)<).+?</span>""")
    newData = newData.replace(regexForSplit) { splittedData ->
        val replacedString = splittedData.value.replace(regexOverallFormat) { matchResult ->
            var newValue = matchResult.value
            val replacedValue = matchResult.value.replace("<span class=\"math-tex\">", "")
                .replace("<span class=\"math inline\">", "").replace("</span>", "")
            if (!(replacedValue.startsWith("\\( ") && replacedValue.endsWith(" \\)"))) {
                var formattedValue = replacedValue
                if (formattedValue.startsWith("\\(")) {
                    formattedValue = formattedValue.replace("\\(", "")
                }
                if (formattedValue.endsWith("\\)")) {
                    formattedValue = formattedValue.replace("\\)", "")
                }
                newValue = "\\( $formattedValue \\)"
            }
            return@replace newValue
        }
        return@replace replacedString
    }
    newData = when {
        newData.contains("""\(""") -> {
            newData.replace("""\(""", "$", true).replace("""\)""", "$", true)
        }

        newData.contains("""\[""") -> {
            newData.replace("""\[""", "$$", true).replace("""\]""", "$$", true)
        }

        else -> {
            newData
        }
    }


    return newData
}

/**
 * Convert [Int] into [mm:ss]
 */
fun Int.toMinuteSeconds(): String =
    runCatching {
        val minute = this / 60
        val strMinute = if (minute < 10) "0$minute" else minute.toString()

        val second = this % 60
        val strSecond = if (second < 10) "0$second" else second.toString()

        "$strMinute:$strSecond"
    }.getOrElse { "" }
