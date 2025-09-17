package com.noblesoftware.portalcore.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class Dimen(
    val zero: Dp = 0.dp,
    val extraSmall: Dp = 2.dp,
    val small: Dp = 4.dp,
    val default: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val regular: Dp = 16.dp,
    val extraRegular: Dp = 24.dp,
    val large: Dp = 32.dp,
    val extraLarge: Dp = 64.dp,

    val buttonSmall: Dp = 32.dp,
    val buttonMedium: Dp = 40.dp,
    val buttonLarge: Dp = 48.dp,
)

val LocalDimen = compositionLocalOf { Dimen() }