package com.sirekanyan.knigopis.feature.books

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.android.adapter.CommonViewHolder
import com.sirekanyan.knigopis.common.extensions.*
import com.sirekanyan.knigopis.model.BookDataModel
import com.sirekanyan.knigopis.model.BookModel

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