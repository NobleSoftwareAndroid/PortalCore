package com.noblesoftware.portalcore.component.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes
import com.noblesoftware.portalcore.util.extension.isFalse

/**
 * The DefaultTextInput composable is used to create a text input field with customizable properties. It allows users to enter and modify text.
 *
 * Parameters
 * @param modifier A modifier that can be used to customize the appearance of the text input.
 * @param value The current value of the text input (controlled by the parent component).
 * @param label An optional label to display above the input field.
 * @param placeholder An optional placeholder text to show when the input field is empty.
 * @param required A boolean indicating whether the input is required (e.g., for form validation).
 * @param background The background color of the input field.
 * @param shape The shape (e.g., rounded corners) of the input field.
 * @param inputType [KeyboardType] The type of keyboard input (e.g., text, number, email).
 * @param readOnly A boolean indicating whether the input field is read-only.
 * @param singleLine A boolean indicating whether the input should be a single-line field.
 * @param maxLength The maximum allowed length of the input.
 * @param maxLines The maximum number of visible lines (if singleLine is false).
 * @param minLines The minimum number of visible lines (if singleLine is false).
 * @param imeAction [ImeAction] The action to perform when the user presses the IME (soft keyboard) action button.
 * @param keyboardActions Custom keyboard actions (e.g., handling “Done” or “Next” actions).
 * @param keyboardCapitalization The capitalization style of the input (e.g., none, sentences, words).
 * @param height The height of the input field.
 * @param leadingIcon An optional icon (e.g., search icon) to display within the left input field.
 * @param trailingIcon An optional icon (e.g., search icon) to display within the right input field.
 * @param errorText An optional error message to display below the input field.
 * @param helperText An optional helper text to provide additional context.
 * @param enabled A boolean indicating whether the input field is enabled (clickable).
 * @param onValueChange A lambda function to handle changes in the input value.
 * @param onFocusChange A lambda function to handle focus changes (e.g., when the input gains or loses focus).
 *
 * @sample com.noblesoftware.portalcore.component.compose.ExampleDefaultTextInput
 *
 * @author VPN Android Team
 * @since 2024
 */

@Composable
fun DefaultTextInputCurrency(
    modifier: Modifier = Modifier,
    value: String,
    label: String = stringResource(id = R.string.empty_string),
    placeholder: String = stringResource(id = R.string.empty_string),
    required: Boolean = false,
    background: Color = Color(0xFFFFFFFF),
    shape: Shape = LocalShapes.medium,
    inputType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLength: Int = Int.MAX_VALUE,
    maxLines: Int = 1,
    minLines: Int = 1,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions? = null,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    height: Dp = 50.dp,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    errorText: String = stringResource(id = R.string.empty_string),
    helperText: String = stringResource(id = R.string.empty_string),
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    val isFocused = remember { mutableStateOf(false) }
    val isInputError = remember { mutableStateOf(false) }
    isInputError.value = errorText != stringResource(id = R.string.empty_string)
    val textTransform = remember { mutableStateOf(inputType == KeyboardType.Password) }

    Column {
        if (label != stringResource(id = R.string.empty_string)) {
            Row(
                modifier = Modifier
                    .padding(
                        bottom = LocalDimen.current.default,
                        start = LocalDimen.current.extraSmall
                    )
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        colorResource(id = R.color.text_primary),
                    )
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
        BasicTextField(
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    onFocusChange.invoke(it.isFocused)
                    isFocused.value = it.isFocused
                },
            value = TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            ),
            enabled = enabled,
            singleLine = singleLine,
            maxLines = if (minLines > 1 && maxLines == 1) minLines else maxLines,
            minLines = minLines,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = if (isInputError.value.isFalse()) colorResource(id = R.color.text_primary) else colorResource(
                    id = R.color.danger_outlined_color
                )
            ),
            onValueChange = {
                if (it.text.length <= maxLength) {
                    isInputError.value = false
                    onValueChange(it.text)
                }
            },
            keyboardActions = keyboardActions ?: KeyboardActions(
                onDone = { focusManager.clearFocus() },
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                onSearch = { focusManager.clearFocus() }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = inputType,
                imeAction = imeAction,
                capitalization = keyboardCapitalization
            ),
            visualTransformation = if (textTransform.value.isFalse()) VisualTransformation.None else PasswordVisualTransformation(),
            cursorBrush = SolidColor(colorResource(id = R.color.text_primary)),
            readOnly = readOnly,
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .clip(shape)
                        .then(
                            if (minLines <= 1) {
                                Modifier.height(height)
                            } else {
                                Modifier
                            }
                        )
                        .background(
                            if (isFocused.value) background.copy(alpha = .10f) else background.copy(
                                alpha = .5f
                            )
                        )
                        .then(
                            if (isInputError.value.isFalse()) {
                                Modifier.border(
                                    width = 1.9.dp,
                                    color = if (isFocused.value) colorResource(id = R.color.primary_outlined_active_bg) else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            } else if (enabled.isFalse()) {
                                Modifier.border(
                                    width = 1.9.dp,
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            } else {
                                Modifier.border(
                                    width = 1.9.dp,
                                    color = if (isFocused.value) colorResource(id = R.color.danger_outlined_active_bg) else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        )
                ) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            .then(
                                if (isInputError.value.isFalse()) {
                                    Modifier.border(
                                        width = 1.1.dp,
                                        color = if (isFocused.value) colorResource(id = R.color.primary_solid_bg) else
                                            if (enabled) colorResource(id = R.color.neutral_outlined_border) else colorResource(
                                                id = R.color.neutral_outlined_disabled_border
                                            ),
                                        shape = LocalShapes.medium
                                    )
                                } else if (enabled.isFalse()) {
                                    Modifier.border(
                                        width = 1.1.dp,
                                        color = colorResource(id = R.color.neutral_outlined_disabled_border),
                                        shape = LocalShapes.medium
                                    )
                                } else {
                                    Modifier.border(
                                        width = 1.1.dp,
                                        color = if (isFocused.value) colorResource(id = R.color.danger_outlined_color) else colorResource(
                                            id = R.color.danger_outlined_active_bg
                                        ),
                                        shape = LocalShapes.medium
                                    )
                                }
                            )
                            .shadow(elevation = .8.dp, shape = LocalShapes.medium, clip = true)
                            .background(
                                if (enabled) colorResource(id = R.color.background_body) else colorResource(
                                    id = R.color.primary_soft_disabled_bg
                                )
                            )
                    ) {
                        leadingIcon?.let {
                            leadingIcon()
                        }
                        Box(
                            Modifier
                                .weight(1f)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(
                                    start = if (leadingIcon == null) 15.dp else 0.dp,
                                    end = if (trailingIcon == null && minLines > 1) 15.dp else 0.dp,
                                    top = if (minLines > 1) LocalDimen.current.regular else LocalDimen.current.zero,
                                    bottom = if (minLines > 1) LocalDimen.current.regular else LocalDimen.current.zero,
                                )
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    modifier = Modifier,
                                    text = placeholder,
                                    color = colorResource(id = R.color.text_tertiary),
                                    fontSize = 14.sp,
                                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                    maxLines = if (singleLine) 1 else Int.MAX_VALUE,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            innerTextField()
                        }
                        trailingIcon?.let {
                            trailingIcon()
                        }
                        if (inputType == KeyboardType.Password) {
                            IconButton(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                onClick = {
                                    textTransform.value = !textTransform.value
                                }
                            ) {
                                AnimatedVisibility(
                                    visible = textTransform.value,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_eye_on),
                                        contentDescription = "Show Password",
                                        tint = if (value.isNotEmpty() || isFocused.value) colorResource(
                                            id = R.color.text_secondary
                                        ) else colorResource(id = R.color.neutral_plain_disabled_color)
                                    )
                                }

                                AnimatedVisibility(
                                    visible = !textTransform.value,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_eye_off),
                                        contentDescription = "Hide Password",
                                        tint = if (value.isNotEmpty() || isFocused.value) colorResource(
                                            id = R.color.text_secondary
                                        ) else colorResource(id = R.color.neutral_plain_disabled_color)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
        AnimatedVisibility(
            visible = isInputError.value,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(alignment = Alignment.CenterHorizontally)
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
                    style = MaterialTheme.typography.bodySmall.copy(
                        colorResource(id = R.color.text_primary),
                    )
                )
            }
        }
        AnimatedVisibility(
            visible = helperText != "",
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .padding(),
                    painter = painterResource(id = R.drawable.ic_info_outline),
                    contentDescription = "info-icon",
                    tint = colorResource(id = R.color.neutral_outlined_active_bg)
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp, top = 1.dp),
                    text = helperText,
                    color = colorResource(id = R.color.text_secondary),
                    style = MaterialTheme.typography.bodySmall.copy(
                        colorResource(id = R.color.text_primary),
                    )
                )
            }
        }

    }
}


@Composable
private fun ExampleDefaultTextInput(text: MutableState<String>) {
    DefaultTextInput(
        label = "Email",
        placeholder = "Please input email",
        required = true,
        inputType = KeyboardType.Email,
        value = text.value,
        onValueChange = { text.value = it })
}