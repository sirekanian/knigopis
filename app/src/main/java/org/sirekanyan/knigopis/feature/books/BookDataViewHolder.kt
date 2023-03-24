package org.sirekanyan.knigopis.feature.books

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.adapter.CommonViewHolder
import org.sirekanyan.knigopis.common.extensions.*
import org.sirekanyan.knigopis.model.BookDataModel
import org.sirekanyan.knigopis.model.BookModel

class BookDataViewHolder(
    containerView: View,
    onClick: (BookDataModel) -> Unit,
    onLongClick: (BookDataModel) -> Unit,
) : CommonViewHolder<BookModel>(containerView) {

    private val resources = containerView.resources
    private val bookImage = containerView.findViewById<ImageView>(R.id.bookImage)
    private val bookTitle = containerView.findViewById<TextView>(R.id.bookTitle)
    private val bookAuthor = containerView.findViewById<TextView>(R.id.bookAuthor)
    private val bookProgress = containerView.findViewById<ProgressBar>(R.id.bookProgress)

    init {
        containerView.setOnClickListener {
            model?.let { book ->
                onClick(book as BookDataModel)
            }
        }
        containerView.setOnLongClickListener {
            model?.let { book ->
                onLongClick(book as BookDataModel)
            }
            true
        }
    }

    override fun onBind(position: Int, model: BookModel) {
        val book = model as BookDataModel
        bookImage.setSquareImage(book.image)
        bookTitle.text = resources.getTitleString(book.title)
        bookAuthor.text = resources.getAuthorString(book.author)
        bookProgress.progress = 0
        if (book.isFinished) {
            bookProgress.hideNow()
        } else {
            bookProgress.showNow()
            bookProgress.setProgressSmoothly(book.priority)
        }
    }

}