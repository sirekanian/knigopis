package org.sirekanyan.knigopis.feature.user

import android.view.ViewGroup
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.adapter.HeadedAdapter
import org.sirekanyan.knigopis.common.android.adapter.SimpleItemCallback
import org.sirekanyan.knigopis.common.extensions.inflate
import org.sirekanyan.knigopis.model.BookDataModel
import org.sirekanyan.knigopis.model.BookModel

class UserBooksAdapter(
    private val onLongClick: (BookDataModel) -> Unit,
) : HeadedAdapter<BookModel>(SimpleItemCallback { it.id }) {

    override fun onCreateHeaderViewHolder(parent: ViewGroup) =
        UserBookHeaderViewHolder(parent.inflate(R.layout.header))

    override fun onCreateDataViewHolder(parent: ViewGroup) =
        UserBookDataViewHolder(parent.inflate(R.layout.user_book), onLongClick)

}