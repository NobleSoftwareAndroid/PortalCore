package com.noblesoftware.portalcore.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.noblesoftware.portalcore.R

sealed class FieldType(
    val type: String,
    val formTitle: String,
    @StringRes val formTitleId: Int,
) {
    data class Single(
        val title: String = "",
        @StringRes val titleId: Int = R.string.empty_string,
        val value: String = "",
        @StringRes val valueId: Int = R.string.empty_string,
        @ColorRes val textColor: Int = R.color.text_secondary,
    ) : FieldType(
        type = SINGLE,
        formTitle = title,
        formTitleId = titleId,
    )

    data class Multiple(
        val title: String = "",
        @StringRes val titleId: Int = R.string.empty_string,
        val listValue: List<String> = emptyList(),
        val listValueId: List<Int> = emptyList(),
        @ColorRes val textColor: Int = R.color.text_secondary,
    ) : FieldType(
        type = MULTIPLE,
        formTitle = title,
        formTitleId = titleId,
    )

    data class SingleClickable<T>(
        val title: String = "",
        @StringRes val titleId: Int = R.string.empty_string,
        val value: String = "",
        @StringRes val valueId: Int = R.string.empty_string,
        @ColorRes val textColor: Int = R.color.text_secondary,
        val extras: T
    ) : FieldType(
        type = SINGLE_CLICKABLE,
        formTitle = title,
        formTitleId = titleId,
    )

    data class Status(
        val title: String = "",
        @StringRes val titleId: Int = R.string.empty_string,
        val statusModels: List<StatusModel>,
        val additionalText: String = "",
    ) : FieldType(
        type = STATUS,
        formTitle = title,
        formTitleId = titleId,
    )

    data class File(
        val title: String = "",
        @StringRes val titleId: Int = R.string.empty_string,
        val url: String,
        val fileName: String = "",
    ) : FieldType(
        type = FILE,
        formTitle = title,
        formTitleId = titleId,
    )

    data class MultipleAnswer(
        val title: String = "",
        @StringRes val titleId: Int = R.string.empty_string,
        val listValue: List<String>,
    ) : FieldType(
        type = MULTIPLE_ANSWER,
        formTitle = title,
        formTitleId = titleId,
    )

    companion object {
        const val SINGLE = "single"
        const val MULTIPLE = "multiple"
        const val SINGLE_CLICKABLE = "single_clickable"
        const val STATUS = "status"
        const val FILE = "file"
        const val MULTIPLE_ANSWER = "multiple_answer"
    }
}
