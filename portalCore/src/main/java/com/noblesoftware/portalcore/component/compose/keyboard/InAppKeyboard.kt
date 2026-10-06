package com.noblesoftware.portalcore.component.compose.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.component.compose.keyboard.component.KeyButton

@Composable
fun InAppKeyboard(
    state: KeyboardState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFECEFF1)
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(backgroundColor)
            .padding(horizontal = 2.dp, vertical = 1.dp)
            .navigationBarsPadding()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false).consume()
                }
            }
    ) {
        if (state.keyboardType == KeyboardType.ALPHABET) {
            AlphabetLayout(state, context)
        } else {
            SymbolLayout(state, context)
        }
    }
}

@Composable
private fun AlphabetLayout(state: KeyboardState, context: android.content.Context) {
    val rows: List<List<String>> = remember {
        listOf(
            listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
            listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
            listOf("Shift", "z", "x", "c", "v", "b", "n", "m", "Backspace"),
            listOf("?123", ",", "Space", ".", "Enter", "Hide")
        )
    }

    rows.forEachIndexed { index, row: List<String> ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (index == 1) Spacer(modifier = Modifier.weight(0.5f))

            row.forEach { key: String ->
                val weight = remember(key) {
                    when (key) {
                        "Space" -> if (index == 3) 4.2f else 5f
                        "Shift", "Backspace", "?123", "Hide" -> 1.5f
                        "Enter" -> if (index == 3) 1.2f else 1.5f
                        "," , "." -> 0.8f
                        else -> 1f
                    }
                }

                val icon: @Composable (() -> Unit)? = when (key) {
                    "Shift" -> {
                        {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector =
                                    Icons.Default.ArrowUpward,
                                contentDescription = "Shift",
                                tint = Color(0xFF263238)
                            )
                        }
                    }

                    "Backspace" -> {
                        {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector =
                                    Icons.AutoMirrored.Outlined.Backspace,
                                contentDescription = "Backspace",
                                tint = Color(0xFF263238)
                            )
                        }
                    }

                    "Enter" -> {
                        {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector =
                                    Icons.AutoMirrored.Filled.KeyboardReturn,
                                contentDescription = "Enter",
                                tint = Color(0xFF263238)
                            )
                        }
                    }

                    "Hide" -> {
                        {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Default.KeyboardHide,
                                contentDescription = "Hide",
                                tint = Color(0xFF263238)
                            )
                        }
                    }

                    else -> null
                }

                val keyText = if (key.length == 1 && state.isShiftPressed) key.uppercase() else key
                val isSpecialKey = remember(key) { listOf("Shift", "Backspace", "?123", "Enter", "Hide").contains(key) }

                KeyButton(
                    text = keyText,
                    modifier = Modifier.weight(weight),
                    onClick = {
                        when (key) {
                            "Shift" -> state.onKeyAction(KeyAction.Shift, context)
                            "Backspace" -> state.onKeyAction(KeyAction.Backspace, context)
                            "Space" -> state.onKeyAction(KeyAction.Space, context)
                            "Enter" -> state.onKeyAction(KeyAction.Enter, context)
                            "?123" -> state.onKeyAction(KeyAction.SwitchType, context)
                            "Hide" -> state.onKeyAction(KeyAction.Hide, context)
                            else -> {
                                val char = if (state.isShiftPressed) key.uppercase() else key
                                state.onKeyAction(KeyAction.Character(char), context)
                            }
                        }
                    },
                    autoRepeat = key == "Backspace",
                    backgroundColor = if (key == "Shift" && state.isShiftPressed)
                        Color(0xFFB0BEC5)
                    else if (isSpecialKey)
                        Color(0xFFCFD8DC)
                    else Color.White,
                    contentColor = Color(0xFF263238),
                    icon = icon
                )
            }
            if (index == 1) Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
private fun SymbolLayout(state: KeyboardState, context: android.content.Context) {
    val rows: List<List<String>> = remember {
        listOf(
            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
            listOf("+", "-", "*", "/", "=", "%", "(", ")", "_", "&"),
            listOf(",", ".", ":", ";", "'", "\"", "?", "!", "@", "#", "Backspace"),
            listOf("ABC", "Space", "Enter", "Hide")
        )
    }

    rows.forEachIndexed { index, row: List<String> ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            row.forEach { key: String ->
                val weight = remember(key, index) {
                    when (key) {
                        "Space" -> 5.5f
                        "Backspace", "ABC", "Enter", "Hide" -> 1.5f
                        else -> if (index == 2) 0.85f else 1f
                    }
                }

                val icon: @Composable (() -> Unit)? = when (key) {
                    "Backspace" -> {
                        {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector =
                                    Icons.AutoMirrored.Outlined.Backspace,
                                contentDescription = "Backspace",
                                tint = Color(0xFF263238)
                            )
                        }
                    }

                    "Enter" -> {
                        {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.AutoMirrored.Filled.KeyboardReturn,
                                contentDescription = "Enter",
                                tint = Color(0xFF263238)
                            )
                        }
                    }

                    "Hide" -> {
                        {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Default.KeyboardHide,
                                contentDescription = "Hide",
                                tint = Color(0xFF263238)
                            )
                        }
                    }

                    else -> null
                }

                val isSpecialKey = remember(key) { listOf("Backspace", "ABC", "Enter", "Hide").contains(key) }

                KeyButton(
                    text = key,
                    modifier = Modifier.weight(weight),
                    onClick = {
                        when (key) {
                            "Backspace" -> state.onKeyAction(KeyAction.Backspace, context)
                            "Space" -> state.onKeyAction(KeyAction.Space, context)
                            "Enter" -> state.onKeyAction(KeyAction.Enter, context)
                            "ABC" -> state.onKeyAction(KeyAction.SwitchType, context)
                            "Hide" -> state.onKeyAction(KeyAction.Hide, context)
                            else -> state.onKeyAction(KeyAction.Character(key), context)
                        }
                    },
                    autoRepeat = key == "Backspace",
                    backgroundColor = if (isSpecialKey)
                        Color(0xFFCFD8DC)
                    else Color.White,
                    contentColor = Color(0xFF263238),
                    icon = icon
                )
            }
        }
    }
}
