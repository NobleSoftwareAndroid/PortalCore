package com.noblesoftware.portalcore.component.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.core.content.ContextCompat
import com.noblesoftware.portalcore.R

/**
 * A composable function that displays expandable text with optional styling.
 *
 * @param modifier The modifier to apply to the composable.
 * @param text The text content to display.
 * @param style The [TextStyle] to use for the text. Defaults to [MaterialTheme.typography.bodyMedium].
 * @param spanStyle The [SpanStyle] to apply to specific spans within the text.
 * @param maxLines The maximum number of lines to display before truncating the text.
 *
 * @sample com.noblesoftware.portalcore.component.compose.ExampleExpandableText
 *
 * @author VPN Android Team
 * @since 2024
 */
@Composable
fun DefaultExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(colorResource(id = R.color.text_primary)),
    spanStyle: SpanStyle = SpanStyle(
        color = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.primary_plain_color
            )
        ),
    ),
    maxLines: Int
) {
    val finalText = remember {
        mutableStateOf(buildAnnotatedString {
            append(text)
        })
    }
    val textLayoutResult = remember {
        mutableStateOf<TextLayoutResult?>(null)
    }
    val isExpand = remember {
        mutableStateOf(false)
    }

    // This is to know if text already overflowing
    val isOverflow = remember {
        mutableStateOf(false)
    }

    val seeMore = stringResource(id = R.string.see_more)
    val seeLess = stringResource(id = R.string.see_less)

    // not observing isExpand.value because when text clicked, the ClickableText is recreate
    LaunchedEffect(textLayoutResult.value) {
        kotlin.runCatching {
            textLayoutResult.value?.let { textLayoutResult ->
                when {
                    !isExpand.value && textLayoutResult.hasVisualOverflow -> {
                        isOverflow.value = true
                        val lastCharIndex = textLayoutResult.getLineEnd(maxLines - 1)
                        val adjustedText =
                            text.substring(0, lastCharIndex).dropLast(seeMore.length)
                                .dropLastWhile {
                                    it == ' ' || it == '.'
                                }

                        finalText.value = buildAnnotatedString {
                            append(adjustedText)
                            withStyle(
                                spanStyle
                            ) {
                                append(seeMore)
                            }
                        }
                    }

                    isExpand.value -> {
                        finalText.value = buildAnnotatedString {
                            append(text)
                            withStyle(
                                spanStyle
                            ) {
                                append(seeLess)
                            }
                        }
                    }
                }
            }
        }
    }

    ClickableText(
        modifier = modifier.animateContentSize(),
        text = finalText.value,
        style = style,
        maxLines = if (!isExpand.value) maxLines else Int.MAX_VALUE,
        onTextLayout = {
            textLayoutResult.value = it
        },
    ) {
        if (isOverflow.value) {
            isExpand.value = !isExpand.value
        }
    }

}