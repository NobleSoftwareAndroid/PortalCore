package com.noblesoftware.portalcore.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.noblesoftware.portalcore.R

data class StatusModel(
    val label: String = "",
    @StringRes val labelId: Int = R.string.empty_string,
    @ColorRes val backgroundColor: Int = R.color.primary_solid_bg,
    @ColorRes val textColor: Int = R.color.background_body,
    val fontSize: TextUnit = 14.sp,
    @DrawableRes val startIcon: Int? = null,
    @ColorRes val startIconTint: Int = R.color.text_icon,
)
