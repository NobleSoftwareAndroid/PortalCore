package com.noblesoftware.portalcore.component.compose.richeditor.model

import com.noblesoftware.portalcore.model.SelectOption

data class RichEditorState(
    val fontSize: Int = 14,
    val fontAlign: RichEditorFontAlign = RichEditorFontAlign.Left,
)

val richEditorFontSize = (12..25).toList().map {
    SelectOption(
        id = it,
        name = it.toString(),
        isSelected = it == 14
    )
}

val richEditorFontAlign = listOf(
    SelectOption(
        id = 1,
        extras = RichEditorFontAlign.Left.type.name,
        isSelected = true
    ),
    SelectOption(
        id = 2,
        extras = RichEditorFontAlign.Center.type.name,
        isSelected = false
    ),
    SelectOption(
        id = 2,
        extras = RichEditorFontAlign.Right.type.name,
        isSelected = false
    ),
)