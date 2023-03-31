package org.sirekanyan.knigopis.feature.books

import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.BasePresenter
import org.sirekanyan.knigopis.common.extensions.io2main
import org.sirekanyan.knigopis.common.extensions.showProgressBar
import org.sirekanyan.knigopis.common.extensions.toast
import org.sirekanyan.knigopis.common.functions.logError
import org.sirekanyan.knigopis.feature.PagePresenter
import org.sirekanyan.knigopis.feature.PagesPresenter
import org.sirekanyan.knigopis.model.BookDataModel
import org.sirekanyan.knigopis.model.CurrentTab
import org.sirekanyan.knigopis.repository.BookRepository

interface BooksPresenter : PagePresenter {

    interface Router {
        fun openNewBookScreen()
        fun openBookScreen(book: BookDataModel)
    }

}

class BooksPresenterImpl(
    private val router: BooksPresenter.Router,
    private val bookRepository: BookRepository,
) : BasePresenter<BooksView>(),
    BooksPresenter,
    BooksView.Callbacks {

    lateinit var parent: PagesPresenter

    override fun refresh() {
        bookRepository.observeBooks()
            .io2main()
            .showProgressBar(view)
            .bind({ books ->
                view.updateBooks(books)
            }, {
                logError("cannot load books", it)
                view.showBooksError(it)
            })
    }

    override fun onAddBookClicked() {
        router.openNewBookScreen()
    }

    override fun onBookClicked(book: BookDataModel) {
        router.openBookScreen(book)
    }

    override fun onBookLongClicked(book: BookDataModel) {
        view.showBookActions(book)
    }

    override fun onEditBookClicked(book: BookDataModel) {
        router.openBookScreen(book)
    }

    override fun onDeleteBookClicked(book: BookDataModel) {
        view.showBookDeleteDialog(book)
    }

    override fun onDeleteBookConfirmed(book: BookDataModel) {
        bookRepository.deleteBook(book)
            .io2main()
            .bind({
                refresh()
            }, {
                view.toast(R.string.books_error_delete)
                logError("cannot delete finished book", it)
            })
    }

    override fun onBooksUpdated() {
        parent.onPageUpdated(CurrentTab.BOOKS_TAB)
    }

}