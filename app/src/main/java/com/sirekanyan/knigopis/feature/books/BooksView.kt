package com.sirekanyan.knigopis.feature.books

import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.android.dialog.DialogFactory
import com.sirekanyan.knigopis.common.android.dialog.createDialogItem
import com.sirekanyan.knigopis.common.android.header.HeaderItemDecoration
import com.sirekanyan.knigopis.common.android.header.StickyHeaderImpl
import com.sirekanyan.knigopis.common.android.toast.CommonView
import com.sirekanyan.knigopis.common.extensions.*
import com.sirekanyan.knigopis.common.functions.handleError
import com.sirekanyan.knigopis.feature.ProgressView
import com.sirekanyan.knigopis.model.BookDataModel
import com.sirekanyan.knigopis.model.BookModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.books_page.*

interface BooksView : CommonView, ProgressView {

    fun updateBooks(books: List<BookModel>)
    fun showBooksError(throwable: Throwable)
    fun showBookActions(book: BookDataModel)
    fun showBookDeleteDialog(book: BookDataModel)

    interface Callbacks {
        fun onAddBookClicked()
        fun onEditBookClicked(book: BookDataModel)
        fun onDeleteBookClicked(book: BookDataModel)
        fun onDeleteBookConfirmed(book: BookDataModel)
        fun onBookClicked(book: BookDataModel)
        fun onBookLongClicked(book: BookDataModel)
        fun onBooksUpdated()
    }

}

class BooksViewImpl(
    override val containerView: View,
    private val callbacks: BooksView.Callbacks,
    progressView: ProgressView,
    private val dialogs: DialogFactory
) : BooksView, LayoutContainer, ProgressView by progressView {

    private val booksAdapter = BooksAdapter(callbacks::onBookClicked, callbacks::onBookLongClicked)

    init {
        booksRecyclerView.adapter = booksAdapter
        booksRecyclerView.addItemDecoration(HeaderItemDecoration(StickyHeaderImpl(booksAdapter)))
        booksRecyclerView.addOnScrollListener(FabOnScrollListener(resources, addBookButton))
        addBookButton.setOnClickListener { callbacks.onAddBookClicked() }
    }

    override fun updateBooks(books: List<BookModel>) {
        booksPlaceholder.show(books.isEmpty())
        booksErrorPlaceholder.hide()
        booksAdapter.submitList(books)
        callbacks.onBooksUpdated()
    }

    override fun showBooksError(throwable: Throwable) {
        handleError(throwable, booksPlaceholder, booksErrorPlaceholder, booksAdapter)
    }

    override fun showBookActions(book: BookDataModel) {
        val bookFullTitle = resources.getFullTitleString(book.title, book.author)
        dialogs.showDialog(
            bookFullTitle,
            createDialogItem(R.string.books_button_edit, R.drawable.ic_edit) {
                callbacks.onEditBookClicked(book)
            },
            createDialogItem(R.string.books_button_delete, R.drawable.ic_delete) {
                callbacks.onDeleteBookClicked(book)
            }
        )
    }

    override fun showBookDeleteDialog(book: BookDataModel) {
        val bookFullTitle = resources.getFullTitleString(book.title, book.author)
        AlertDialog.Builder(context)
            .setTitle(R.string.books_title_confirm_delete)
            .setMessage(context.getString(R.string.books_message_confirm_delete, bookFullTitle))
            .setNegativeButton(R.string.common_button_cancel) { d, _ -> d.dismiss() }
            .setPositiveButton(R.string.books_button_confirm_delete) { d, _ ->
                callbacks.onDeleteBookConfirmed(book)
                d.dismiss()
            }
            .show()
    }

}