package com.noblesoftware.portalcore.theme

import android.app.Activity
import android.view.Window
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import com.noblesoftware.portalcore.R

@Composable
fun PortalCoreTheme(
    window: Window? = null,
    content: @Composable () -> Unit,
) {

    val view = LocalView.current
    val white = colorResource(id = R.color.background_body)

    if (!view.isInEditMode) {
        SideEffect {
            val mWindow = window ?: (view.context as Activity).window

            mWindow.statusBarColor = white.toArgb()
            mWindow.navigationBarColor = white.toArgb()
            WindowCompat.getInsetsController(mWindow, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(mWindow, view).isAppearanceLightNavigationBars = true
        }
    }

    CompositionLocalProvider(
        LocalDimen provides Dimen()
    ) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = colorResource(id = R.color.primary_solid_bg),
            ),
            typography = Typography,
            content = content
        )
    }
}