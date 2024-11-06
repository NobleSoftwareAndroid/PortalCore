package com.noblesoftware.portalcore.component.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen

/**
 * The DefaultButton composable is used to create a customizable button with standard styling. It provides options for setting the button’s appearance, size, icons, and behavior.

 * Parameters
 * @param modifier A modifier that can be used to customize the appearance of the button.
 * @param text The label text for the button.
 * @param buttonVariant An enum value ([ButtonVariant]) indicating the visual style of the button (e.g. Primary, Neutral, Danger, Success, Warning).
 * @param buttonType An optional enum value ([ButtonType]) indicating the type of button (e.g. Solid, Outlined, Plain, Soft). Defaults to solid.
 * @param buttonSize An optional enum value ([ButtonSize]) indicating the size of the button (e.g., Small, Medium, Large). Defaults to medium.
 * @param startIcon An optional drawable resource ID for an icon to display at the start of the button.
 * @param endIcon An optional drawable resource ID for an icon to display at the end of the button.
 * @param textAllCaps A boolean indicating whether the button text should be displayed in all capital letters. Defaults to false.
 * @param iconWithSpacer A boolean indicating whether to add spacing between the text and icons. Defaults to false.
 * @param enabled A boolean indicating whether the button is enabled (clickable). Defaults to true.
 * @param loading A boolean indicating whether the button is in a loading state (e.g., showing a progress indicator). Defaults to false.
 * @param shape An optional shape (e.g., rounded corners) for the button. Defaults to null (no specific shape).
 * @param onClick A lambda function to execute when the button is clicked.
 *
 * @sample ExampleDefaultButton
 *
 * @author VPN Android Team
 * @since 2024
 **/

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    text: String,
    buttonVariant: ButtonVariant,
    buttonType: ButtonType? = ButtonType.Solid,
    buttonSize: ButtonSize? = ButtonSize.Medium,
    @DrawableRes startIcon: Int? = null,
    @DrawableRes endIcon: Int? = null,
    textAllCaps: Boolean = false,
    iconWithSpacer: Boolean = false,
    enabled: Boolean = true,
    loading: Boolean = false,
    shape: Shape? = null,
    contentPadding: PaddingValues = PaddingValues(
        top = 0.dp,
        bottom = 0.dp
    ), // reset padding
    onClick: () -> Unit
) {
    val currentFocus = LocalFocusManager.current
    val textValue = if (textAllCaps) text.uppercase() else text
    val backgroundColor: Color =
        colorResource(id = buttonVariant.getColor(buttonType ?: ButtonType.Solid).backgroundColor)
    val contentColor: Color =
        colorResource(id = buttonVariant.getColor(buttonType ?: ButtonType.Solid).contentColor)
    val borderColor: Color =
        colorResource(id = buttonVariant.getColor(buttonType ?: ButtonType.Solid).borderColor)

    Button(
        modifier = modifier.height(
            dimensionResource(
                id = (buttonSize ?: ButtonSize.Medium).getButtonHeight()
            )
        ),
        enabled = enabled,
        shape = shape ?: (buttonSize ?: ButtonSize.Medium).getButtonRadius(),
        colors = ButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = if (buttonType == ButtonType.Plain) Color.Transparent else colorResource(
                id = R.color.primary_solid_disabled_bg
            ),
            disabledContentColor = colorResource(id = R.color.primary_solid_disabled_color)
        ),
        border = BorderStroke(
            1.dp,
            if (enabled) borderColor else if (buttonType == ButtonType.Plain) Color.Transparent else colorResource(
                id = R.color.primary_solid_disabled_bg
            )
        ),
        onClick = {
            currentFocus.clearFocus()
            onClick()
        },
        contentPadding = contentPadding
    ) {
        if (startIcon != null) {
            Icon(
                painter = painterResource(id = startIcon),
                contentDescription = text,
                tint = if (enabled) contentColor else colorResource(id = R.color.primary_solid_disabled_color)
            )
            if (iconWithSpacer) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            }
        }

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(LocalDimen.current.extraRegular),
                color = colorResource(id = R.color.primary_solid_bg),
                trackColor = colorResource(id = R.color.primary_plain_hover_bg),
            )
        } else {
            Text(
                text = textValue,
                style = MaterialTheme.typography.labelMedium,
                color = if (enabled) contentColor else colorResource(id = R.color.primary_solid_disabled_color),
                modifier = Modifier.padding(horizontal = LocalDimen.current.default),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        if (endIcon != null) {
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Icon(
                painter = painterResource(id = endIcon),
                contentDescription = textValue,
                tint = if (enabled) contentColor else colorResource(id = R.color.primary_solid_disabled_color)
            )
        }
    }
}

@Composable
fun ButtonSize.getButtonHeightInDp(): Dp {
    return when (this) {
        ButtonSize.Medium -> LocalDimen.current.buttonMedium
        ButtonSize.Small -> LocalDimen.current.buttonSmall
        ButtonSize.Large -> LocalDimen.current.buttonLarge
        else -> LocalDimen.current.buttonMedium
    }
}

@Composable
private fun ExampleDefaultButton() {
    DefaultButton(
        modifier = Modifier.fillMaxWidth(),
        text = "Login",
        buttonVariant = ButtonVariant.Success,
        buttonSize = ButtonSize.Medium,
        buttonType = ButtonType.Solid
    ) {

    }
}