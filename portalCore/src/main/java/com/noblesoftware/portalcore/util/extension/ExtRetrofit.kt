package com.noblesoftware.portalcore.util.extension

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun String?.toFormRequestBody() = this?.toRequestBody(MultipartBody.FORM)

/**
 * @return [MultipartBody.Part] from the file's real path or null if the path is null.
 */
fun String?.createFormDataFromPath(fieldName: String): MultipartBody.Part? {
    return this?.let { filePath ->
        val imageFile = File(filePath)

        val requestBody = imageFile.asRequestBody(filePath.toMediaTypeOrNull())

        MultipartBody.Part.createFormData(
            fieldName,
            imageFile.name,
            requestBody
        )
    }
}
