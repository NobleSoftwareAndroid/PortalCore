package com.noblesoftware.portalcore.component.xml.duration_picker_bottom_sheet.model

data class DurationPickerBottomSheetState(
    val durationPickerFormat: DurationPickerFormat = DurationPickerFormat.H_M_S,
    val initialHour: Int = 0,
    val initialMinute: Int = 0,
    val initialSecond: Int = 0,
    val hourLimit: Int = 24,
    val minuteLimit: Int = 60,
    val secondLimit: Int = 60,
    val visibleItems: Int = 7
)

enum class DurationPickerFormat {
    H_M, H_M_S
}
