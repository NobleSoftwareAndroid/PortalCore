package com.noblesoftware.portalcore.component.compose.richeditor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen

@Composable
fun DefaultFontBox(
    modifier: Modifier = Modifier,
    paddingHorizontal: Dp = LocalDimen.current.default,
    isEnabled: Boolean = true,
    isClicked: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val backgroundColor = if (isEnabled) {
        if (isClicked) colorResource(R.color.divider) else colorResource(R.color.background_body)
    } else {
        colorResource(R.color.neutral_solid_disabled_bg)
    }
    Box(
        modifier = modifier
            .widthIn(min = 40.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(LocalDimen.current.small))
            .then(
                if (isEnabled) {
                    Modifier.clickable(
                        onClick = onClick,
                    )
                } else {
                    Modifier
                }
            )
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.divider),
                shape = RoundedCornerShape(LocalDimen.current.small)
            )
            .padding(horizontal = paddingHorizontal),
    ) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            content.invoke()
        }
    }
}