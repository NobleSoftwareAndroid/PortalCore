package com.noblesoftware.portalcore.component.xml.options_bottom_sheet

import com.noblesoftware.portalcore.model.SelectOption

sealed class BottomSheetEvent {
    data class OnSearch(val keywords: String) : BottomSheetEvent()
    data class InitSelectOptions(
        val options: List<SelectOption>,
    ) : BottomSheetEvent()

    data class OnSelect(val option: SelectOption) : BottomSheetEvent()
    object ResetSelected : BottomSheetEvent()
}