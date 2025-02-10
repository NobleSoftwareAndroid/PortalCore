package com.noblesoftware.portalcore.component.compose

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes
import com.noblesoftware.portalcore.util.extension.dashedBorder

/**
 * Created by Hafizh Anbiya on 29 May 2024
 * https://github.com/Fizhu
 */
@Composable
fun DefaultDocument(
    label: String = stringResource(id = R.string.empty_string),
    required: Boolean = false,
    value: String?,
    error: Boolean = false,
    readOnly: Boolean = false,
    errorText: String = stringResource(id = R.string.empty_string),
    placeholder: String = stringResource(R.string.tap_to_upload),
    subPlaceholder: String = stringResource(R.string.pdf_docx_jpg_jpeg_png_maximum_size_5_mb),
    labelStyle: TextStyle = MaterialTheme.typography.labelSmall,
    placeholderStyle: TextStyle = MaterialTheme.typography.titleSmall,
    subPlaceholderStyle: TextStyle = MaterialTheme.typography.bodySmall,
    @DrawableRes iconDocument: Int = R.drawable.ic_paperclip,
    @DrawableRes iconDocumentEdit: Int = R.drawable.ic_file,
    onClick: () -> Unit,
    onClose: () -> Unit
) {
    val localDimen = LocalDimen.current
    val outlineColor =
        colorResource(id = if (error) R.color.danger_solid_bg else R.color.primary_solid_bg)
    if (label.isNotBlank()) {
        Row(
            modifier = Modifier.padding(
                bottom = LocalDimen.current.default, start = LocalDimen.current.extraSmall
            )
        ) {
            Text(
                text = label,
                style = labelStyle.copy(colorResource(id = R.color.text_primary))
            )
            if (required) {
                Text(
                    modifier = Modifier.padding(start = LocalDimen.current.extraSmall),
                    text = "*",
                    style = MaterialTheme.typography.labelSmall.copy(color = colorResource(id = R.color.danger_outlined_color))
                )
            }
        }
    }
    if (readOnly) {
        DefaultFileButton(text = value.orEmpty(), icon = iconDocument) {
            onClick.invoke()
        }
    } else {
        if (!value.isNullOrEmpty()) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalShapes.medium)
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.neutral_solid_disabled_bg),
                        shape = LocalShapes.medium
                    )
            ) {
                val (image, title, close) = createRefs()
                Image(
                    modifier = Modifier
                        .constrainAs(image) {
                            start.linkTo(parent.start, margin = localDimen.regular)
                            top.linkTo(parent.top, margin = localDimen.regular)
                            bottom.linkTo(parent.bottom, margin = localDimen.regular)
                        }
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.primary_soft_bg))
                        .padding(LocalDimen.current.default),
                    painter = painterResource(id = iconDocumentEdit),
                    contentDescription = "file",
                    colorFilter = ColorFilter.tint(colorResource(id = R.color.primary_plain_color))
                )
                Text(
                    modifier = Modifier.constrainAs(title) {
                        start.linkTo(image.end, margin = localDimen.regular)
                        end.linkTo(close.start, margin = localDimen.regular)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    },
                    text = value,
                    style = MaterialTheme.typography.labelMedium.copy(color = colorResource(id = R.color.primary_solid_bg)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(modifier = Modifier.constrainAs(close) {
                    end.linkTo(parent.end, margin = localDimen.default)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }, onClick = { onClose.invoke() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close_circle),
                        contentDescription = "close",
                        tint = colorResource(id = R.color.primary_soft_disabled_color)
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalShapes.medium)
                    .background(colorResource(id = if (error) R.color.red_soft else R.color.primary_plain_active_bg))
                    .clickable { onClick.invoke() }
                    .dashedBorder(
                        strokeWidth = 2.dp,
                        color = outlineColor,
                        cornerRadiusDp = LocalDimen.current.default
                    )
                    .padding(LocalDimen.current.regular),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colorResource(id = if (error) R.color.danger_soft_bg else R.color.primary_soft_bg))
                        .padding(LocalDimen.current.default),
                    painter = painterResource(id = R.drawable.ic_upload),
                    contentDescription = "file",
                    tint = outlineColor
                )
                DefaultSpacer()
                Column(Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = placeholder,
                        style = placeholderStyle.copy(color = colorResource(id = if (error) R.color.danger_solid_bg else R.color.text_primary)),
                    )
                    DefaultSpacer(LocalDimen.current.small)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = subPlaceholder,
                        style = subPlaceholderStyle.copy(color = colorResource(id = if (error) R.color.danger_soft_active_bg else R.color.text_tertiary)),
                    )
                }

            }
            AnimatedVisibility(
                visible = error,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(),
                        painter = painterResource(id = R.drawable.ic_error_outline),
                        contentDescription = "error-icon",
                        tint = colorResource(id = R.color.danger_outlined_color)
                    )
                    Text(
                        modifier = Modifier.padding(start = 5.dp, top = 1.dp),
                        text = errorText,
                        color = colorResource(id = R.color.danger_outlined_color),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}