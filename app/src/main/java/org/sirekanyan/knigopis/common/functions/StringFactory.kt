package org.sirekanyan.knigopis.common.functions

import org.sirekanyan.knigopis.BuildConfig.APPLICATION_ID
import org.sirekanyan.knigopis.MAIN_WWW
import org.sirekanyan.knigopis.STATIC_API

fun extra(name: String) = "$APPLICATION_ID.extra_$name"

fun createUserPublicUrl(userId: String) = "$MAIN_WWW/#/user/books?u=$userId"

fun createUserImageUrl(userId: String) = "$STATIC_API/user/$userId"

fun createBookImageUrl(bookTitle: String): String {
    val normalizedTitle = bookTitle.lowercase()
        .replace(Regex("[^\\w-]+"), "_")
        .replace(Regex("(^_|_$)"), "")
        .replace("ё", "е")
    return "$STATIC_API/book/$normalizedTitle"
}