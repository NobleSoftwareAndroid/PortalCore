package com.noblesoftware.portalcore.util.extension

import android.text.Html
import android.util.Patterns
import java.util.Locale

fun String.toLower() = this.lowercase(Locale.ENGLISH)

fun String.toUpper() = this.uppercase(Locale.ENGLISH)

fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


fun String?.ifValueIs(value: String, execute: () -> String): String {
    if (this.equals(value)) {
        return execute.invoke()
    }
    return this.orEmpty()
}

fun String?.ifNull(execute: () -> String): String {
    return this ?: execute.invoke()
}

inline fun String?.ifNotNullOrEmpty(execute: (String) -> String) =
    if (!this.isNullOrBlank()) execute.invoke(this) else this

inline fun String?.ifNullOrEmpty(execute: () -> String) =
    if (this.isNullOrBlank()) execute.invoke() else this

val String.capitalizeWords: String
    get() = split(" ").joinToString(" ") { it ->
        it.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }
    }


val String.fileName: String
    get() = try {
        val fileName = this.split("/")
        fileName[fileName.lastIndex]
    } catch (e: Exception) {
        "-"
    }

val String.fileNameWithOutExtension: String
    get() = try {
        val fileName = this.split("/")
        fileName[fileName.lastIndex].split(".").first()
    } catch (e: Exception) {
        "-"
    }

val String.fileExtension: String
    get() = try {
        val arrNameExt = this.split(".")
        arrNameExt[arrNameExt.lastIndex].lowercase()
    } catch (e: Exception) {
        ""
    }

fun String?.orSelectNone(): String = if (this.isNullOrBlank()) "none" else this

val String.graph: String
    get() = this + "_graph"

fun String.addArgument(argument: String, nullable: Boolean? = false): String {
    return if (nullable == true) if (this.contains("?")) "${this}&$argument={$argument}" else "${this}?$argument={$argument}" else "${this}/{$argument}"
}

fun String.addArgumentValue(argument: String, value: String, nullable: Boolean? = false): String {
    return if (nullable == true) if (this.contains("?")) "${this}&$argument=$value" else "${this}?$argument=$value" else "${this}/$value"
}

fun String.toHtmlFormat(): String {
    val header = "<head>\n" +
            "<style = 'text/css'>\n" +
//            "@font-face {font-family: poppinsreguler;" +
//            "src: url(file:///android_res/font/poppinsregular.ttf);}\n" +
            "body {" +
            "font-size: 14px;" +
            "text-align: left;" +
            "margin-left: 0px; " +
//            "font-family: poppinsreguler;" +
            "color: #303030" +
            "}\n" +
            "</style>\n" +
            "</head>\n"

    var body = this
    if (body.contains("<img", true)) {
        body = body.replace("<img", "<img style=\"height: auto;max-width: 100%\"", true)
    }

    return "$header<body> $body </body>"
}

fun String.htmlToString(keepSpacing: Boolean = false): String {
    var text = Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).toString()
    if (!keepSpacing) {
        text = text.replace("\n", " ")
            .replace("\t", "")
            .trim()
    }
    return text
}

fun String.removeSubdomain(): String {
    val protocolEndIndex = this.indexOf("://")

    if (protocolEndIndex != -1) {
        val afterProtocol = this.substring(protocolEndIndex + 3) // +3 to skip "://"
        val firstDotIndex = afterProtocol.indexOf(".")

        if (firstDotIndex != -1) {
            val beforeDot = afterProtocol.substring(0, firstDotIndex)
            // Reconstruct the URL: protocol + (part before the removed string) + (part after the removed string)
            return this.substring(0, protocolEndIndex + 3) + afterProtocol.substring(firstDotIndex)
        }
    }
    // If "://" or a dot after "://" is not found, return the original URL
    return this
}

fun String.toHtmlFormatMention(): String {
    return "<span id=\"#${System.currentTimeMillis()}\" style=\"color: #034aa6;\" contenteditable=\"false\"><strong>@$this</strong></span>&nbsp;"
}