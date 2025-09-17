package com.noblesoftware.portalcore.model

import android.os.Parcelable
import androidx.annotation.ColorRes
import com.noblesoftware.portalcore.R
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class SnackbarState(
    val message: String? = null,
    val messageId: Int? = null,
    val isSuccess: Boolean = true,
    val type: @RawValue SnackbarType? = null,
) : Parcelable

sealed class SnackbarType(
    @ColorRes val containerColor: Int = R.color.primary_soft_bg,
    @ColorRes val contentColor: Int = R.color.primary_soft_color,
) {
    object Primary : SnackbarType(
        containerColor = R.color.primary_soft_bg,
        contentColor = R.color.primary_soft_color,
    )

    object Neutral : SnackbarType(
        containerColor = R.color.neutral_soft_bg,
        contentColor = R.color.neutral_soft_color,
    )

    object Danger : SnackbarType(
        containerColor = R.color.danger_soft_bg,
        contentColor = R.color.danger_soft_color,
    )

    object Success : SnackbarType(
        containerColor = R.color.success_soft_bg,
        contentColor = R.color.success_soft_color,
    )

    object Warning : SnackbarType(
        containerColor = R.color.warning_soft_bg,
        contentColor = R.color.warning_soft_color,
    )
}
