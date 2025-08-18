package com.noblesoftware.portalcore.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.noblesoftware.portalcore.R

val Inter = FontFamily(
    Font(R.font.interregular, weight = FontWeight.Normal),
    Font(R.font.intermedium, weight = FontWeight.Medium),
    Font(R.font.intersemibold, weight = FontWeight.SemiBold),
    Font(R.font.interbold, weight = FontWeight.Bold),
    Font(R.font.interextrabold, weight = FontWeight.ExtraBold),
)
val Typography: Typography
    get() = Typography(
        bodySmall = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false,
            ),
        ),
        bodyMedium = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false,
            ),
        ),
        bodyLarge = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false,
            ),
        ),
        labelSmall = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            ),
        ),
        labelMedium = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false,
            ),
        ),
        labelLarge = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false,
            ),
        ),
        titleSmall = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false,
            ),
        ),
        titleMedium = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false,
            ),
        ),
        titleLarge = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false,
            ),
        ),
    )