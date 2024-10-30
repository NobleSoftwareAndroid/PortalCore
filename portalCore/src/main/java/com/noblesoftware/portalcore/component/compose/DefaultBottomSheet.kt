package com.noblesoftware.portalcore.component.compose

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen

/**
 * The DefaultBottomSheet composable is used to create a customizable bottom sheet with standard styling. It provides options for displaying a title, message, and buttons (positive and negative).
 *
 * Parameters:
 * @param modifier A modifier that can be used to customize the appearance of the bottom sheet.
 * @param bottomSheetType An enum value ([BottomSheetType]) indicating the type of bottom sheet (DefaultDialog, AlertDialog).
 * @param sheetState The state of the bottom sheet, which allows you to control its visibility and behavior.
 * @param icon An optional Painter representing an icon to display in the bottom sheet.
 * @param title The title text for the bottom sheet.
 * @param message An optional message text to display below the title.
 * @param positiveButtonText The label for the positive button (e.g., “OK”).
 * @param onPositive A lambda function to execute when the positive button is clicked.
 * @param onNegative An optional lambda function to execute when the negative button is clicked.
 * @param negativeButtonText An optional label for the negative button (e.g., “Cancel”).
 * @param onDismissRequest A lambda function to execute when the bottom sheet is dismissed.
 * @param content An optional composable lambda that defines the content of the bottom sheet.
 *
 * @author VPN Android Team
 * @since 2024
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultBottomSheet(
    modifier: Modifier = Modifier,
    bottomSheetType: BottomSheetType = BottomSheetType.Content,
    sheetState: SheetState,
    icon: Painter? = null,
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties(),
    title: String = stringResource(id = R.string.empty_string),
    message: String = stringResource(id = R.string.empty_string),
    annotatedMessage: AnnotatedString? = null,
    positiveButtonText: String = stringResource(id = R.string.empty_string),
    onPositive: (() -> Unit)? = null,
    onNegative: (() -> Unit)? = null,
    negativeButtonText: String = stringResource(id = R.string.empty_string),
    onDismissRequest: () -> Unit,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {

    val topInsets = WindowInsets(top = rememberDimensionByName("status_bar_height"))
    val bottomInsets = WindowInsets(bottom = rememberDimensionByName("navigation_bar_height"))

    val dragHandleTopPadding = animateDpAsState(
        targetValue = if (sheetState.currentValue == SheetValue.Expanded && sheetState.targetValue == SheetValue.Expanded) LocalDimen.current.medium.plus(
            topInsets.asPaddingValues()
                .calculateTopPadding()
        ) else LocalDimen.current.medium, label = "Top Padding Sheet"
    )

    val roundedCornerShape = animateDpAsState(
        targetValue = if (sheetState.currentValue == SheetValue.Expanded && sheetState.targetValue == SheetValue.Expanded) 0.dp else LocalDimen.current.extraRegular,
        label = "Rounded Corner Sheet"
    )

    ModalBottomSheet(
        modifier = modifier.imePadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = roundedCornerShape.value,
            topEnd = roundedCornerShape.value
        ),
        containerColor = colorResource(id = R.color.background_body),
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        properties = properties,
        dragHandle = {
            Surface(
                modifier = modifier
                    .padding(
                        top = dragHandleTopPadding.value,
                        start = LocalDimen.current.medium,
                        end = LocalDimen.current.medium
                    ),
                color = colorResource(id = R.color.neutral_outlined_active_bg),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Box(Modifier.size(width = 56.dp, height = 4.dp))
            }
        }
    ) {
        when (bottomSheetType) {
            BottomSheetType.DefaultDialog, BottomSheetType.AlertDialog -> {
                Column(
                    modifier = Modifier
                        .padding(horizontal = LocalDimen.current.regular),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    icon?.let {
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally)
                                .padding(vertical = LocalDimen.current.regular)
                        ) {
                            Image(
                                modifier = Modifier.size(size = dimensionResource(id = R.dimen.default_bottom_sheet_dialog_icon_size)),
                                painter = icon,
                                contentDescription = stringResource(id = R.string.empty_string)
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 18.sp,
                            color = colorResource(id = R.color.text_primary),
                        ),
                        textAlign = TextAlign.Center
                    )
                    DefaultSpacer(height = LocalDimen.current.default)
                    if (annotatedMessage != null) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = annotatedMessage,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = colorResource(
                                    id = R.color.text_secondary
                                )
                            ),
                            textAlign = TextAlign.Center,
                            lineHeight = TextUnit(19f, TextUnitType.Sp)
                        )
                    } else {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = message,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                colorResource(id = R.color.text_primary),
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                    DefaultSpacer(height = LocalDimen.current.extraRegular)
                }
                HorizontalDivider(color = colorResource(id = R.color.neutral_outlined_border))
                Row(
                    modifier = Modifier
                        .padding(LocalDimen.current.regular)
                ) {
                    if (stringResource(id = R.string.empty_string) != negativeButtonText) {
                        onNegative?.let {
                            DefaultButton(
                                modifier = Modifier.weight(1f),
                                text = negativeButtonText,
                                buttonVariant = ButtonVariant.Neutral,
                                buttonSize = ButtonSize.Large,
                                buttonType = ButtonType.Outlined,
                                onClick = it::invoke
                            )
                        }
                        DefaultSpacer(width = LocalDimen.current.medium)
                    }
                    onPositive?.let {
                        DefaultButton(
                            modifier = Modifier.weight(1f),
                            text = positiveButtonText,
                            buttonVariant = if (bottomSheetType == BottomSheetType.AlertDialog) ButtonVariant.Danger else ButtonVariant.Primary,
                            buttonSize = ButtonSize.Large,
                            onClick = it::invoke
                        )
                    }
                }
            }

            else -> {
                content?.let {
                    it()
                }
            }
        }
        DefaultSpacer(height = bottomInsets.asPaddingValues().calculateBottomPadding())
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun rememberDimensionByName(name: String): Int {
    val resources = LocalContext.current.resources
    return remember {
        val id = resources.getIdentifier(name, "dimen", "android")
        if (id == 0) 0 else resources.getDimensionPixelSize(id)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultBottomSheetSessionExpired(
    sheetState: SheetState,
    onPositive: (() -> Unit),
    onDismissRequest: () -> Unit,
) {
    DefaultBottomSheet(
        sheetState = sheetState,
        bottomSheetType = BottomSheetType.DefaultDialog,
        icon = painterResource(id = R.drawable.img_robot),
        title = stringResource(R.string.session_expired),
        message = stringResource(R.string.session_expired_desc),
        positiveButtonText = stringResource(id = R.string.login),
        properties = ModalBottomSheetProperties(
            securePolicy = SecureFlagPolicy.Inherit,
            shouldDismissOnBackPress = false
        ),
        onPositive = onPositive::invoke,
        onDismissRequest = onDismissRequest::invoke
    )
}


@JvmInline
value class BottomSheetType internal constructor(@Suppress("unused") private val value: Int) {

    override fun toString(): String {
        return when (this) {
            DefaultDialog -> "Dialog"
            AlertDialog -> "Alert"
            Content -> "Content"
            else -> "Invalid"
        }
    }

    companion object {
        @Stable
        val DefaultDialog: BottomSheetType = BottomSheetType(0)

        @Stable
        val AlertDialog: BottomSheetType = BottomSheetType(1)

        @Stable
        val Content: BottomSheetType = BottomSheetType(2)
    }
}