package com.noblesoftware.portalcore.component.compose.richeditor.model

import androidx.annotation.DrawableRes
import com.noblesoftware.portalcore.R

sealed class RichEditorFontAlign(
    val type: RichEditorFontAlignType,
    @DrawableRes val icon: Int
) {
    object Left : RichEditorFontAlign(
        type = RichEditorFontAlignType.LEFT,
        icon = R.drawable.ic_align_left
    )

    object Center : RichEditorFontAlign(
        type = RichEditorFontAlignType.CENTER,
        icon = R.drawable.ic_align_center
    )

    object Right : RichEditorFontAlign(
        type = RichEditorFontAlignType.RIGHT,
        icon = R.drawable.ic_align_right
    )

    companion object {
        fun String?.toRichEditorFontAlign(): RichEditorFontAlign = when (this) {
            Right.type.name -> Right
            Center.type.name -> Center
            else -> Left
        }
    }
}

enum class RichEditorFontAlignType {
    LEFT, CENTER, RIGHT
}
