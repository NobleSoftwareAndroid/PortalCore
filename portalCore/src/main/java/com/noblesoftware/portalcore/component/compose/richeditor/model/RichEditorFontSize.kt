package com.noblesoftware.portalcore.component.compose.richeditor.model

import androidx.annotation.StringRes
import com.noblesoftware.portalcore.R

sealed class RichEditorFontSize(
    val type: RichEditorFontSizeType,
    val size: Int,
    @StringRes val nameId: Int,
) {
    object Small : RichEditorFontSize(
        type = RichEditorFontSizeType.SMALL,
        size = 4,
        nameId = R.string.small
    )

    object Normal : RichEditorFontSize(
        type = RichEditorFontSizeType.NORMAL,
        size = 12,
        nameId = R.string.normal
    )

    object Large : RichEditorFontSize(
        type = RichEditorFontSizeType.LARGE,
        size = 20,
        nameId = R.string.large
    )

    companion object {
        fun String?.toRichEditorFontSize(): RichEditorFontSize = when (this) {
            Small.type.name -> Small
            Normal.type.name -> Normal
            else -> Large
        }
    }
}

enum class RichEditorFontSizeType {
    SMALL, NORMAL, LARGE
}

