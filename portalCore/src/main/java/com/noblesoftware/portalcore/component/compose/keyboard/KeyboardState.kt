@file:Suppress("DEPRECATION")
package com.noblesoftware.portalcore.component.compose.keyboard

import android.view.KeyEvent
import android.view.KeyCharacterMap
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class KeyboardType {
    ALPHABET,
    SYMBOL
}

class KeyboardState {
    var isEnabled by mutableStateOf(false)
    var isVisible by mutableStateOf(false)
    private var _keyboardType = mutableStateOf(KeyboardType.ALPHABET)
    var keyboardType: KeyboardType
        get() = _keyboardType.value
        set(value) {
            _keyboardType.value = value
            updateAutoShift()
        }

    var isShiftPressed by mutableStateOf(false)

    // Current focused text field value
    private var _textFieldValue = mutableStateOf(TextFieldValue())
    var textFieldValue: TextFieldValue
        get() = _textFieldValue.value
        set(value) {
            _textFieldValue.value = value
            updateAutoShift()
        }
    
    // Callback to update the focused text field
    var onValueChange: ((TextFieldValue) -> Unit)? = null
    
    // To identify which field is focused
    private var _focusedFieldId = mutableStateOf<String?>(null)
    var focusedFieldId: String?
        get() = _focusedFieldId.value
        set(value) {
            _focusedFieldId.value = value
            if (value != null) {
                updateAutoShift()
            }
        }

    // Optional command callback for components that handle keys themselves (like RichEditor)
    var onKeyCommand: ((KeyAction) -> Boolean)? = null

    var context: android.content.Context? = null

    private val keyCharacterMap = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD)

    fun onKeyAction(action: KeyAction, context: android.content.Context? = null) {
        if (onKeyCommand?.invoke(action) == true) {
            if (action is KeyAction.Character) {
                val c = action.char
                isShiftPressed = (c == "." || c == "?" || c == "!")
            }
            return
        }

        val activity = findActivity(context ?: this.context)

        when (action) {
            is KeyAction.Character -> {
                if (activity != null) {
                    sendKeyEventsForString(activity, action.char)
                } else {
                    insertText(action.char)
                }
                
                val c = action.char
                isShiftPressed = (c == "." || c == "?" || c == "!")
            }
            KeyAction.Backspace -> {
                if (activity != null) {
                    sendKeyEvent(activity, KeyEvent.KEYCODE_DEL)
                } else {
                    performBackspace()
                }
            }
            KeyAction.Space -> {
                if (activity != null) {
                    sendKeyEvent(activity, KeyEvent.KEYCODE_SPACE)
                } else {
                    insertText(" ")
                }
            }
            KeyAction.Enter -> {
                if (activity != null) {
                    sendKeyEvent(activity, KeyEvent.KEYCODE_ENTER)
                } else {
                    insertText("\n")
                }
            }
            KeyAction.Shift -> {
                isShiftPressed = !isShiftPressed
            }
            KeyAction.SwitchType -> {
                keyboardType = if (keyboardType == KeyboardType.ALPHABET) KeyboardType.SYMBOL else KeyboardType.ALPHABET
            }
            KeyAction.Hide -> {
                isVisible = false
            }
        }
    }

    private fun findActivity(context: android.content.Context?): android.app.Activity? {
        var ctx = context
        while (ctx is android.content.ContextWrapper) {
            if (ctx is android.app.Activity) return ctx
            ctx = ctx.baseContext
        }
        return null
    }

    private fun sendKeyEvent(activity: android.app.Activity, keyCode: Int) {
        activity.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
        activity.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, keyCode))
    }

    private fun sendKeyEventsForString(activity: android.app.Activity, text: String) {
        val events = keyCharacterMap.getEvents(text.toCharArray())
        if (events != null) {
            for (event in events) {
                activity.dispatchKeyEvent(event)
            }
        } else {
            // Fallback for characters not in map
            insertText(text)
        }
    }

    private fun insertText(text: String) {
        val selection = textFieldValue.selection
        val currentText = textFieldValue.text
        
        val newText = currentText.substring(0, selection.start) + text + currentText.substring(selection.end)
        val newSelection = TextRange(selection.start + text.length)
        
        val newValue = textFieldValue.copy(text = newText, selection = newSelection)
        textFieldValue = newValue
        onValueChange?.invoke(newValue)
    }

    private fun updateAutoShift() {
        if (keyboardType != KeyboardType.ALPHABET) return

        val text = textFieldValue.text
        val selection = textFieldValue.selection

        // Only auto-shift if cursor is collapsed
        if (!selection.collapsed) {
            return
        }

        if (selection.start == 0) {
            isShiftPressed = true
            return
        }

        val prevText = text.substring(0, selection.start)
        val trimmedPrev = prevText.trimEnd()

        if (trimmedPrev.isEmpty()) {
            isShiftPressed = true
        } else {
            val lastChar = trimmedPrev.last()
            if (lastChar == '.' || lastChar == '?' || lastChar == '!') {
                isShiftPressed = true
            } else {
                isShiftPressed = false
            }
        }
    }

    private fun performBackspace() {
        val selection = textFieldValue.selection
        val currentText = textFieldValue.text
        
        if (selection.start != selection.end) {
            // Delete selection
            val newText = currentText.substring(0, selection.start) + currentText.substring(selection.end)
            val newSelection = TextRange(selection.start)
            val newValue = textFieldValue.copy(text = newText, selection = newSelection)
            textFieldValue = newValue
            onValueChange?.invoke(newValue)
        } else if (selection.start > 0) {
            // Delete one character
            val newText = currentText.substring(0, selection.start - 1) + currentText.substring(selection.start)
            val newSelection = TextRange(selection.start - 1)
            val newValue = textFieldValue.copy(text = newText, selection = newSelection)
            textFieldValue = newValue
            onValueChange?.invoke(newValue)
        }
    }

    fun suppressSystemKeyboard(context: android.content.Context? = null) {
        val currentContext = context ?: this.context
        val activity = findActivity(currentContext) ?: return
        val window = activity.window

        // Proactively set flags to prevent system IME interaction and layout shifts
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        )

        // Force hide using WindowInsetsController for immediate effect without blinking
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.ime())

        // Try to disable soft input on focus for the currently focused view
        val currentFocus = activity.currentFocus
        if (currentFocus != null) {
            if (currentFocus is android.widget.TextView) {
                currentFocus.showSoftInputOnFocus = false
            }
            if (currentFocus is ViewGroup) {
                disableSoftInputRecursively(currentFocus)
            }
            
            // Fallback for older APIs or specific View types
            val imm = activity.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
        
        val imm = activity.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    /**
     * More aggressive version to be used when standard suppression fails (e.g. for WebViews)
     */
    fun suppressSystemKeyboardAggressively(scope: kotlinx.coroutines.CoroutineScope, context: android.content.Context? = null) {
        scope.launch {
            repeat(3) {
                suppressSystemKeyboard(context)
                delay(100)
            }
        }
    }

    private fun disableSoftInputRecursively(view: android.view.View) {
        if (view is android.widget.TextView) {
            view.showSoftInputOnFocus = false
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                disableSoftInputRecursively(view.getChildAt(i))
            }
        }
    }
}

sealed class KeyAction {
    data class Character(val char: String) : KeyAction()
    object Backspace : KeyAction()
    object Enter : KeyAction()
    object Space : KeyAction()
    object Shift : KeyAction()
    object SwitchType : KeyAction()
    object Hide : KeyAction()
}

val LocalKeyboardState = compositionLocalOf { KeyboardState() }

@Composable
fun Modifier.connectToInAppKeyboard(
    id: String,
    value: String,
    onKeyCommand: ((KeyAction) -> Boolean)? = null,
    onValueChange: (String) -> Unit
): androidx.compose.ui.Modifier {
    val keyboardState = LocalKeyboardState.current
    val density = LocalDensity.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val isImeVisible = WindowInsets.ime.asPaddingValues(density).calculateBottomPadding() > 0.dp

    LaunchedEffect(isImeVisible, keyboardState.isEnabled) {
        if (keyboardState.isEnabled && isImeVisible) {
            keyboardState.suppressSystemKeyboardAggressively(scope)
        }
    }

    LaunchedEffect(value) {
        if (keyboardState.focusedFieldId == id && keyboardState.textFieldValue.text != value) {
            // Only update text, try to keep selection if possible
            val oldText = keyboardState.textFieldValue.text
            val oldSelection = keyboardState.textFieldValue.selection
            val newSelection = if (oldSelection.end == oldText.length) {
                TextRange(value.length)
            } else if (oldSelection.end <= value.length) {
                oldSelection
            } else {
                TextRange(value.length)
            }
            keyboardState.textFieldValue = TextFieldValue(
                text = value,
                selection = newSelection
            )
        }
    }

    return this
        .onFocusChanged { focusState ->
            if (keyboardState.isEnabled) {
                if (focusState.isFocused) {
                    keyboardState.focusedFieldId = id
                    // Initial value from component
                    if (keyboardState.textFieldValue.text != value) {
                        keyboardState.textFieldValue = TextFieldValue(
                            text = value,
                            selection = TextRange(value.length)
                        )
                    }
                    keyboardState.onValueChange = { newValue ->
                        onValueChange(newValue.text)
                    }
                    keyboardState.onKeyCommand = onKeyCommand
                    keyboardState.isVisible = true
                    keyboardState.suppressSystemKeyboardAggressively(scope)
                } else if (keyboardState.focusedFieldId == id) {
                    keyboardState.isVisible = false
                    keyboardState.focusedFieldId = null
                    keyboardState.onValueChange = null
                    keyboardState.onKeyCommand = null
                }
            }
        }
        .pointerInput(id, value) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    if (event.type == PointerEventType.Press && keyboardState.isEnabled) {
                        if (keyboardState.focusedFieldId != id) {
                            keyboardState.focusedFieldId = id
                            keyboardState.textFieldValue = TextFieldValue(
                                text = value,
                                selection = TextRange(value.length)
                            )
                            keyboardState.onValueChange = { newValue ->
                                onValueChange(newValue.text)
                            }
                            keyboardState.onKeyCommand = onKeyCommand
                        }
                        keyboardState.isVisible = true
                        keyboardState.suppressSystemKeyboardAggressively(scope)
                    }
                }
            }
        }
}

/**
 * Enhanced version that supports full TextFieldValue (including selection/cursor)
 */
@Composable
fun Modifier.connectToInAppKeyboard(
    id: String,
    value: TextFieldValue,
    onKeyCommand: ((KeyAction) -> Boolean)? = null,
    onValueChange: (TextFieldValue) -> Unit
): Modifier {
    val keyboardState = LocalKeyboardState.current
    val density = LocalDensity.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val isImeVisible = WindowInsets.ime.asPaddingValues(density).calculateBottomPadding() > 0.dp

    LaunchedEffect(isImeVisible, keyboardState.isEnabled) {
        if (keyboardState.isEnabled && isImeVisible) {
            keyboardState.suppressSystemKeyboardAggressively(scope)
        }
    }

    LaunchedEffect(value) {
        if (keyboardState.focusedFieldId == id) {
            keyboardState.textFieldValue = value
        }
    }

    return this
        .onFocusChanged { focusState ->
            if (keyboardState.isEnabled) {
                if (focusState.isFocused) {
                    keyboardState.focusedFieldId = id
                    keyboardState.textFieldValue = value
                    keyboardState.onValueChange = onValueChange
                    keyboardState.onKeyCommand = onKeyCommand
                    keyboardState.isVisible = true
                    keyboardState.suppressSystemKeyboardAggressively(scope)
                } else if (keyboardState.focusedFieldId == id) {
                    keyboardState.isVisible = false
                    keyboardState.focusedFieldId = null
                    keyboardState.onValueChange = null
                    keyboardState.onKeyCommand = null
                }
            }
        }
        .pointerInput(id, value) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    if (event.type == PointerEventType.Press && keyboardState.isEnabled) {
                        if (keyboardState.focusedFieldId != id) {
                            keyboardState.focusedFieldId = id
                            keyboardState.textFieldValue = value
                            keyboardState.onValueChange = onValueChange
                            keyboardState.onKeyCommand = onKeyCommand
                        }
                        keyboardState.isVisible = true
                        keyboardState.suppressSystemKeyboardAggressively(scope)
                    }
                }
            }
        }
}

/**
 * Internal service to capture state updates from Compose TextFields
 */
@Suppress("DEPRECATION")
class InAppTextInputService(
    keyboardState: KeyboardState,
    platformTextInputService: PlatformTextInputService
) : TextInputService(platformTextInputService)

@Suppress("DEPRECATION")
class InAppPlatformTextInputService(
    private val keyboardState: KeyboardState
) : PlatformTextInputService {
    override fun startInput(
        value: TextFieldValue,
        imeOptions: ImeOptions,
        onEditCommand: (List<EditCommand>) -> Unit,
        onImeActionPerformed: (ImeAction) -> Unit
    ) {
        if (keyboardState.focusedFieldId != null) {
            keyboardState.textFieldValue = value
        }
    }

    override fun stopInput() {}
    override fun showSoftwareKeyboard() {
        keyboardState.suppressSystemKeyboard()
    }
    override fun hideSoftwareKeyboard() {
        keyboardState.isVisible = false
    }
    
    override fun updateState(oldValue: TextFieldValue?, newValue: TextFieldValue) {
        if (keyboardState.focusedFieldId != null) {
            keyboardState.textFieldValue = newValue
        }
    }
}
