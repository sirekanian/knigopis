package org.sirekanyan.knigopis.dependency

import org.sirekanyan.knigopis.common.extensions.app
import org.sirekanyan.knigopis.feature.book.*
import org.sirekanyan.knigopis.model.EditBookModel

fun BookActivity.providePresenter(book: EditBookModel): BookPresenter {
    val interactor = BookInteractorImpl(app.bookRepository)
    return BookPresenterImpl(this, interactor, book).also { presenter ->
        presenter.view = BookViewImpl(binding, presenter, book)
    }
}