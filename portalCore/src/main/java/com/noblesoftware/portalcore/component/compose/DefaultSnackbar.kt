package com.noblesoftware.portalcore.component.compose

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.model.SnackbarState
import com.noblesoftware.portalcore.model.SnackbarType
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes
import com.noblesoftware.portalcore.util.extension.formatErrorMessage
import com.noblesoftware.portalcore.util.extension.ifNullOrEmpty
import com.noblesoftware.portalcore.util.extension.orResourceStringEmpty

/**
 * The DefaultSnackbar composable is used to display a standard snackbar (brief message) with customizable styling. It’s commonly used to inform users about app processes or provide feedback.
 *
 * Parameters
 * @param modifier A modifier that can be used to customize the appearance of the snackbar.
 * @param data A SnackbarData ([SnackbarData]) object containing information about the snackbar content (e.g., title, message, action).
 * @param state A SnackbarState ([SnackbarState]) object that manages the snackbar’s visibility and behavior.
 *
 * @sample com.noblesoftware.portalcore.component.compose.ExampleSnackbar
 *
 * @author VPN Android Team
 * @since 2024
 */

@Composable
fun DefaultSnackbar(
    modifier: Modifier = Modifier,
    data: SnackbarData,
    state: SnackbarState,
) {
    val context = LocalContext.current
    val snackbarData = data.visuals.toDefaultSnackBarVisual(context, state)
    val actionLabel = snackbarData.actionLabel
    var contentColor = snackbarData.type?.contentColor ?: R.color.neutral_soft_color
    var containerColor = snackbarData.type?.containerColor ?: R.color.neutral_soft_bg

    if (snackbarData.type == null) {
        contentColor =
            if (snackbarData.isSuccess) R.color.success_soft_color else R.color.danger_soft_color
        containerColor =
            if (snackbarData.isSuccess) R.color.success_soft_bg else R.color.danger_soft_bg
    }

    val actionComposable: (@Composable () -> Unit)? = if (actionLabel != null) {
        @Composable {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = colorResource(id = contentColor)),
                onClick = { data.performAction() },
                content = {
                    Text(
                        text = actionLabel,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(id = contentColor)
                        )
                    )
                }
            )
        }
    } else {
        null
    }
    val dismissActionComposable: (@Composable () -> Unit)? =
        if (snackbarData.withDismissAction) {
            @Composable {
                IconButton(
                    onClick = { data.dismiss() },
                    content = {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = colorResource(id = contentColor)
                        )
                    }
                )
            }
        } else {
            null
        }
    Box(modifier = modifier.padding(LocalDimen.current.regular)) {
        Snackbar(
            modifier = Modifier
                .clip(LocalShapes.small)
                .shadow(2.dp),
            contentColor = colorResource(id = contentColor),
            containerColor = colorResource(id = containerColor),
            actionContentColor = colorResource(id = contentColor),
            action = actionComposable,
            dismissActionContentColor = colorResource(id = contentColor),
            dismissAction = dismissActionComposable,
            shape = LocalShapes.small,
        ) {
            BoxWithConstraints {
                val textMeasurer = rememberTextMeasurer()
                val measuredLayoutResult = textMeasurer.measure(
                    constraints = constraints,
                    text = snackbarData.message.formatErrorMessage(context)
                )
                Text(
                    modifier = Modifier.padding(bottom = if (measuredLayoutResult.lineCount > 1) LocalDimen.current.medium else LocalDimen.current.zero),
                    text = snackbarData.message.formatErrorMessage(context),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium, color = colorResource(id = contentColor)
                    )
                )
            }
        }
    }
}

data class DefaultSnackbarVisuals(
    override val message: String,
    val isSuccess: Boolean,
    val type: SnackbarType? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false
) : SnackbarVisuals

fun SnackbarVisuals.toDefaultSnackBarVisual(context: Context, state: SnackbarState) =
    DefaultSnackbarVisuals(
        message = state.message.ifNullOrEmpty { context.getString(state.messageId.orResourceStringEmpty()) },
        isSuccess = state.isSuccess,
        type = state.type,
        duration = this.duration,
        actionLabel = this.actionLabel,
        withDismissAction = this.withDismissAction,
    )

suspend fun SnackbarHostState.showDefaultSnackbar(
    context: Context,
    snackbar: SnackbarState,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short,
): SnackbarResult {
    return this.showSnackbar(
        DefaultSnackbarVisuals(
            message = snackbar.message.ifNullOrEmpty { context.getString(snackbar.messageId.orResourceStringEmpty()) },
            isSuccess = snackbar.isSuccess,
            type = snackbar.type,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration
        ),
    )
}