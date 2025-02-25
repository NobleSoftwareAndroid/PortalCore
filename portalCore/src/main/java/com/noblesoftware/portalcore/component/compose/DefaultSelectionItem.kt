package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen

@Composable
fun DefaultSelectionItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LocalDimen.current.small))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    color = colorResource(id = R.color.primary_plain_color),
                ),
            ) {
                onClick.invoke()
            }
            .background(
                colorResource(
                    id = if (isSelected) R.color.primary_plain_active_bg else R.color.background_body
                )
            )
            .padding(
                horizontal = LocalDimen.current.default,
                vertical = LocalDimen.current.regular
            )
    ) {
        content.invoke()
    }
}