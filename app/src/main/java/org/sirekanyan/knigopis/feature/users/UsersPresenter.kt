package org.sirekanyan.knigopis.feature.users

import android.net.Uri
import org.sirekanyan.knigopis.common.BasePresenter
import org.sirekanyan.knigopis.common.android.ResourceProvider
import org.sirekanyan.knigopis.common.extensions.io2main
import org.sirekanyan.knigopis.common.extensions.showProgressBar
import org.sirekanyan.knigopis.common.extensions.toUriOrNull
import org.sirekanyan.knigopis.common.functions.logError
import org.sirekanyan.knigopis.feature.PagePresenter
import org.sirekanyan.knigopis.feature.PagesPresenter
import org.sirekanyan.knigopis.model.CurrentTab
import org.sirekanyan.knigopis.model.ProfileItem
import org.sirekanyan.knigopis.model.UserModel
import org.sirekanyan.knigopis.repository.UserRepository

interface UsersPresenter : PagePresenter {

    interface Router {
        fun openUserScreen(user: UserModel)
        fun openWebPage(uri: Uri)
    }

}

class UsersPresenterImpl(
    private val router: UsersPresenter.Router,
    private val userRepository: UserRepository,
    private val resources: ResourceProvider,
) : BasePresenter<UsersView>(),
    UsersPresenter,
    UsersView.Callbacks {

    lateinit var parent: PagesPresenter

    override fun refresh() {
        userRepository.observeUsers()
            .io2main()
            .showProgressBar(view)
            .bind({ users ->
                view.updateUsers(users)
            }, {
                logError("cannot load users", it)
                view.showUsersError(it)
            })
    }

    override fun onUserClicked(user: UserModel) {
        router.openUserScreen(user)
    }

    override fun onUserLongClicked(user: UserModel) {
        val uriItems = user.profiles
            .mapNotNull(String::toUriOrNull)
            .map { ProfileItem(it, resources) }
            .distinctBy(ProfileItem::title)
        view.showUserProfiles(user.name, uriItems)
    }

    override fun onUserProfileClicked(uri: ProfileItem) {
        router.openWebPage(uri.uri)
    }

    override fun onUsersUpdated() {
        parent.onPageUpdated(CurrentTab.USERS_TAB)
    }

}