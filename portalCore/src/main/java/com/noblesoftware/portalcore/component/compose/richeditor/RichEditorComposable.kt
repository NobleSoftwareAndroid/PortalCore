package com.noblesoftware.portalcore.component.compose.richeditor

import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.foundation.lazy.LazyItemScope
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
import com.noblesoftware.portalcore.component.compose.richeditor.model.RichEditorFontSize.Companion.toRichEditorFontSize
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
import com.noblesoftware.portalcore.util.extension.orZero
import com.noblesoftware.portalcore.util.extension.rememberKeyboardState
import okhttp3.MultipartBody


/**
 * A Composable function that provides a rich text editor with various formatting options.
 *
 * This editor allows users to format text with bold, italic, font size changes,
 * ordered/unordered lists, text alignment, and image insertion.
 * It also supports anti-cheat features like preventing paste and copy/cut operations.
 *
 * @param modifier Modifier for styling the Composable.
 * @param value The initial HTML content of the editor.
 * @param tag A unique tag for the [DefaultDynamicBottomSheetDialog] used for font size and alignment selection.
 * @param state The current state of the rich editor, including font size and alignment.
 * @param placeholder The placeholder text to display when the editor is empty.
 * @param minEditorHeight The minimum height of the editor in pixels.
 * @param contentPadding The padding around the content of the editor in pixels.
 * @param imageFormName The name to be used for the image file in the [MultipartBody.Part] when uploading.
 * @param maxImageSize The maximum allowed size for an uploaded image in megabytes (MB).
 * @param maxImageSizeError The error message to display when an image exceeds the maximum size.
 * @param notSupportImageError The error message to display when an unsupported image type is selected.
 * @param isImageEnabled A boolean indicating whether the image insertion feature is enabled.
 * @param isAntiCheatEnable A boolean indicating whether anti-cheat features (preventing paste and copy/cut) are enabled.
 * @param onImageUpload A lambda function that is invoked when an image is selected and ready for upload. It provides the [MultipartBody.Part] of the image.
 * @param onImageRetrieve A lambda function that is invoked to get the URL or path of an image that has been successfully uploaded and should be inserted into the editor.
 * @param onSnackbar A lambda function to display a snackbar message (e.g., for errors or success notifications).
 * @param onTextPaste A lambda function that is invoked when text is pasted into the editor. It receives the pasted text as a String.
 * @param onTextCopyOrCut A lambda function that is invoked when text is copied or cut from the editor. It receives the copied/cut text as a String.
 */
@Composable
fun RichEditorComposable(
    modifier: Modifier = Modifier,
    value: String = "",
    tag: String = DefaultDynamicBottomSheetDialog::class.java.simpleName,
    state: RichEditorState = RichEditorState(),
    placeholder: String = stringResource(R.string.type_answer_here),
    minEditorHeight: Int = 150,
    contentPadding: Int = 12,
    isCount: Boolean = false,
    maxLength: Int = Int.MAX_VALUE,
    simplify: Boolean = false,
    /**[simplify] if true it will only show B, I, and Bullets*/
    /** [imageFormName] MultipartBody image name */
    imageFormName: String,
    /** [maxImageSize] max file size in MB */
    maxImageSize: Int = 5,
    maxImageSizeError: String = stringResource(R.string.max_file_5mb),
    notSupportImageError: String = stringResource(R.string.file_not_supported),
    isImageEnabled: Boolean = true,
    isAntiCheatEnable: Boolean = false,
    onImageUpload: (MultipartBody.Part) -> Unit,
    onImageRetrieve: () -> String,
    onSnackbar: (SnackbarState) -> Unit,
    onTextPaste: ((String) -> Unit?) = {},
    onTextCopyOrCut: ((String) -> Unit?) = {},
    onTextChanged: (String) -> Unit,
    onHtmlRetrieve: (() -> String?)? = null,
    action: (@Composable LazyItemScope.() -> Unit)? = null
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
            setEditorFontSize(state.fontSize.size.plus(4))
            setPlaceholder(placeholder)
            html = value
            setOnTextChangeListener { text ->
                onTextChanged.invoke(
                    if (text.htmlToString().isBlank()) "" else text
                )
            }
            setPreventPaste(isAntiCheatEnable)
            setOnTextPasteListener { text ->
                onTextPaste.invoke(text)
            }
            setPreventCopyOrCut(isAntiCheatEnable)
            setOnTextCopyOrCut { text ->
                onTextCopyOrCut.invoke(text)
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
                                message = notSupportImageError,
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

    LaunchedEffect(onHtmlRetrieve?.invoke()) {
        val htmlValue = onHtmlRetrieve?.invoke()
        if (htmlValue.isNullOrBlank().isFalse()) {
            checkFocusEditor(richEditor)
            richEditor.insertHtmlValue(htmlValue)
        }
    }
    Column(Modifier.fillMaxWidth()) {
        // text editor
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
//            .animateContentSize(),
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
                        if (!simplify) {
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
                                                    val selectOption = richEditorFontSize[index]
                                                    val isSelected =
                                                        selectOption.id == richEditorState.value.fontSize.size

                                                    DefaultSelectionItem(
                                                        isSelected = isSelected,
                                                        onClick = {
                                                            activity.dismiss(tag)
                                                            richEditor.setFontTextSize(selectOption.id.orZero())
                                                            richEditorState.value =
                                                                richEditorState.value.copy(fontSize = selectOption.extras.toRichEditorFontSize())
                                                        },
                                                    ) {
                                                        Text(
                                                            text = stringResource(
                                                                selectOption.nameId
                                                                    ?: R.string.strip
                                                            ),
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
                                            text = stringResource(richEditorState.value.fontSize.nameId),
                                            style = MaterialTheme.typography.labelMedium.copy(
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
                        if (!simplify) {
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
                        action?.let {
                            item {
                                it()
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

        // text counter
        if (isCount) {
            DefaultSpacer(height = LocalDimen.current.small)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier.padding(end = 5.dp, top = 1.dp),
                    text = "${value.htmlToString(true).length}/$maxLength",
                    style = MaterialTheme.typography.bodySmall.copy(
                        colorResource(id = if (value.htmlToString(true).length > maxLength) R.color.danger_outlined_color else R.color.text_icon),
                    )
                )
            }
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