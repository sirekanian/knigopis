package com.sirekanyan.knigopis.feature.user

import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.adapter.AbstractBooksAdapter
import com.sirekanyan.knigopis.common.adapter.BookHeaderViewHolder
import com.sirekanyan.knigopis.common.adapter.BookItemViewHolder
import com.sirekanyan.knigopis.common.view.dialog.DialogFactory
import com.sirekanyan.knigopis.common.view.dialog.createDialogItem
import com.sirekanyan.knigopis.feature.book.createNewBookIntent
import com.sirekanyan.knigopis.repository.model.Book
import com.sirekanyan.knigopis.repository.model.BookHeader
import com.sirekanyan.knigopis.repository.model.FinishedBook

class BooksAdapter(
    books: List<Book>,
    private val dialogs: DialogFactory
) : AbstractBooksAdapter(books, R.layout.header, R.layout.user_book) {

    override fun bindHeaderViewHolder(holder: BookHeaderViewHolder, header: BookHeader, i: Int) {
        if (header.title.isEmpty()) {
            holder.setTitle(R.string.books_header_done_other)
        } else {
            holder.setTitle(header.title)
        }
        holder.showTopDivider(i > 0)
        holder.setBooksCount(header.count)
    }

    override fun bindItemViewHolder(holder: BookItemViewHolder, book: FinishedBook) {
        holder.setTitle(book.title)
        holder.setAuthor(book.author)
        holder.setNotes(book.notes)
        holder.setBookImageUrl(book.bookImageUrl)
        holder.setOnLongClick { view ->
            val context = view.context
            dialogs.showDialog(
                book.title + " — " + book.author,
                createDialogItem(R.string.user_button_todo, R.drawable.ic_playlist_add) {
                    context.startActivity(context.createNewBookIntent(book.title, book.author))
                },
                createDialogItem(R.string.user_button_done, R.drawable.ic_playlist_add_check) {
                    context.startActivity(context.createNewBookIntent(book.title, book.author, 100))
                }
            )
            true
        }
    }

}