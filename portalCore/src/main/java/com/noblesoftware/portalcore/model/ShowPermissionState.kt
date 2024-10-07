package com.noblesoftware.portalcore.model

import com.noblesoftware.portalcore.enums.PermissionType

data class ShowPermissionState(
    val isShow: Boolean,
    val isRationale: Boolean,
    val permissionType: PermissionType
)
