package com.noblesoftware.portalcore.component.compose

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes

/**
 * Created by Hafizh Anbiya on 22 May 2024
 * https://github.com/Fizhu
 */
@Composable
fun TextLabel(
    modifier: Modifier = Modifier,
    label: String,
    backgroundColor: Color = colorResource(id = R.color.primary_soft_bg),
    textColor: Color = colorResource(id = R.color.primary_soft_color),
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor,
        fontWeight = FontWeight.W500
    ),
    @DrawableRes startIcon: Int? = null,
    @ColorRes startIconTint: Int = R.color.text_icon
) {
    Row(
        modifier = modifier
            .clip(LocalShapes.medium.copy(all = CornerSize(LocalDimen.current.extraRegular)))
            .background(backgroundColor)
            .padding(
                horizontal = LocalDimen.current.default,
                vertical = LocalDimen.current.extraSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (startIcon != null) {
            Icon(
                painter = painterResource(startIcon),
                tint = colorResource(startIconTint),
                contentDescription = ""
            )
            DefaultSpacer(width = LocalDimen.current.default)
        }
        Text(
            text = label,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview
@Composable
fun PreviewTextLabel() {
    TextLabel(
        label = "My Label",
        backgroundColor = colorResource(id = R.color.primary_soft_color),
        textStyle = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.W500,
            color = colorResource(id = R.color.background_body),
        )
    )
}

