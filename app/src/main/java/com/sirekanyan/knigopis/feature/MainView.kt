package com.sirekanyan.knigopis.feature

import android.content.Context.MODE_PRIVATE
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.sirekanyan.knigopis.BuildConfig
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.extensions.*
import com.sirekanyan.knigopis.common.view.dialog.DialogFactory
import com.sirekanyan.knigopis.common.view.dialog.DialogItem
import com.sirekanyan.knigopis.common.view.dialog.createDialogItem
import com.sirekanyan.knigopis.common.view.header.HeaderItemDecoration
import com.sirekanyan.knigopis.common.view.header.StickyHeaderImpl
import com.sirekanyan.knigopis.feature.books.BooksAdapter
import com.sirekanyan.knigopis.feature.notes.NotesAdapter
import com.sirekanyan.knigopis.feature.users.UriItem
import com.sirekanyan.knigopis.feature.users.UsersAdapter
import com.sirekanyan.knigopis.model.*
import com.sirekanyan.knigopis.model.CurrentTab.*
import com.sirekanyan.knigopis.repository.cache.COMMON_PREFS_NAME
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.about.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.books_page.*
import kotlinx.android.synthetic.main.notes_page.*
import kotlinx.android.synthetic.main.users_page.*
import retrofit2.HttpException

interface MainView {

    fun showAboutDialog()
    fun showPage(tab: CurrentTab)
    fun updateBooks(books: List<BookModel>)
    fun updateUsers(users: List<UserModel>)
    fun updateNotes(notes: List<NoteModel>)
    fun showBooksError(it: Throwable)
    fun showUsersError(it: Throwable)
    fun showNotesError(it: Throwable)
    fun showProgress()
    fun hideProgress()
    fun hideSwipeRefresh()
    fun showBookActions(book: BookDataModel)
    fun showBookDeleteDialog(book: BookDataModel)
    fun showBookDeleteError()
    fun showUserProfiles(title: String, items: List<UriItem>)

    interface Callbacks {
        fun onLoginOptionClicked()
        fun onProfileOptionClicked()
        fun onAboutOptionClicked()
        fun onDarkThemeOptionClicked(isChecked: Boolean)
        fun onAddBookClicked()
        fun onEditBookClicked(book: BookDataModel)
        fun onDeleteBookClicked(book: BookDataModel)
        fun onDeleteBookConfirmed(book: BookDataModel)
        fun onBookClicked(book: BookDataModel)
        fun onBookLongClicked(book: BookDataModel)
        fun onUserClicked(user: UserModel)
        fun onUserLongClicked(user: UserModel)
        fun onUserProfileClicked(uri: UriItem)
        fun onNoteClicked(note: NoteModel)
    }

}

class MainViewImpl(
    override val containerView: View,
    private val callbacks: MainView.Callbacks,
    private val dialogs: DialogFactory
) : MainView, LayoutContainer {

    private val context = containerView.context
    private val resources = context.resources
    private val booksAdapter = BooksAdapter(callbacks::onBookClicked, callbacks::onBookLongClicked)
    private val usersAdapter = UsersAdapter(callbacks::onUserClicked, callbacks::onUserLongClicked)
    private val notesAdapter = NotesAdapter(callbacks::onNoteClicked)

    init {
        toolbar.inflateMenu(R.menu.options)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.option_login -> {
                    callbacks.onLoginOptionClicked()
                    true
                }
                R.id.option_profile -> {
                    callbacks.onProfileOptionClicked()
                    true
                }
                R.id.option_about -> {
                    callbacks.onAboutOptionClicked()
                    true
                }
                R.id.option_dark_theme -> {
                    item.isChecked = !item.isChecked
                    callbacks.onDarkThemeOptionClicked(item.isChecked)
                    true
                }
                R.id.option_clear_cache -> {
                    context.cacheDir.deleteRecursively()
                    context.getSharedPreferences(COMMON_PREFS_NAME, MODE_PRIVATE)
                        .edit().clear().apply()
                    true
                }
                else -> false
            }
        }
        booksRecyclerView.adapter = booksAdapter
        usersRecyclerView.adapter = usersAdapter
        notesRecyclerView.adapter = notesAdapter
        booksRecyclerView.addItemDecoration(HeaderItemDecoration(StickyHeaderImpl(booksAdapter)))
        addBookButton.setOnClickListener {
            callbacks.onAddBookClicked()
        }
    }

    override fun showAboutDialog() {
        val dialogView = View.inflate(context, R.layout.about, null)
        dialogView.aboutAppVersion.text = BuildConfig.VERSION_NAME
        AlertDialog.Builder(context).setView(dialogView).show()
    }

    override fun showPage(tab: CurrentTab) {
        booksPage.show(tab == HOME_TAB)
        usersPage.show(tab == USERS_TAB)
        notesPage.show(tab == NOTES_TAB)
    }

    override fun updateBooks(books: List<BookModel>) {
        booksPlaceholder.show(books.isEmpty())
        booksErrorPlaceholder.hide()
        booksAdapter.submitList(books)
    }

    override fun updateUsers(users: List<UserModel>) {
        usersPlaceholder.show(users.isEmpty())
        usersErrorPlaceholder.hide()
        usersAdapter.submitList(users)
    }

    override fun updateNotes(notes: List<NoteModel>) {
        notesPlaceholder.show(notes.isEmpty())
        notesErrorPlaceholder.hide()
        notesAdapter.submitList(notes)
    }

    override fun showBooksError(it: Throwable) {
        handleError(it, booksPlaceholder, booksErrorPlaceholder, booksAdapter)
    }

    override fun showUsersError(it: Throwable) {
        handleError(it, usersPlaceholder, usersErrorPlaceholder, usersAdapter)
    }

    override fun showNotesError(it: Throwable) {
        handleError(it, notesPlaceholder, notesErrorPlaceholder, notesAdapter)
    }

    override fun showProgress() {
        if (!swipeRefresh.isRefreshing) {
            booksProgressBar.show()
        }
    }

    override fun hideProgress() {
        booksProgressBar.hide()
    }

    override fun hideSwipeRefresh() {
        swipeRefresh.isRefreshing = false
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

    override fun showBookDeleteError() {
        context.toast(R.string.books_error_delete)
    }

    override fun showUserProfiles(title: String, items: List<UriItem>) {
        val dialogItems: List<DialogItem> = items.map { uriItem ->
            createDialogItem(uriItem.title, uriItem.iconRes) {
                callbacks.onUserProfileClicked(uriItem)
            }
        }
        dialogs.showDialog(title, *dialogItems.toTypedArray())
    }

    private fun handleError(
        throwable: Throwable,
        placeholder: View,
        errorPlaceholder: TextView,
        adapter: RecyclerView.Adapter<*>
    ) {
        if (placeholder.isVisible || adapter.itemCount > 0) {
            context.toast(throwable.messageRes)
        } else {
            errorPlaceholder.setText(throwable.messageRes)
            errorPlaceholder.show()
        }
    }

    private val Throwable.messageRes
        get() = if (this is HttpException && code() == 401) {
            R.string.main_error_unauthorized
        } else {
            R.string.common_error_network
        }

}