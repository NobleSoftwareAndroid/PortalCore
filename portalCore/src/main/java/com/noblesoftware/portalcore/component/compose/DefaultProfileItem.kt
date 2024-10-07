package com.noblesoftware.portalcore.component.compose

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen

@Composable
fun DefaultProfileItem(
    modifier: Modifier = Modifier,
    @DrawableRes prefixIcon: Int,
    @DrawableRes suffixIcon: Int = R.drawable.ic_chevron_right_outlined,
    @ColorRes prefixIconColor: Int = R.color.text_secondary,
    @ColorRes suffixIconColor: Int = R.color.text_icon,
    @StringRes text: Int,
    @ColorRes textColor: Int = R.color.text_primary,
    @ColorRes rippleColor: Int? = null,
    isSuffixIcon: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    color = if (rippleColor != null) colorResource(id = rippleColor) else Color.Unspecified
                )
            ) {
                onClick.invoke()
            }
            .padding(
                horizontal = LocalDimen.current.regular,
                vertical = LocalDimen.current.medium
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(end = LocalDimen.current.default),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = prefixIcon),
                tint = colorResource(prefixIconColor),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(LocalDimen.current.default))
            Text(
                text = stringResource(id = text),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(id = textColor)
            )
        }
        Icon(
            modifier = Modifier.alpha(if (isSuffixIcon) 1f else 0f),
            painter = painterResource(id = suffixIcon),
            tint = colorResource(suffixIconColor),
            contentDescription = ""
        )
    }
}