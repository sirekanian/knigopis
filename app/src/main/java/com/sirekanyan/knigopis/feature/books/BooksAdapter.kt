package com.sirekanyan.knigopis.feature.books

import android.view.ViewGroup
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.adapter.CommonAdapter
import com.sirekanyan.knigopis.common.extensions.inflate
import com.sirekanyan.knigopis.model.BookDataModel
import com.sirekanyan.knigopis.model.BookModel

class BooksAdapter(
    private val onClick: (BookDataModel) -> Unit,
    private val onLongClick: (BookDataModel) -> Unit
) : CommonAdapter<BookModel>() {

    override fun onCreateHeaderViewHolder(parent: ViewGroup) =
        BookHeaderViewHolder(parent.inflate(R.layout.header))

    override fun onCreateDataViewHolder(parent: ViewGroup) =
        BookDataViewHolder(parent.inflate(R.layout.book), onClick, onLongClick)

}