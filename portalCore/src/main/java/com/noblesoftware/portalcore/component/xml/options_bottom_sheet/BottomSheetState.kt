package com.noblesoftware.portalcore.component.xml.options_bottom_sheet

import com.noblesoftware.portalcore.model.SelectOption

data class BottomSheetState(
    val selectOptions: List<SelectOption> = listOf(),
    val filteredSelectOptions: List<SelectOption> = listOf(),
    val keywords: String = ""
)