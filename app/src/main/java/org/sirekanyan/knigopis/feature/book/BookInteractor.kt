package org.sirekanyan.knigopis.feature.book

import org.sirekanyan.knigopis.MAX_BOOK_PRIORITY
import org.sirekanyan.knigopis.model.EditBookModel
import org.sirekanyan.knigopis.model.toFinishedBook
import org.sirekanyan.knigopis.model.toPlannedBook
import org.sirekanyan.knigopis.repository.BookRepository
import io.reactivex.Completable

interface BookInteractor {

    fun saveBook(initialBook: EditBookModel, book: EditBookModel): Completable

}

class BookInteractorImpl(private val repository: BookRepository) : BookInteractor {

    override fun saveBook(initialBook: EditBookModel, book: EditBookModel): Completable =
        if (book.progress == MAX_BOOK_PRIORITY) {
            repository.saveBook(book.id, book.toFinishedBook(), initialBook.isFinished)
        } else {
            repository.saveBook(book.id, book.toPlannedBook(), initialBook.isPlanned)
        }

}