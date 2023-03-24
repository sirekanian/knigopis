package org.sirekanyan.knigopis.model

import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.ResourceProvider
import org.sirekanyan.knigopis.common.android.adapter.HeadedModel
import org.sirekanyan.knigopis.common.functions.createBookImageUrl

fun createBookHeaderModel(resources: ResourceProvider, title: String, count: Int): BookHeaderModel {
    val titleOrDefault = title.ifEmpty { resources.getString(R.string.books_header_done_other) }
    val countText = resources.getQuantityString(R.plurals.common_header_books, count, count)
    return BookHeaderModel(titleOrDefault, countText)
}

sealed class BookModel(
    val id: String,
    override val isHeader: Boolean,
    val group: BookGroupModel
) : HeadedModel

class BookHeaderModel(
    val title: String,
    val count: String
) : BookModel("header-id-$title", true, BookGroupModel(title, count))

class BookDataModel(
    id: String,
    group: BookGroupModel,
    val title: String,
    val author: String,
    val isFinished: Boolean,
    val priority: Int,
    val date: DateModel,
    val notes: String
) : BookModel(id, false, group) {
    val image = createBookImageUrl(title)
}