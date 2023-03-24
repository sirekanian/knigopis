package com.sirekanyan.knigopis.feature.user

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.android.adapter.CommonViewHolder
import com.sirekanyan.knigopis.common.extensions.setSquareImage
import com.sirekanyan.knigopis.common.extensions.showNow
import com.sirekanyan.knigopis.model.BookDataModel
import com.sirekanyan.knigopis.model.BookModel

class UserBookDataViewHolder(
    containerView: View,
    private val onClick: (BookDataModel) -> Unit,
) : CommonViewHolder<BookModel>(containerView) {

    private val bookTitle = containerView.findViewById<TextView>(R.id.bookTitle)
    private val bookAuthor = containerView.findViewById<TextView>(R.id.bookAuthor)
    private val bookNotes = containerView.findViewById<TextView>(R.id.bookNotes)
    private val bookImage = containerView.findViewById<ImageView>(R.id.bookImage)

    init {
        containerView.setOnLongClickListener {
            model?.let { book ->
                onClick(book as BookDataModel)
            }
            true
        }
    }

    override fun onBind(position: Int, model: BookModel) {
        val book = model as BookDataModel
        bookTitle.text = book.title
        bookAuthor.text = book.author
        bookNotes.showNow(book.notes.isNotEmpty())
        bookNotes.text = book.notes
        bookImage.setSquareImage(book.image)
    }

}