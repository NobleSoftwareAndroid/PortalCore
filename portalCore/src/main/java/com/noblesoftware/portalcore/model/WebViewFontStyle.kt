package com.noblesoftware.portalcore.model

import androidx.annotation.ColorRes
import androidx.annotation.FontRes

data class WebViewFontStyle(
    val size: Int = 14,
    val bold: Boolean = false,
    @ColorRes val color: Int = com.noblesoftware.portalcore.R.color.text_primary,
    @FontRes val fontFamily: Int = com.noblesoftware.portalcore.R.font.interregular,
)