package org.sirekanyan.knigopis.feature.books

import androidx.appcompat.app.AlertDialog
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.dialog.DialogFactory
import org.sirekanyan.knigopis.common.android.dialog.createDialogItem
import org.sirekanyan.knigopis.common.android.header.HeaderItemDecoration
import org.sirekanyan.knigopis.common.android.header.StickyHeaderImpl
import org.sirekanyan.knigopis.common.android.toast.CommonView
import org.sirekanyan.knigopis.common.extensions.*
import org.sirekanyan.knigopis.common.functions.handleError
import org.sirekanyan.knigopis.databinding.BooksPageBinding
import org.sirekanyan.knigopis.feature.ProgressView
import org.sirekanyan.knigopis.model.BookDataModel
import org.sirekanyan.knigopis.model.BookModel

interface BooksView : CommonView<BooksPageBinding>, ProgressView {

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
    override val binding: BooksPageBinding,
    private val callbacks: BooksView.Callbacks,
    progressView: ProgressView,
    private val dialogs: DialogFactory,
) : BooksView,
    ProgressView by progressView {

    private val booksRecyclerView = binding.booksRecyclerView
    private val addBookButton = binding.addBookButton
    private val booksPlaceholder = binding.booksPlaceholder
    private val booksErrorPlaceholder = binding.booksErrorPlaceholder

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