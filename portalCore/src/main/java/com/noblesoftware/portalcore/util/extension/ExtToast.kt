@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.noblesoftware.portalcore.util.extension

import android.content.Context
import android.widget.Toast

fun Context.toast(msg: String?) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

/**
 * Display the simple Toast message with the [Toast.LENGTH_SHORT] duration.
 *
 * @param message the message text resource.
 */
inline fun Context.toast(message: Int): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

/**
 * Display the simple Toast message with the [Toast.LENGTH_SHORT] duration.
 *
 * @param message the message text.
 */
inline fun Context.toast(message: CharSequence): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

/**
 * Display the simple Toast message with the [Toast.LENGTH_LONG] duration.
 *
 * @param message the message text resource.
 */
inline fun Context.longToast(message: Int): Toast = Toast
    .makeText(this, message, Toast.LENGTH_LONG)
    .apply {
        show()
    }

/**
 * Display the simple Toast message with the [Toast.LENGTH_LONG] duration.
 *
 * @param message the message text.
 */
inline fun Context.longToast(message: CharSequence): Toast = Toast
    .makeText(this, message, Toast.LENGTH_LONG)
    .apply {
        show()
    }
