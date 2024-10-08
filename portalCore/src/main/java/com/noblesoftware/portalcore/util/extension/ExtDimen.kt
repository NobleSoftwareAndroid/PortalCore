package com.noblesoftware.portalcore.util.extension

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


/**
 * Get dimension attributes value
 *
 * Example:
 * getAttrDimension(R.attr.actionBarSize)
 */
fun Context.getAttrDimension(@AttrRes attr: Int): Int {
    val dimenAttr = intArrayOf(attr)
    val typedValue = TypedValue()
    val typedArray = this.obtainStyledAttributes(typedValue.data, dimenAttr)
    val dimenInPixel = typedArray.getDimensionPixelSize(0, -1)
    typedArray.recycle()
    return dimenInPixel
}

/**
 * This method converts device specific pixels to density independent pixels.
 */
fun Int.pxToDp(context: Context): Int {
    return (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 */
fun Int.dpToPx(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()
}

/**
 * This method converts sp unit to equivalent pixels, depending on device density.
 */
fun Int.spToPx(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()
}

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }