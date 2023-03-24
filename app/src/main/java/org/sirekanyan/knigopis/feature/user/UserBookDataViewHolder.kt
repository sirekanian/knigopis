package org.sirekanyan.knigopis.feature.user

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.adapter.CommonViewHolder
import org.sirekanyan.knigopis.common.extensions.setSquareImage
import org.sirekanyan.knigopis.common.extensions.showNow
import org.sirekanyan.knigopis.model.BookDataModel
import org.sirekanyan.knigopis.model.BookModel

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