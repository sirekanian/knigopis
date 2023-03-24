package org.sirekanyan.knigopis.feature.books

import android.view.ViewGroup
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.adapter.HeadedAdapter
import org.sirekanyan.knigopis.common.extensions.inflate
import org.sirekanyan.knigopis.model.BookDataModel
import org.sirekanyan.knigopis.model.BookModel

class BooksAdapter(
    private val onClick: (BookDataModel) -> Unit,
    private val onLongClick: (BookDataModel) -> Unit
) : HeadedAdapter<BookModel>(BookItemCallback()) {

    override fun onCreateHeaderViewHolder(parent: ViewGroup) =
        BookHeaderViewHolder(parent.inflate(R.layout.header))

    override fun onCreateDataViewHolder(parent: ViewGroup) =
        BookDataViewHolder(parent.inflate(R.layout.book), onClick, onLongClick)

}