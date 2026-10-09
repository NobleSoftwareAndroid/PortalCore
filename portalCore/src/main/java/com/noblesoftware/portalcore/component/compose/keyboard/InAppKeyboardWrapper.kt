@file:Suppress("DEPRECATION")
package com.noblesoftware.portalcore.component.compose.keyboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import android.view.WindowManager
import kotlinx.coroutines.awaitCancellation
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InAppKeyboardWrapper(
    isEnabled: Boolean,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val keyboardState = remember { KeyboardState() }
    keyboardState.isEnabled = isEnabled
    keyboardState.context = context
    
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.asPaddingValues(density).calculateBottomPadding() > 0.dp

    LaunchedEffect(isImeVisible, isEnabled) {
        if (isEnabled && isImeVisible) {
            keyboardState.suppressSystemKeyboardAggressively(this, context)
        }
    }

    DisposableEffect(isEnabled) {
        val activity = keyboardState.context?.let { ctx ->
            var c = ctx
            while (c is android.content.ContextWrapper) {
                if (c is android.app.Activity) break
                c = c.baseContext
            }
            c as? android.app.Activity
        }
        
        val originalMode = activity?.window?.attributes?.softInputMode
        
        val focusChangeListener = android.view.ViewTreeObserver.OnGlobalFocusChangeListener { _, _ ->
            if (isEnabled) {
                keyboardState.suppressSystemKeyboardAggressively(scope, context)
            }
        }

        if (isEnabled) {
            activity?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
            )
            activity?.window?.decorView?.viewTreeObserver?.addOnGlobalFocusChangeListener(focusChangeListener)
        }
        
        onDispose {
            if (isEnabled && originalMode != null) {
                activity?.window?.setSoftInputMode(originalMode)
            }
            activity?.window?.decorView?.viewTreeObserver?.removeOnGlobalFocusChangeListener(focusChangeListener)
        }
    }

    val customTextInputService = remember(keyboardState) {
        InAppTextInputService(keyboardState, InAppPlatformTextInputService(keyboardState))
    }
    
    val dummySoftwareKeyboardController = remember(keyboardState, scope) {
        object : SoftwareKeyboardController {
            override fun show() {
                keyboardState.suppressSystemKeyboardAggressively(scope, context)
            }
            override fun hide() {
                keyboardState.isVisible = false
            }
        }
    }
    

    @Suppress("DEPRECATION")
    CompositionLocalProvider(
        LocalKeyboardState provides keyboardState,
        LocalTextInputService provides if (isEnabled) customTextInputService else LocalTextInputService.current,
        LocalSoftwareKeyboardController provides if (isEnabled) dummySoftwareKeyboardController else LocalSoftwareKeyboardController.current
    ) {
        BackHandler(enabled = isEnabled && keyboardState.isVisible) {
            keyboardState.isVisible = false
        }

        InterceptPlatformTextInput(interceptor = { request, next ->
            if (isEnabled) {
                awaitCancellation()
            } else {
                next.startInputMethod(request)
            }
        }) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .pointerInput(isEnabled, keyboardState.isVisible) {
                            if (isEnabled && keyboardState.isVisible) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent(PointerEventPass.Final)
                                        if (event.type == PointerEventType.Press) {
                                            val isAnyConsumed = event.changes.any { it.isConsumed }
                                            if (!isAnyConsumed) {
                                                keyboardState.isVisible = false
                                            }
                                        }
                                    }
                                }
                            }
                        }
                ) {
                    content()
                }

                AnimatedVisibility(
                    visible = keyboardState.isEnabled && keyboardState.isVisible,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    InAppKeyboard(state = keyboardState)
                }
            }
        }
    }
}
