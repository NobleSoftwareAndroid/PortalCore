package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.model.FieldType
import com.noblesoftware.portalcore.model.StatusModel
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.util.extension.isFalse
import com.noblesoftware.portalcore.util.extension.isNotStrip
import com.noblesoftware.portalcore.util.extension.isTrue

@Composable
fun FieldItem(
    modifier: Modifier = Modifier,
    fieldType: FieldType,
    onFileClick: (fileUrl: String, fileName: String) -> Unit = { _, _ -> },
    onIconClick: () -> Unit = {},
    onResendVerificationClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier
            .then(
                if (fieldType is FieldType.SingleClickable<*>) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple()
                    ) {
                        onClick.invoke()
                    }
                } else {
                    Modifier
                }
            )
            .padding(
                horizontal = LocalDimen.current.regular,
                vertical = LocalDimen.current.medium,
            ),
    ) {
        val (field, icon, btnResendVerification) = createRefs()
        Column(
            modifier = Modifier.constrainAs(field) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                if ((fieldType is FieldType.Single && fieldType.icon != null) || (fieldType is FieldType.Email && fieldType.icon != null)) {
                    end.linkTo(icon.start, margin = 12.dp)
                } else {
                    end.linkTo(parent.end)
                }
                width = Dimension.fillToConstraints
            }
        ) {
            Text(
                text = fieldType.formTitle.ifBlank {
                    stringResource(id = fieldType.formTitleId).ifBlank { "-" }
                },
                style = MaterialTheme.typography.labelMedium.copy(colorResource(id = R.color.text_primary))
            )
            // if is Single Answer
            if (fieldType is FieldType.Single) {
                DefaultSpacer(height = LocalDimen.current.default)
                Text(
                    text = fieldType.value.ifBlank {
                        stringResource(id = fieldType.valueId).ifBlank { "-" }
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(colorResource(id = fieldType.textColor))
                )
            }
            // if is Multiple
            if (fieldType is FieldType.Multiple) {
                DefaultSpacer(height = LocalDimen.current.default)
                if (fieldType.listValue.isEmpty() && fieldType.listValueId.isEmpty()) {
                    Text(
                        text = "-",
                        style = MaterialTheme.typography.bodyMedium.copy(colorResource(id = fieldType.textColor))
                    )
                }
                if (fieldType.listValue.isNotEmpty()) {
                    fieldType.listValue.onEachIndexed { index, value ->
                        Text(
                            text = value,
                            style = MaterialTheme.typography.bodyMedium.copy(colorResource(id = fieldType.textColor))
                        )
                        if (index < fieldType.listValue.size - 1) {
                            DefaultSpacer(height = LocalDimen.current.small)
                        }
                    }
                }
                if (fieldType.listValueId.isNotEmpty()) {
                    fieldType.listValueId.onEachIndexed { index, valueId ->
                        Text(
                            text = stringResource(id = valueId),
                            style = MaterialTheme.typography.bodyMedium.copy(colorResource(id = fieldType.textColor))
                        )
                        if (index < fieldType.listValueId.size - 1) {
                            DefaultSpacer(height = LocalDimen.current.small)
                        }
                    }
                }
            }
            // if is Single Clickable Answer
            if (fieldType is FieldType.SingleClickable<*>) {
                DefaultSpacer(height = LocalDimen.current.default)
                Text(
                    text = fieldType.value.ifBlank {
                        stringResource(id = fieldType.valueId).ifBlank { "-" }
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(colorResource(id = fieldType.textColor))
                )
            }
            // if is Status
            if (fieldType is FieldType.Status) {
                DefaultSpacer(height = LocalDimen.current.default)
                if (fieldType.statusModels.isNotEmpty()) {
                    fieldType.statusModels.forEachIndexed { index, statusModel ->
                        TextLabel(
                            label = statusModel.label.ifEmpty { stringResource(id = statusModel.labelId) },
                            backgroundColor = colorResource(id = statusModel.backgroundColor),
                            textStyle = MaterialTheme.typography.labelMedium.copy(
                                color = colorResource(id = statusModel.textColor),
                                fontWeight = FontWeight.W500,
                                fontSize = statusModel.fontSize
                            ),
                        )
                        if (index < fieldType.statusModels.size - 1) {
                            DefaultSpacer(LocalDimen.current.default)
                        }
                    }
                } else {
                    Text(
                        text = "-",
                        style = MaterialTheme.typography.bodyMedium.copy(colorResource(id = R.color.text_secondary))
                    )
                }
                if (fieldType.additionalText.isNotBlank()) {
                    DefaultSpacer(LocalDimen.current.default)
                    Text(
                        text = fieldType.additionalText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = colorResource(
                                id = R.color.text_secondary
                            )
                        )
                    )
                }
            }
            // if is File
            if (fieldType is FieldType.File) {
                DefaultSpacer(height = LocalDimen.current.default)
                if (fieldType.url.isBlank()) {
                    Text(
                        text = "-",
                        style = MaterialTheme.typography.bodyMedium.copy(colorResource(id = R.color.text_secondary))
                    )
                } else {
                    DefaultFileButtonSmall(text = fieldType.fileName) {
                        onFileClick.invoke(fieldType.url, fieldType.fileName)
                    }
                }
            }
            // if is Multiple Answer
            if (fieldType is FieldType.MultipleAnswer) {
                DefaultSpacer(height = LocalDimen.current.default)
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (fieldType.listValue.isEmpty()) {
                        Text(
                            text = "-",
                            style = MaterialTheme.typography.bodyMedium.copy(colorResource(id = R.color.text_secondary))
                        )
                    } else {
                        fieldType.listValue.onEachIndexed { index, value ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = LocalDimen.current.default)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.bullet),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        colorResource(
                                            id = R.color.text_secondary
                                        )
                                    )
                                )
                                DefaultSpacer(width = LocalDimen.current.default)
                                Text(
                                    text = value,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        colorResource(
                                            id = R.color.text_secondary
                                        )
                                    )
                                )
                            }
                            if (index < fieldType.listValue.size - 1) {
                                DefaultSpacer(height = LocalDimen.current.small)
                            }
                        }
                    }
                }
            }
            // if is Email
            if (fieldType is FieldType.Email) {
                DefaultSpacer(height = LocalDimen.current.default)
                Text(
                    text = fieldType.value.ifBlank {
                        stringResource(id = fieldType.valueId).ifBlank { "-" }
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(colorResource(id = fieldType.textColor))
                )
                if (fieldType.isVerified != null && fieldType.value.isNotBlank() && fieldType.value.isNotStrip()) {
                    val statusModel = if (fieldType.isVerified.isTrue())
                        StatusModel(
                            labelId = R.string.verified_email,
                            backgroundColor = R.color.success_soft_bg,
                            textColor = R.color.success_soft_color,
                            startIcon = R.drawable.ic_check,
                            startIconTint = R.color.success_soft_color,
                        )
                    else
                        StatusModel(
                            labelId = R.string.unverified_email,
                            backgroundColor = R.color.warning_soft_bg,
                            textColor = R.color.warning_soft_color,
                            startIcon = R.drawable.ic_mail,
                            startIconTint = R.color.warning_soft_color,
                        )

                    DefaultSpacer(height = LocalDimen.current.medium)
                    TextLabel(
                        label = stringResource(statusModel.labelId),
                        backgroundColor = colorResource(id = statusModel.backgroundColor),
                        textStyle = MaterialTheme.typography.labelMedium.copy(
                            color = colorResource(id = statusModel.textColor),
                            fontWeight = FontWeight.W500,
                        ),
                        startIcon = statusModel.startIcon,
                        startIconTint = statusModel.startIconTint
                    )
                }
            }
        }

        /** Add End Icon if its Single and has icon */
        if (fieldType is FieldType.Single && fieldType.icon != null) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides LocalDimen.current.zero) {
                IconButton(modifier = Modifier.constrainAs(icon) {
                    end.linkTo(parent.end)
                    top.linkTo(field.top)
                    bottom.linkTo(field.bottom)
                }, onClick = onIconClick) {
                    Icon(
                        painter = painterResource(fieldType.icon),
                        contentDescription = "",
                        tint = colorResource(id = fieldType.iconTint)
                    )
                }
            }
        }
        /** Add End Icon if its Email and has icon */
        if (fieldType is FieldType.Email && fieldType.icon != null) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides LocalDimen.current.zero) {
                IconButton(modifier = Modifier.constrainAs(icon) {
                    end.linkTo(parent.end)
                    top.linkTo(field.top)
                    bottom.linkTo(field.bottom)
                }, onClick = onIconClick) {
                    Icon(
                        painter = painterResource(fieldType.icon),
                        contentDescription = "",
                        tint = colorResource(id = fieldType.iconTint)
                    )
                }
            }
        }

        /** Add Resend Verification Button if its Unverified Email */
        if (fieldType is FieldType.Email && fieldType.isVerified.isFalse() && fieldType.value.isNotBlank() && fieldType.value.isNotStrip()) {
            DefaultButton(
                modifier = Modifier.constrainAs(btnResendVerification) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(field.bottom, margin = 12.dp)
                    width = Dimension.fillToConstraints
                },
                text = stringResource(R.string.resend_verification_email),
                buttonVariant = ButtonVariant.Primary,
                buttonSize = ButtonSize.Medium,
                buttonType = ButtonType.Outlined
            ) {
                onResendVerificationClick.invoke()
            }
        }
    }
}