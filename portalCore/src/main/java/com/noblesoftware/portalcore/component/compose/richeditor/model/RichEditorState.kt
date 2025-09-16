package com.noblesoftware.portalcore.component.compose.richeditor.model

import com.noblesoftware.portalcore.model.SelectOption

data class RichEditorState(
    val fontSize: RichEditorFontSize = RichEditorFontSize.Normal,
    val fontAlign: RichEditorFontAlign = RichEditorFontAlign.Left,
)

val richEditorFontSize = listOf(
    SelectOption(
        id = RichEditorFontSize.Small.size,
        nameId = RichEditorFontSize.Small.nameId,
        extras = RichEditorFontSize.Small.type.name,
        isSelected = true
    ),
    SelectOption(
        id = RichEditorFontSize.Normal.size,
        nameId = RichEditorFontSize.Normal.nameId,
        extras = RichEditorFontSize.Normal.type.name,
        isSelected = false
    ),
    SelectOption(
        id = RichEditorFontSize.Large.size,
        nameId = RichEditorFontSize.Large.nameId,
        extras = RichEditorFontSize.Large.type.name,
        isSelected = false
    ),
)

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