package com.noblesoftware.portalcore.util.extension

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.noblesoftware.portalcore.R

/**
 * Created by Hafizh Anbiya on 22 August 2023
 * https://github.com/Fizhu
 */

enum class SnackBarType {
    NEUTRAL,
    ERROR,
    SUCCESS,
    TWO_ACTION
}

fun View.showSnackBar(
    message: String,
    isSuccess: Boolean = true,
    textDismissButton: String = this.context.getString(R.string.okay),
    dismissClicked: () -> Unit = {},
    isAnchor: Boolean = false
) {
    // Init
    val snackbar = Snackbar.make(this, "", Snackbar.LENGTH_SHORT)
    val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    val customSnackbar = View.inflate(this.context, R.layout.snackbar_action, null)
    val cardSnackbar = customSnackbar.findViewById<MaterialCardView>(R.id.card_snackbar)
    val tvTitle = customSnackbar.findViewById<AppCompatTextView>(R.id.tv_title)
    val btnOk = customSnackbar.findViewById<MaterialButton>(R.id.btn_ok)

    // Implementation
    if (isSuccess) {
        cardSnackbar.setCardBackgroundColor(this.context.getColorCompat(R.color.success_soft_bg))
        cardSnackbar.setStrokeColor(this.context.getColorStateList(R.color.success_soft_bg))
        tvTitle.setTextColor(this.context.getColorCompat(R.color.success_soft_color))
    } else {
        cardSnackbar.setCardBackgroundColor(this.context.getColorCompat(R.color.danger_soft_bg))
        cardSnackbar.setStrokeColor(this.context.getColorStateList(R.color.danger_soft_bg))
        tvTitle.setTextColor(this.context.getColorCompat(R.color.danger_soft_color))
    }

    tvTitle.text = message
    btnOk.text = textDismissButton
    btnOk.setOnClickListener {
        dismissClicked()
        snackbar.dismiss()
    }

    snackbarLayout.setBackgroundColor(Color.TRANSPARENT)
    snackbarLayout.addView(customSnackbar, 0)

    if (isAnchor) {
        snackbar.anchorView = this
    }
    snackbar.animationMode = Snackbar.ANIMATION_MODE_FADE
    snackbar.show()
}

fun Context.fadeInanimation() = AnimationUtils.loadAnimation(this, R.anim.fade_in)

fun Context.fadeOutanimation() = AnimationUtils.loadAnimation(this, R.anim.fade_out)

/** makes visible a view. */
fun View.visible() {
    visibility = View.VISIBLE
}

/** makes gone a view. */
fun View.gone() {
    visibility = View.GONE
}

/** makes invisible a view. */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/** makes invisible or gone a view. */
fun View.visibleWhen(isVisible: Boolean, animate: Boolean = false) {
    if (animate) {
        if (isVisible) {
            if (visibility != View.VISIBLE) {
                visibility = View.VISIBLE
                startAnimation(context.fadeInanimation())
            }
        } else {
            if (visibility != View.GONE) {
                startAnimation(context.fadeOutanimation())
                visibility = View.GONE
            }
        }
    } else visibility = if (isVisible) View.VISIBLE else View.GONE
}

/** set full edge */
fun View.setTransparentStatusBar(transparentStatusBar: Boolean) {
    val white = androidx.compose.ui.graphics.Color(
        ContextCompat.getColor(
            this.context,
            R.color.background_body
        )
    )
    if (!this.isInEditMode) {
        val window = (this.context as Activity).window
        window.navigationBarColor = white.toArgb()
        WindowCompat.getInsetsController(window, this).isAppearanceLightStatusBars = true
        WindowCompat.getInsetsController(window, this).isAppearanceLightNavigationBars = true
        if (!transparentStatusBar) {
            window.statusBarColor = white.toArgb()
        } else {
            window.statusBarColor = androidx.compose.ui.graphics.Color.Transparent.toArgb()
        }
    }
}

///** makes visible a view. */
//fun View.visibleWithAnimation(context: Context) {
//    if (visibility != View.VISIBLE) {
//        visibility = View.VISIBLE
//        startAnimation(context.fadeInanimation())
//    }
//}
//
///** makes gone a view. */
//fun View.goneWithAnimation(context: Context) {
//    if (visibility != View.GONE) {
//        startAnimation(context.fadeOutanimation())
//        visibility = View.GONE
//    }
//}
//
///** makes invisible a view. */
//fun View.invisibleWithAnimation(context: Context) {
//    if (visibility != View.INVISIBLE) {
//        startAnimation(context.fadeOutanimation())
//        visibility = View.INVISIBLE
//    }
//}

fun convertPxToDp(px: Float): Float {
    return px / Resources.getSystem().displayMetrics.density
}

fun convertDpToPx(dp: Float): Float {
    return dp * Resources.getSystem().displayMetrics.density
}


//val Int.dp: Int
//    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
//
//val Int.dpf: Float
//    get() = (this * Resources.getSystem().displayMetrics.density)

fun View.setWidth(width: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.LayoutParams
    menuLayoutParams.width = width
    this.layoutParams = menuLayoutParams
}

fun View.setHeight(height: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.LayoutParams
    menuLayoutParams.height = height
    this.layoutParams = menuLayoutParams
}

fun getWindowHeight(): Int = Resources.getSystem().displayMetrics.heightPixels

fun getWindowWidth(): Int = Resources.getSystem().displayMetrics.widthPixels

//fun ShimmerFrameLayout.startAndShow(animate: Boolean = false) {
//    this.startShimmer()
//    if (animate) {
//        this.alpha = 0f
//        this.visible()
//        this.animate().alpha(1f).setDuration(200).start()
//    } else {
//        this.visible()
//    }
//}
//
//fun ShimmerFrameLayout.stopAndHide() {
//    this.stopShimmer()
//    this.gone()
//}

fun View.setAdjustWindowImeResize(window: Window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            this.setPadding(0, 0, 0, imeHeight)
            insets
        }
    } else {
        @Suppress("DEPRECATION")
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

}

fun hideKeyboard(activity: Activity, windowToken: IBinder?) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun showKeyboard(activity: Activity, view: View?) {
    if (view != null && view.requestFocus()) {
        WindowCompat.getInsetsController(activity.window, view).show(WindowInsetsCompat.Type.ime())
    }
}

//fun showNoInternetDialog(fragmentManager: FragmentManager, onRetryClicked: () -> Unit) {
//    DefaultBottomSheetDialog.showDialog(
//        fragmentManager,
//        title = R.string.no_internet_connection,
//        description = R.string.no_internet_connection_makesure,
//        image = R.drawable.img_no_internet_connection,
//        onPositiveClick = { onRetryClicked() }
//    )
//}

//private lateinit var loadingDialog: LoadingDialog
//
//fun Context.showLoading(isVisible: Boolean = true) {
//    if (isVisible) {
//        loadingDialog = LoadingDialog(this)
//        loadingDialog.setCancelable(false)
//        loadingDialog.show()
//    } else {
//        runCatching {
//            loadingDialog.dismiss()
//        }
//    }
//}

//internal class LoadingDialog(context: Context) : Dialog(context, R.style.DialogStyle) {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.dialog_loading)
//        window!!.apply {
//            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            setLayout(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT
//            )
//        }
//        setOnCancelListener { }
//    }
//}

//fun View.showError(
//    state: State.Error,
//    onErrorString: ((String) -> Unit)? = null,
//    onElse: (() -> Unit)? = null,
//    onRetryWhenNoInternet: (() -> Unit)? = null
//) {
//    when (state.type) {
//        ErrorType.NO_INTERNET -> {
//            if (this.context is FragmentActivity) {
//                showNoInternetDialog((this.context as FragmentActivity).supportFragmentManager) {
//                    onRetryWhenNoInternet?.invoke()
//                }
//            } else {
//                this.showSnackBar(
//                    state.message?.ifBlank { this.context.getString(state.messageId) }
//                        ?: this.context.getString(R.string.an_error_occurred),
//                    SnackBarType.ERROR
//                )
//            }
//        }
//
//        ErrorType.ERROR_STRING -> {
//            onErrorString?.invoke(state.message?.ifBlank { this.context.getString(state.messageId) }
//                ?: this.context.getString(R.string.an_error_occurred))
//        }
//
//        else -> {
//            onElse?.invoke()
//            this.showSnackBar(
//                this.context.getString(state.messageId).ifBlank { state.message }
//                    ?: this.context.getString(R.string.an_error_occurred),
//                SnackBarType.ERROR
//            )
//        }
//    }
//}
//
//fun View.showError(
//    state: State.Error,
//    onElse: (() -> Unit)? = null,
//    onRetryWhenNoInternet: (() -> Unit)? = null
//) {
//    when (state.type) {
//        ErrorType.NO_INTERNET -> {
//            if (this.context is FragmentActivity) {
//                showNoInternetDialog((this.context as FragmentActivity).supportFragmentManager) {
//                    onRetryWhenNoInternet?.invoke()
//                }
//            } else {
//                this.showSnackBar(
//                    state.message?.ifBlank { this.context.getString(state.messageId) }
//                        ?: this.context.getString(R.string.an_error_occurred),
//                    SnackBarType.ERROR
//                )
//            }
//        }
//
//        ErrorType.ERROR_STRING -> {
//            this.showSnackBar(
//                state.message?.ifBlank { this.context.getString(state.messageId) }
//                    ?: this.context.getString(R.string.an_error_occurred),
//                SnackBarType.ERROR
//            )
//        }
//
//        else -> {
//            onElse?.invoke()
//            this.showSnackBar(
//                this.context.getString(state.messageId).ifBlank { state.message }
//                    ?: this.context.getString(R.string.an_error_occurred),
//                SnackBarType.ERROR
//            )
//        }
//    }
//}

fun Window.isLightSystemBars() {
    WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightNavigationBars =
        true
    WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = true
}

//fun Window.hideViewWhenKeyboardShown(view: View, animate: Boolean = false) {
//    ViewCompat.setOnApplyWindowInsetsListener(this.decorView) { _: View?, insets: WindowInsetsCompat ->
//        val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
//        view.visibleWhen(!isKeyboardVisible, animate)
//        return@setOnApplyWindowInsetsListener insets
//    }
//}

//open class NoUnderlineClickSpan(val context: Context) : ClickableSpan() {
//    override fun updateDrawState(textPaint: TextPaint) {
//        textPaint.isUnderlineText = false
//        textPaint.color = ContextCompat.getColor(context, R.color.primary)
//    }
//
//    override fun onClick(widget: View) {}
//}

//fun TextView.setResizableText(
//    fullText: String,
//    maxLines: Int,
//    viewMore: Boolean,
//    applyExtraHighlights: ((Spannable) -> (Spannable))? = null
//) {
//    val width = width
//    if (width <= 0) {
//        post {
//            setResizableText(fullText, maxLines, viewMore, applyExtraHighlights)
//        }
//        return
//    }
//    movementMethod = LinkMovementMethod.getInstance()
//    // Since we take the string character by character, we don't want to break up the Windows-style
//    // line endings.
//    val adjustedText = fullText.replace("\r\n", "\n")
//    // Check if even the text has to be resizable.
//    val textLayout = StaticLayout(
//        adjustedText,
//        paint,
//        width - paddingLeft - paddingRight,
//        Layout.Alignment.ALIGN_NORMAL,
//        lineSpacingMultiplier,
//        lineSpacingExtra,
//        includeFontPadding
//    )
//    if (textLayout.lineCount <= maxLines || adjustedText.isEmpty()) {
//        // No need to add 'read more' / 'read less' since the text fits just as well (less than max lines #).
//        val htmlText = adjustedText.replace("\n", "<br/>")
//        text = addClickablePartTextResizable(
//            fullText,
//            maxLines,
//            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
//            null,
//            viewMore,
//            applyExtraHighlights
//        )
//        return
//    }
//    val charactersAtLineEnd = textLayout.getLineEnd(maxLines - 1)
//    val suffixText =
//        if (viewMore) resources.getString(R.string.resizable_text_read_more) else resources.getString(R.string.resizable_text_read_less)
//    var charactersToTake = charactersAtLineEnd - suffixText.length / 2 // Good enough first guess
//    if (charactersToTake <= 0) {
//        // Happens when text is empty
//        val htmlText = adjustedText.replace("\n", "<br/>")
//        text = addClickablePartTextResizable(
//            fullText,
//            maxLines,
//            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
//            null,
//            viewMore,
//            applyExtraHighlights
//        )
//        return
//    }
//    if (!viewMore) {
//        // We can set the text immediately because nothing needs to be measured
//        val htmlText = adjustedText.replace("\n", "<br/>")
//        text = addClickablePartTextResizable(
//            fullText,
//            maxLines,
//            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
//            suffixText,
//            viewMore,
//            applyExtraHighlights
//        )
//        return
//    }
//    val lastHasNewLine =
//        adjustedText.substring(textLayout.getLineStart(maxLines - 1), textLayout.getLineEnd(maxLines - 1))
//            .contains("\n")
//    var linedText = if (lastHasNewLine) {
//        val charactersPerLine =
//            textLayout.getLineEnd(0) / (textLayout.getLineWidth(0) / textLayout.ellipsizedWidth.toFloat())
//        val lineOfSpaces =
//            "\u00A0".repeat(charactersPerLine.roundToInt()) // non breaking space, will not be thrown away by HTML parser
//        charactersToTake += lineOfSpaces.length - 1
//        adjustedText.take(textLayout.getLineStart(maxLines - 1)) +
//                adjustedText.substring(textLayout.getLineStart(maxLines - 1), textLayout.getLineEnd(maxLines - 1))
//                    .replace("\n", lineOfSpaces) +
//                adjustedText.substring(textLayout.getLineEnd(maxLines - 1))
//    } else {
//        adjustedText
//    }
//    // Check if we perhaps need to even add characters? Happens very rarely, but can be possible if there was a long word just wrapped
//    val shortenedString = linedText.take(charactersToTake)
//    val shortenedStringWithSuffix = shortenedString + suffixText
//    val shortenedStringWithSuffixLayout = StaticLayout(
//        shortenedStringWithSuffix,
//        paint,
//        width - paddingLeft - paddingRight,
//        Layout.Alignment.ALIGN_NORMAL,
//        lineSpacingMultiplier,
//        lineSpacingExtra,
//        includeFontPadding
//    )
//    val modifier: Int
//    if (shortenedStringWithSuffixLayout.getLineEnd(maxLines - 1) >= shortenedStringWithSuffix.length) {
//        modifier = 1
//        charactersToTake-- // We might just be at the right position already
//    } else {
//        modifier = -1
//    }
//    do {
//        charactersToTake += modifier
//        val baseString = linedText.take(charactersToTake)
//        val appended = baseString + suffixText
//        val newLayout = StaticLayout(
//            appended,
//            paint,
//            width - paddingLeft - paddingRight,
//            Layout.Alignment.ALIGN_NORMAL,
//            lineSpacingMultiplier,
//            lineSpacingExtra,
//            includeFontPadding
//        )
//    } while ((modifier < 0 && newLayout.getLineEnd(maxLines - 1) < appended.length) ||
//        (modifier > 0 && newLayout.getLineEnd(maxLines - 1) >= appended.length)
//    )
//    if (modifier > 0) {
//        charactersToTake-- // We went overboard with 1 char, fixing that
//    }
//    // We need to convert newlines because we are going over to HTML now
//    val htmlText = linedText.take(charactersToTake).replace("\n", "<br/>")
//    text = addClickablePartTextResizable(
//        fullText,
//        maxLines,
//        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
//        suffixText,
//        viewMore,
//        applyExtraHighlights
//    )
//}

//private fun TextView.addClickablePartTextResizable(
//    fullText: String,
//    maxLines: Int,
//    shortenedText: Spanned,
//    clickableText: String?,
//    viewMore: Boolean,
//    applyExtraHighlights: ((Spannable) -> (Spannable))? = null
//): Spannable {
//    val builder = SpannableStringBuilder(shortenedText)
//    if (clickableText != null) {
//        builder.append(clickableText)
//        builder.setSpan(object : NoUnderlineClickSpan(context) {
//            override fun onClick(widget: View) {
//                if (viewMore) {
//                    setResizableText(fullText, maxLines, false, applyExtraHighlights)
//                } else {
//                    setResizableText(fullText, maxLines, true, applyExtraHighlights)
//                }
//            }
//        }, builder.indexOf(clickableText), builder.indexOf(clickableText) + clickableText.length, 0)
//    }
//    if (applyExtraHighlights != null) {
//        return applyExtraHighlights(builder)
//    }
//    return builder
//}
//
//fun Chip.setSelectedStyle(isSelected: Boolean = true) {
//    this.chipBackgroundColor = ContextCompat.getColorStateList(
//        this.context,
//        if (isSelected) R.color.primary50 else R.color.gray50
//    )
//    this.chipStrokeColor = ContextCompat.getColorStateList(
//        this.context,
//        if (isSelected) R.color.primary else R.color.stroke
//    )
//}