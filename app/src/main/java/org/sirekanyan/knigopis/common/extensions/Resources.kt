package org.sirekanyan.knigopis.common.extensions

import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.ResourceProvider

fun ResourceProvider.getTitleString(title: String): String =
    title.ifEmpty { getString(R.string.common_book_notitle) }

fun ResourceProvider.getAuthorString(author: String): String =
    author.ifEmpty { getString(R.string.common_book_noauthor) }

fun ResourceProvider.getFullTitleString(title: String, author: String): String =
    if (author.isEmpty()) {
        getTitleString(title)
    } else {
        "${getTitleString(title)} — ${getAuthorString(author)}"
    }
