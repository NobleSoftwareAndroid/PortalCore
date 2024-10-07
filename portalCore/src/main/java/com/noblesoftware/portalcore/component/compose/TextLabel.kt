package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen

/**
 * Created by Hafizh Anbiya on 22 May 2024
 * https://github.com/Fizhu
 */
@Composable
fun TextLabel(
    modifier: Modifier = Modifier,
    label: String,
    backgroundColor: Color = colorResource(id = R.color.primary_soft_bg),
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(
        color = colorResource(id = R.color.primary_soft_color),
        fontWeight = FontWeight.W500
    )
) {
    Text(
        text = label,
        modifier = modifier
            .clip(RoundedCornerShape(LocalDimen.current.extraRegular))
            .background(backgroundColor)
            .padding(
                horizontal = LocalDimen.current.default,
                vertical = LocalDimen.current.extraSmall
            ),
        style = textStyle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
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

