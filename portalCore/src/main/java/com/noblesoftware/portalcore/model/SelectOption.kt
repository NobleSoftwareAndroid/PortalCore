package com.noblesoftware.portalcore.model

import androidx.annotation.DrawableRes

data class SelectOption(
    var id: Int? = 0,
    var isSelected: Boolean = false,
    var name: String = "",
    var nameId: Int? = null,
    var extras: String? = null,
    var extras2: String? = null,
    var booleanExtras: Boolean? = null,
    var booleanExtras2: Boolean? = null,
    var intExtras: Int? = null,
    var intExtras2: Int? = null,
    @DrawableRes val startIcon: Int? = null,
    @DrawableRes val endIcon: Int? = null,
    var enabled: Boolean = true,
)