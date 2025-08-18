package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes

/**
 * This DefaultDialog composable function allows you to create a customizable dialog with the following parameters:
 *
 * @param modifier A modifier for styling the dialog.
 * @param icon An optional icon (Painter) to display in the dialog.
 * @param iconSize An optional iconSize (Dp) to set size icon in the dialog.
 * @param title The title of the dialog.
 * @param message An optional message to display in the dialog (blank will be hide message).
 * @param dialogType The type of dialog ([DialogType]) (e.g., default, alert, full-screen).
 * @param positiveButtonText The text for the positive button.
 * @param onPositive A callback when the positive button is clicked.
 * @param negativeButtonText The text for the negative button (optional).
 * @param onNegative A callback when the negative button is clicked (optional).
 * @param onDismissRequest A callback when the dialog is dismissed (optional).
 *
 * @sample com.noblesoftware.portalcore.component.compose.ExampleDefaultDialog
 *
 * @author VPN Android Team
 * @since 2024
 **/

@Composable
fun DefaultDialog(
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    title: String,
    message: String? = null,
    messageAnnotated: AnnotatedString? = null,
    dialogType: DialogType = DialogType.Default,
    positiveButtonText: String,
    negativeButtonText: String = stringResource(id = R.string.empty_string),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    iconSize: Dp = dimensionResource(id = if (dialogType == DialogType.Default || dialogType == DialogType.Alert) R.dimen.default_dialog_icon_size else R.dimen.default_dialog_icon_size_small),
    onPositive: () -> Unit,
    onNegative: () -> Unit? = {},
    onDismissRequest: () -> Unit? = {}
) {

    if (dialogType == DialogType.Default || dialogType == DialogType.Alert) {
        Dialog(
            onDismissRequest = { onDismissRequest.invoke() },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = true,
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = dismissOnClickOutside,
            ),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalDimen.current.regular)
                    .shadow(LocalDimen.current.small),
                shape = LocalShapes.medium,
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.background_body))
            ) {
                DialogContent(
                    modifier = modifier,
                    icon = icon,
                    iconSize = iconSize,
                    title = title,
                    message = message,
                    messageAnnotated = messageAnnotated,
                    positiveButtonText = positiveButtonText,
                    dialogType = dialogType,
                    onPositive = onPositive,
                    negativeButtonText = negativeButtonText,
                    onNegative = onNegative
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
                .background(color = colorResource(id = R.color.background_body)),
            verticalArrangement = Arrangement.Center
        ) {
            DialogContent(
                modifier = modifier,
                icon = icon,
                iconSize = iconSize,
                title = title,
                message = message,
                messageAnnotated = messageAnnotated,
                positiveButtonText = positiveButtonText,
                dialogType = dialogType,
                onPositive = onPositive,
                negativeButtonText = negativeButtonText,
                onNegative = onNegative
            )
        }
    }
}

@Composable
private fun DialogContent(
    modifier: Modifier,
    icon: Painter?,
    iconSize: Dp,
    title: String,
    message: String?,
    messageAnnotated: AnnotatedString?,
    positiveButtonText: String,
    dialogType: DialogType,
    onPositive: () -> Unit,
    negativeButtonText: String,
    onNegative: () -> Unit?
) {
    Column(
        modifier = modifier
            .padding(LocalDimen.current.regular),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(vertical = LocalDimen.current.regular)
        ) {
            icon?.let {
                Image(
                    modifier = Modifier
                        .size(
                            size = iconSize,
                        ),
                    painter = icon,
                    contentDescription = "dialog-icon"
                )
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 18.sp,
                color = colorResource(id = R.color.text_primary)
            ),
            textAlign = TextAlign.Center
        )
        message?.let {
            DefaultSpacer(height = LocalDimen.current.default)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = if (dialogType == DialogType.Default || dialogType == DialogType.Alert) 0.dp else 12.dp
                    ),
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(colorResource(id = R.color.text_primary)),
                textAlign = TextAlign.Center
            )
        }
        messageAnnotated?.let {
            DefaultSpacer(height = LocalDimen.current.default)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = if (dialogType == DialogType.Default || dialogType == DialogType.Alert) 0.dp else 12.dp
                    ),
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(colorResource(id = R.color.text_primary)),
                textAlign = TextAlign.Center
            )
        }
        DefaultSpacer(height = LocalDimen.current.large)
        DefaultButton(
            modifier = Modifier.fillMaxWidth(),
            text = positiveButtonText,
            buttonVariant = if (dialogType == DialogType.Alert || dialogType == DialogType.FullScreenAlert) ButtonVariant.Danger else ButtonVariant.Primary,
            buttonSize = if (dialogType == DialogType.Default || dialogType == DialogType.Alert) ButtonSize.Medium else ButtonSize.Large,
            onClick = onPositive::invoke
        )
        if (stringResource(id = R.string.empty_string) != negativeButtonText) {
            DefaultSpacer(height = LocalDimen.current.default)
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = negativeButtonText,
                buttonVariant = ButtonVariant.Neutral,
                buttonSize = if (dialogType == DialogType.Default || dialogType == DialogType.Alert) ButtonSize.Medium else ButtonSize.Large,
                buttonType = ButtonType.Outlined,
                onClick = onNegative::invoke
            )
        }
    }
}

@JvmInline
value class DialogType internal constructor(@Suppress("unused") private val value: Int) {


    override fun toString(): String {
        return when (this) {
            Default -> "Default"
            Alert -> "Alert"
            FullScreen -> "FullScreen"
            FullScreenAlert -> "FullScreenAlert"
            else -> "Invalid"
        }
    }

    companion object {
        @Stable
        val Default: DialogType = DialogType(0)

        @Stable
        val Alert: DialogType = DialogType(1)

        @Stable
        val FullScreen: DialogType = DialogType(2)

        @Stable
        val FullScreenAlert: DialogType = DialogType(3)
    }
}

@Composable
private fun ExampleDefaultDialog() {
    DefaultDialog(
        icon = painterResource(id = R.drawable.img_shield),
        title = "title example",
        message = "message example",
        positiveButtonText = "oke",
        onPositive = { /* positive callback */ },
        onDismissRequest = {/* negative callback */ },
    )
}