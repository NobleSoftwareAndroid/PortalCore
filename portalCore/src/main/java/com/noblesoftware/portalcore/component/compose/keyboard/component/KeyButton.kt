package com.noblesoftware.portalcore.component.compose.keyboard.component

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit,
    autoRepeat: Boolean = false,
    icon: @Composable (() -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val currentOnClick by rememberUpdatedState(onClick)
    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier
            .padding(horizontal = 2.dp, vertical = 4.dp)
            .height(40.dp)
            .focusProperties { canFocus = false }
            .pointerInput(autoRepeat) {
                awaitEachGesture {
                    try {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        down.consume()
                        isPressed = true
                        currentOnClick()

                        val job = if (autoRepeat) {
                            scope.launch {
                                delay(500)
                                while (true) {
                                    currentOnClick()
                                    delay(60)
                                }
                            }
                        } else null

                        try {
                            waitForUpOrCancellation()
                        } finally {
                            job?.cancel()
                        }
                    } finally {
                        isPressed = false
                    }
                }
            },
        shape = RoundedCornerShape(6.dp),
        color = if (isPressed) {
            if (backgroundColor == Color.White) Color(0xFFECEFF1) else backgroundColor.copy(alpha = 0.8f)
        } else {
            backgroundColor
        },
        shadowElevation = if (isPressed) 0.dp else 1.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                icon()
            } else {
                Text(
                    text = text,
                    color = contentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1
                )
            }
        }
    }
}
