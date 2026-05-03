package org.sirekanyan.knigopis.common.extensions

import android.net.Uri
import androidx.core.net.toUri

private val HTTP_SCHEMES = setOf("http", "https")

fun String.toUriOrNull() =
    this.toUri().takeIf(Uri::isValidHttpLink)

private fun Uri.isValidHttpLink(): Boolean {
    val scheme: String? = scheme
    return scheme in HTTP_SCHEMES && !host.isNullOrBlank()
}