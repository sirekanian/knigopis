package org.sirekanyan.knigopis.common.functions

import android.content.Intent

private const val TEXT_MIME_TYPE = "text/plain"

fun createTextShareIntent(text: String): Intent =
    Intent(Intent.ACTION_SEND)
        .setType(TEXT_MIME_TYPE)
        .putExtra(Intent.EXTRA_TEXT, text)
        .let { Intent.createChooser(it, null) }
