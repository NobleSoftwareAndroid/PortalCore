package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes
import java.util.regex.Pattern

/**
 * A composable function that displays a default password strength indicator.
 *
 * This component takes a password string as input and displays a progress bar
 * with different colors depending on the password strength. It also shows text labels
 * indicating the password requirements (at least 8 characters, upper/lowercase letters,
 * and at least one number). Additionally, it provides a callback function to notify
 * about the password validity.
 *
 * @param modifier Additional modifiers to apply to the layout.
 * @param password The password string to check the strength for.
 * @param isValidListener A callback function that gets called when valid indicator changes
 * meets all requirements (all 3 progress bars are filled), and `false` otherwise.
 *
 * @author VPN Android Team
 * @since 2024
 *
 */

@Composable
fun DefaultPasswordStrengthIndicator(
    modifier: Modifier = Modifier, password: String, isValidListener: (ValidationState) -> Unit
) {
    val validCaseList = remember {
        mutableStateOf(listOf<PasswordValidCase>())
    }
    val indicatorColor = remember {
        mutableStateOf(R.color.primary_soft_disabled_bg)
    }

    LaunchedEffect(password) {
        val list = mutableListOf<PasswordValidCase>()
        if (password.isNotBlank()) {
            list.add(PasswordValidCase.PASSWORD_FILLED)
        } else {
            list.removeItem(PasswordValidCase.PASSWORD_FILLED)
        }
        if (password.validateCharCount()) {
            list.add(PasswordValidCase.CHAR_COUNT_VALID)
        } else {
            list.removeItem(PasswordValidCase.CHAR_COUNT_VALID)
        }
        if (password.validateUpperAndLowerCase()) {
            list.add(PasswordValidCase.UPPER_AND_LOWERCASE_VALID)
        } else {
            list.removeItem(PasswordValidCase.UPPER_AND_LOWERCASE_VALID)
        }
        if (password.validateNumber()) {
            list.add(PasswordValidCase.NUMBER_VALID)
        } else {
            list.removeItem(PasswordValidCase.NUMBER_VALID)
        }
        validCaseList.value = list
        isValidListener.invoke(
            when {
                password.isBlank() -> ValidationState.IDLE
                list.count() >= 4 -> ValidationState.VALID
                else -> ValidationState.INVALID
            }
        )
    }

    LaunchedEffect(validCaseList.value) {
        val count = validCaseList.value.count()
        indicatorColor.value = when (count) {
            1 -> R.color.danger_outlined_color
            2, 3 -> R.color.warning_outlined_color
            4 -> R.color.success_outlined_color
            else -> R.color.primary_soft_disabled_bg
        }
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            LinearProgressIndicator(
                progress = { 1f },
                modifier = Modifier.weight(1f),
                color = colorResource(id = if (validCaseList.value.isNotEmpty()) indicatorColor.value else R.color.primary_soft_disabled_bg)
            )
            DefaultSpacer(height = LocalDimen.current.zero, width = LocalDimen.current.default)
            LinearProgressIndicator(
                progress = { 1f },
                modifier = Modifier.weight(1f),
                color = colorResource(id = if (validCaseList.value.count() > 1) indicatorColor.value else R.color.primary_soft_disabled_bg)
            )
            DefaultSpacer(height = LocalDimen.current.zero, width = LocalDimen.current.default)
            LinearProgressIndicator(
                progress = { 1f },
                modifier = Modifier.weight(1f),
                color = colorResource(id = if (validCaseList.value.count() > 3) indicatorColor.value else R.color.primary_soft_disabled_bg)
            )
        }
        DefaultSpacer(height = LocalDimen.current.medium)
        Column(
            Modifier
                .fillMaxWidth()
                .clip(LocalShapes.medium)
                .background(colorResource(id = R.color.primary_soft_disabled_bg))
                .padding(LocalDimen.current.medium)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.your_password_must_have),
                style = MaterialTheme.typography.titleSmall.copy(
                    colorResource(id = R.color.text_primary)
                ),
            )
            DefaultSpacer(height = LocalDimen.current.small + 2.dp)
            LabelIndicator(
                indicatorState = when {
                    validCaseList.value.isEmpty() -> IndicatorState.IDLE
                    validCaseList.value.isValid(PasswordValidCase.CHAR_COUNT_VALID) -> IndicatorState.CORRECT
                    else -> IndicatorState.INCORRECT
                }, label = stringResource(R.string._8_or_more_characters)
            )
            DefaultSpacer(height = LocalDimen.current.small)
            LabelIndicator(
                indicatorState = when {
                    validCaseList.value.isEmpty() -> IndicatorState.IDLE
                    validCaseList.value.isValid(PasswordValidCase.UPPER_AND_LOWERCASE_VALID) -> IndicatorState.CORRECT
                    else -> IndicatorState.INCORRECT
                }, label = stringResource(R.string.upper_lowercase_letters)
            )
            DefaultSpacer(height = LocalDimen.current.small)
            LabelIndicator(
                indicatorState = when {
                    validCaseList.value.isEmpty() -> IndicatorState.IDLE
                    validCaseList.value.isValid(PasswordValidCase.NUMBER_VALID) -> IndicatorState.CORRECT
                    else -> IndicatorState.INCORRECT
                }, label = stringResource(R.string.at_least_one_number)
            )
        }
    }
}

@Composable
private fun LabelIndicator(indicatorState: IndicatorState, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (indicatorState != IndicatorState.IDLE) {
            Icon(
                modifier = Modifier.size(LocalDimen.current.regular),
                painter = painterResource(id = if (indicatorState == IndicatorState.CORRECT) R.drawable.ic_check else R.drawable.ic_close),
                contentDescription = "icon",
                tint = colorResource(id = if (indicatorState == IndicatorState.CORRECT) R.color.success_outlined_color else R.color.danger_outlined_color)
            )
            DefaultSpacer(width = LocalDimen.current.small)
        }
        val textColor = when (indicatorState) {
            IndicatorState.CORRECT -> R.color.success_outlined_color
            IndicatorState.INCORRECT -> R.color.danger_outlined_color
            else -> R.color.text_secondary
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                colorResource(id = textColor),
                textDecoration = if (indicatorState == IndicatorState.CORRECT) TextDecoration.LineThrough else null
            ),
        )
    }
}

enum class IndicatorState {
    IDLE, CORRECT, INCORRECT,
}

enum class ValidationState {
    IDLE, VALID, INVALID,
}

enum class PasswordValidCase {
    PASSWORD_FILLED, CHAR_COUNT_VALID, UPPER_AND_LOWERCASE_VALID, NUMBER_VALID,
}

private fun String.validateCharCount() =
    Pattern.compile("^(?=.*[a-zA-Z0-9]).{8,}\$").matcher(this).matches()

private fun String.validateUpperAndLowerCase() =
    Pattern.compile("^(?=.*[A-Z]).+\$").matcher(this)
        .matches() && Pattern.compile("^(?=.*[a-z]).+\$").matcher(this).matches()

private fun String.validateNumber() = Pattern.compile("^(?=.*\\d).+\$").matcher(this).matches()

private fun List<PasswordValidCase>.isValid(passwordValidCase: PasswordValidCase) =
    this.find { x -> x == passwordValidCase } != null

private fun MutableList<PasswordValidCase>.removeItem(passwordValidCase: PasswordValidCase) =
    this.find { x -> x == passwordValidCase }?.let { x ->
        this.remove(x)
    }