package com.sirekanyan.knigopis.common.extensions

import android.content.res.Resources
import com.sirekanyan.knigopis.R

fun Resources.getTitleString(title: String): String =
    title.ifEmpty { getString(R.string.common_book_notitle) }

fun Resources.getAuthorString(author: String): String =
    author.ifEmpty { getString(R.string.common_book_noauthor) }

fun Resources.getFullTitleString(title: String, author: String): String =
    if (author.isEmpty()) {
        getTitleString(title)
    } else {
        "${getTitleString(title)} — ${getAuthorString(author)}"
    }
