package com.noblesoftware.portalcore.component.compose

import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R

@Composable
fun DefaultTextInputIcon(
    modifier: Modifier,
    icon: Painter,
    @ColorRes tint: Int = R.color.text_icon,
) {
    Icon(
        painter = icon,
        modifier = modifier
            .size(height = 50.dp, width = 48.dp)
            .padding(12.dp),
        contentDescription = null,
        tint = colorResource(id = tint)
    )
}