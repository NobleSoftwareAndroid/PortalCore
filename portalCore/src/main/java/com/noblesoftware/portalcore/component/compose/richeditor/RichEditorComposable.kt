package com.noblesoftware.portalcore.component.compose.richeditor

import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.compose.DefaultSelectionItem
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.component.compose.richeditor.component.DefaultFontBox
import com.noblesoftware.portalcore.component.compose.richeditor.model.RichEditorFontAlign
import com.noblesoftware.portalcore.component.compose.richeditor.model.RichEditorFontAlign.Companion.toRichEditorFontAlign
import com.noblesoftware.portalcore.component.compose.richeditor.model.RichEditorState
import com.noblesoftware.portalcore.component.compose.richeditor.model.richEditorFontAlign
import com.noblesoftware.portalcore.component.compose.richeditor.model.richEditorFontSize
import com.noblesoftware.portalcore.component.xml.dynamic_bottom_sheet.DefaultDynamicBottomSheetDialog
import com.noblesoftware.portalcore.component.xml.dynamic_bottom_sheet.DefaultDynamicBottomSheetDialog.Companion.dismiss
import com.noblesoftware.portalcore.libs.rich_editor.RichEditor
import com.noblesoftware.portalcore.model.SnackbarState
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.util.FileUtils
import com.noblesoftware.portalcore.util.extension.findActivity
import com.noblesoftware.portalcore.util.extension.htmlToString
import com.noblesoftware.portalcore.util.extension.isFalse
import com.noblesoftware.portalcore.util.extension.isTrue
import com.noblesoftware.portalcore.util.extension.loge
import com.noblesoftware.portalcore.util.extension.orZero
import com.noblesoftware.portalcore.util.extension.rememberKeyboardState
import okhttp3.MultipartBody


@Composable
fun RichEditorComposable(
    modifier: Modifier = Modifier,
    value: String = "",
    tag: String = DefaultDynamicBottomSheetDialog::class.java.simpleName,
    state: RichEditorState = RichEditorState(),
    placeholder: String = stringResource(R.string.type_answer_here),
    minEditorHeight: Int = 150,
    contentPadding: Int = 12,
    /** [imageFormName] MultipartBody image name */
    imageFormName: String,
    /** [maxImageSize] max file size in MB */
    maxImageSize: Int = 5,
    maxImageSizeError: String = stringResource(R.string.max_file_5mb),
    isImageEnabled: Boolean = true,
    onImageUpload: (MultipartBody.Part) -> Unit,
    onImageRetrieve: () -> String,
    onSnackbar: (SnackbarState) -> Unit,
    onTextPaste: ((String) -> Unit?) = {},
    onTextChanged: (String) -> Unit,
) {

    val context = LocalContext.current
    val activity = context.findActivity()
    val isKeyboardOpen = rememberKeyboardState()

    val richEditorState = remember {
        mutableStateOf(state)
    }

    val richEditor = remember {
        RichEditor(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            setPadding(contentPadding)
            setEditorHeight(minEditorHeight)
            isFocusable = true
            isLongClickable = false
            setOnLongClickListener { true }
            setEditorFontColor(
                ContextCompat.getColor(
                    context,
                    R.color.text_primary
                )
            )
            setFontTextSize(state.fontSize)
            setPlaceholder(placeholder)
            html = value
            setOnTextChangeListener { text ->
                onTextChanged.invoke(
                    if (text.htmlToString().isBlank()) "" else text
                )
            }
            setOnTextPasteListener { text ->
                onTextPaste.invoke(text)
            }
        }
    }


    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            runCatching {
                uri?.let {
                    val fileData = FileUtils.getFileData(it, context)

                    if (validMimes.any { validMime ->
                            fileData.mime?.lowercase()?.contains(validMime) == true
                        }) {
                        if (fileData.size.orZero() <= maxImageSize * 1024 * 1024) {
                            val multipartBody = FileUtils.getFileBodyPart(
                                context = context,
                                uri = it,
                                formName = imageFormName,
                                isFromGallery = true,
                                onError = {
                                    onSnackbar.invoke(
                                        SnackbarState(
                                            messageId = R.string.something_went_wrong,
                                            isSuccess = false
                                        )
                                    )
                                })
                            multipartBody?.let { file ->
                                onImageUpload.invoke(file)
                            }
                        } else {
                            onSnackbar.invoke(
                                SnackbarState(
                                    message = maxImageSizeError,
                                    isSuccess = false
                                )
                            )
                        }
                    } else {
                        onSnackbar.invoke(
                            SnackbarState(
                                messageId = R.string.file_not_supported,
                                isSuccess = false
                            )
                        )
                    }
                }
            }.getOrElse {
                onSnackbar.invoke(
                    SnackbarState(
                        messageId = R.string.something_went_wrong,
                        isSuccess = false
                    )
                )
            }
        }

    LaunchedEffect(onImageRetrieve.invoke()) {
        val image = onImageRetrieve.invoke()
        if (image.isNotBlank()) {
            richEditor.insertImageWithSize(image, "", "100%", "auto", false)
        }
    }

    Box(
        modifier = modifier
            .then(
                if (isKeyboardOpen.value.isTrue()) {
                    Modifier
                        .border(
                            width = 2.dp,
                            color = colorResource(id = R.color.primary_outlined_active_bg),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(2.dp)
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.primary_solid_bg),
                            shape = RoundedCornerShape(LocalDimen.current.default)
                        )
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = colorResource(id = R.color.divider),
                        shape = RoundedCornerShape(LocalDimen.current.default)
                    )
                }
            )
            .animateContentSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = LocalDimen.current.default,
                            vertical = LocalDimen.current.medium,
                        ),
                    horizontalArrangement = Arrangement.spacedBy(LocalDimen.current.default)
                ) {
                    /** Bold */
                    item {
                        DefaultFontBox(
                            onClick = {
                                checkFocusEditor(richEditor)
                                richEditor.setBold()
                            },
                        ) {
                            Text(
                                text = "B", style = MaterialTheme.typography.labelLarge.copy(
                                    colorResource(id = R.color.text_primary),
                                )
                            )
                        }
                    }
                    /** Italic */
                    item {
                        DefaultFontBox(
                            onClick = {
                                checkFocusEditor(richEditor)
                                richEditor.setItalic()
                            },
                        ) {
                            Text(
                                modifier = Modifier.offset(x = -(LocalDimen.current.extraSmall)),
                                text = "i",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    colorResource(id = R.color.text_primary),
                                    fontStyle = FontStyle.Italic,
                                ),
                            )
                        }
                    }
                    /** Font Size */
                    item {
                        DefaultFontBox(
                            onClick = {
                                checkFocusEditor(richEditor)
                                DefaultDynamicBottomSheetDialog.showDialog(
                                    fragmentManager = (activity as AppCompatActivity).supportFragmentManager,
                                    tag = tag
                                ) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(horizontal = LocalDimen.current.regular)
                                    ) {
                                        items(richEditorFontSize.size) { index ->
                                            val fontSize = richEditorFontSize[index]
                                            val isSelected =
                                                fontSize.id == richEditorState.value.fontSize

                                            DefaultSelectionItem(
                                                isSelected = isSelected,
                                                onClick = {
                                                    activity.dismiss(tag)
                                                    richEditor.setFontTextSize(fontSize.id.orZero())
                                                    richEditorState.value =
                                                        richEditorState.value.copy(fontSize = fontSize.id.orZero())
                                                },
                                            ) {
                                                Text(
                                                    text = fontSize.name,
                                                    style = MaterialTheme.typography.labelMedium.copy(
                                                        colorResource(id = if (isSelected) R.color.primary_plain_color else R.color.text_primary)
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = richEditorState.value.fontSize.toString(),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        colorResource(id = R.color.text_primary),
                                    )
                                )
                                DefaultSpacer(width = LocalDimen.current.medium)
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                                    tint = colorResource(id = R.color.text_primary),
                                    contentDescription = "",
                                )
                            }
                        }
                    }
                    /** Unordered List */
                    item {
                        DefaultFontBox(
                            onClick = {
                                checkFocusEditor(richEditor)
                                richEditor.setBullets()
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_unordered_list),
                                tint = colorResource(id = R.color.text_primary),
                                contentDescription = "",
                            )
                        }
                    }
                    /** Ordered List */
                    item {
                        DefaultFontBox(
                            onClick = {
                                checkFocusEditor(richEditor)
                                richEditor.setNumbers()
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_ordered_list),
                                tint = colorResource(id = R.color.text_primary),
                                contentDescription = "",
                            )
                        }
                    }
                    /** Font Align */
                    item {
                        DefaultFontBox(
                            onClick = {
                                checkFocusEditor(richEditor)
                                DefaultDynamicBottomSheetDialog.showDialog(
                                    fragmentManager = (activity as AppCompatActivity).supportFragmentManager,
                                    tag = tag
                                ) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(horizontal = LocalDimen.current.regular)
                                    ) {
                                        items(richEditorFontAlign.size) { index ->
                                            val fontAlign = richEditorFontAlign[index]
                                            val isSelected =
                                                fontAlign.extras.toRichEditorFontAlign() == richEditorState.value.fontAlign

                                            DefaultSelectionItem(
                                                isSelected = isSelected,
                                                onClick = {
                                                    activity.dismiss(tag)
                                                    richEditorState.value =
                                                        richEditorState.value.copy(fontAlign = fontAlign.extras.toRichEditorFontAlign())
                                                    when (fontAlign.extras.toRichEditorFontAlign()) {
                                                        is RichEditorFontAlign.Right -> richEditor.setAlignRight()
                                                        is RichEditorFontAlign.Left -> richEditor.setAlignLeft()
                                                        is RichEditorFontAlign.Center -> richEditor.setAlignCenter()
                                                    }
                                                },
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = fontAlign.extras.toRichEditorFontAlign().icon),
                                                    tint = colorResource(id = if (isSelected) R.color.primary_plain_color else R.color.text_primary),
                                                    contentDescription = "",
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = richEditorState.value.fontAlign.icon),
                                    tint = colorResource(id = R.color.text_primary),
                                    contentDescription = "",
                                )
                                DefaultSpacer(width = LocalDimen.current.medium)
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                                    tint = colorResource(id = R.color.text_primary),
                                    contentDescription = "",
                                )
                            }
                        }
                    }
                    /** Image */
                    item {
                        DefaultFontBox(
                            isEnabled = isImageEnabled,
                            onClick = {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_image_manager),
                                tint = colorResource(id = if (isImageEnabled) R.color.text_primary else R.color.neutral_solid_disabled_color),
                                contentDescription = "",
                            )
                        }
                    }
                }
            }
            HorizontalDivider(color = colorResource(id = R.color.divider))
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    richEditor
                },
            )
        }
    }

}

/** Forcing editor to focus,
 * this is use when user want to set style but the editor not focused yet */
fun checkFocusEditor(richEditor: RichEditor) {
    if (richEditor.isFocused.isFalse()) {
        richEditor.focusEditor()
    }
}

private val validMimes = listOf("jpeg", "jpg", "png")