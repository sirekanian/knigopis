package org.sirekanyan.knigopis.common.functions

import android.content.Context
import android.content.Intent
import org.sirekanyan.knigopis.R

private const val TEXT_MIME_TYPE = "text/plain"

fun Context.createProfileShareIntent(text: String) =
    createTextShareIntent(text, getString(R.string.profile_title_share))

private fun createTextShareIntent(text: String, title: String): Intent =
    Intent(Intent.ACTION_SEND)
        .setType(TEXT_MIME_TYPE)
        .putExtra(Intent.EXTRA_TEXT, text)
        .let { Intent.createChooser(it, title) }