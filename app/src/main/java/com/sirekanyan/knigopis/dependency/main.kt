package com.sirekanyan.knigopis.dependency

import com.sirekanyan.knigopis.common.android.dialog.DialogFactory
import com.sirekanyan.knigopis.common.extensions.app
import com.sirekanyan.knigopis.feature.*
import com.sirekanyan.knigopis.feature.books.BooksPresenterImpl
import com.sirekanyan.knigopis.feature.books.BooksViewImpl
import com.sirekanyan.knigopis.feature.notes.NotesPresenterImpl
import com.sirekanyan.knigopis.feature.notes.NotesViewImpl
import com.sirekanyan.knigopis.feature.users.UsersPresenterImpl
import com.sirekanyan.knigopis.feature.users.UsersViewImpl
import com.sirekanyan.knigopis.model.CurrentTab.*

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