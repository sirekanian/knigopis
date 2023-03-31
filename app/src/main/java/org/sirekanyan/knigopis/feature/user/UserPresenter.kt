package org.sirekanyan.knigopis.feature.user

import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.BasePresenter
import org.sirekanyan.knigopis.common.Presenter
import org.sirekanyan.knigopis.common.android.ResourceProvider
import org.sirekanyan.knigopis.common.extensions.snackbar
import org.sirekanyan.knigopis.common.extensions.toast
import org.sirekanyan.knigopis.common.functions.createUserImageUrl
import org.sirekanyan.knigopis.common.functions.createUserPublicUrl
import org.sirekanyan.knigopis.common.functions.logError
import org.sirekanyan.knigopis.model.BookDataModel
import org.sirekanyan.knigopis.model.EditBookModel
import org.sirekanyan.knigopis.model.createDoneBook
import org.sirekanyan.knigopis.model.createTodoBook
import org.sirekanyan.knigopis.repository.AuthRepository

interface UserPresenter : Presenter {

    fun init()
    fun start()

    interface Router {
        fun onBackPressed()
        fun openBookScreen(book: EditBookModel)
        fun copyToClipboard(text: String)
    }

}

class UserPresenterImpl(
    private val router: UserPresenter.Router,
    private val interactor: UserInteractor,
    private val auth: AuthRepository,
    private val userId: String,
    private val userName: String,
    private val resources: ResourceProvider
) : BasePresenter<UserView>(),
    UserPresenter,
    UserView.Callbacks {

    override fun init() {
        view.setTitle(userName)
        view.setImage(createUserImageUrl(userId))
    }

    override fun start() {
        interactor.getBooks(userId)
            .doOnSubscribe { view.showProgress() }
            .doFinally { view.hideProgress() }
            .doOnSuccess { view.showBooks() }
            .doOnError { view.showError() }
            .bind({
                view.setBooks(it)
                onBooksLoaded()
            }, {
                logError("Cannot load user books", it)
            })
    }

    override fun onNavigationBackClicked() {
        router.onBackPressed()
    }

    override fun onCopyOptionClicked() {
        val link = createUserPublicUrl(userId)
        router.copyToClipboard(link)
        view.toast(R.string.user_info_copied, link)
    }

    override fun onUnsubscribeOptionClicked() {
        interactor.removeFriend(userId)
            .bind({}, {
                logError("Cannot unsubscribe", it)
                view.toast(R.string.user_error_unsubscribe)
            })
    }

    override fun onFabClicked() {
        interactor.addFriend(userId)
            .doOnSubscribe { view.showFab(false) }
            .doFinally { view.showFab(true) }
            .bind({
                view.disableFabClick()
                view.setFabSelected(true)
            }, {
                logError("Cannot update subscription", it)
                view.snackbar(R.string.common_error_network)
                view.setFabSelected(false)
            })
    }

    override fun onBookLongClicked(book: BookDataModel) {
        if (auth.isAuthorized()) {
            view.showActionsDialog(book.title, book.author, book)
        } else {
            view.toast(R.string.user_error_unauthorized)
        }
    }

    override fun onTodoActionClicked(book: BookDataModel) {
        val notes = resources.getString(R.string.book_notes_copied, userName)
        val todoBook = createTodoBook(book.title, book.author, notes)
        router.openBookScreen(todoBook)
    }

    override fun onDoneActionClicked(book: BookDataModel) {
        val doneBook = createDoneBook(book.title, book.author)
        router.openBookScreen(doneBook)
    }

    private fun onBooksLoaded() {
        interactor.isFriend(userId)
            .bind({ isSubscribed ->
                if (isSubscribed) {
                    view.showUnsubscribeOption()
                } else {
                    view.showFab(true)
                }
            }, {
                logError("Cannot check subscription", it)
            })
    }

}