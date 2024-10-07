package com.noblesoftware.portalcore.model

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
)