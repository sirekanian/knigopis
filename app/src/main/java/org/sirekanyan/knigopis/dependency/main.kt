package org.sirekanyan.knigopis.dependency

import org.sirekanyan.knigopis.common.android.dialog.DialogFactory
import org.sirekanyan.knigopis.common.extensions.app
import org.sirekanyan.knigopis.feature.*
import org.sirekanyan.knigopis.feature.books.BooksPresenterImpl
import org.sirekanyan.knigopis.feature.books.BooksViewImpl
import org.sirekanyan.knigopis.feature.notes.NotesPresenterImpl
import org.sirekanyan.knigopis.feature.notes.NotesViewImpl
import org.sirekanyan.knigopis.feature.users.UsersPresenterImpl
import org.sirekanyan.knigopis.feature.users.UsersViewImpl
import org.sirekanyan.knigopis.model.CurrentTab.*

fun MainActivity.providePresenter(): MainPresenter {
    val booksPresenter = BooksPresenterImpl(this, app.bookRepository)
    val usersPresenter = UsersPresenterImpl(this, app.userRepository, app.resourceProvider)
    val notesPresenter = NotesPresenterImpl(this, app.noteRepository)
    return MainPresenterImpl(
        mapOf(
            BOOKS_TAB to booksPresenter,
            USERS_TAB to usersPresenter,
            NOTES_TAB to notesPresenter
        ),
        this,
        app.config,
        app.authRepository
    ).also { mainPresenter ->
        val progressView = ProgressViewImpl(binding, mainPresenter)
        val dialogs: DialogFactory = provideDialogs()
        booksPresenter.also { p ->
            p.view = BooksViewImpl(binding.books, booksPresenter, progressView, dialogs)
            p.parent = mainPresenter
        }
        usersPresenter.also { p ->
            p.view = UsersViewImpl(binding.users, usersPresenter, progressView, dialogs)
            p.parent = mainPresenter
        }
        notesPresenter.also { p ->
            p.view = NotesViewImpl(binding.notes, notesPresenter, progressView)
            p.parent = mainPresenter
        }
        mainPresenter.view = MainViewImpl(binding, mainPresenter)
    }
}