package org.sirekanyan.knigopis.feature.users

import org.sirekanyan.knigopis.common.android.dialog.DialogFactory
import org.sirekanyan.knigopis.common.android.dialog.DialogItem
import org.sirekanyan.knigopis.common.android.dialog.createDialogItem
import org.sirekanyan.knigopis.common.android.toast.CommonView
import org.sirekanyan.knigopis.common.extensions.hide
import org.sirekanyan.knigopis.common.extensions.keepOnTop
import org.sirekanyan.knigopis.common.extensions.show
import org.sirekanyan.knigopis.common.functions.handleError
import org.sirekanyan.knigopis.databinding.UsersPageBinding
import org.sirekanyan.knigopis.feature.ProgressView
import org.sirekanyan.knigopis.model.ProfileItem
import org.sirekanyan.knigopis.model.UserModel

interface UsersView : CommonView<UsersPageBinding>, ProgressView {

    fun updateUsers(users: List<UserModel>)
    fun showUsersError(throwable: Throwable)
    fun showUserProfiles(title: String, items: List<ProfileItem>)

    interface Callbacks {
        fun onUserClicked(user: UserModel)
        fun onUserLongClicked(user: UserModel)
        fun onUserProfileClicked(uri: ProfileItem)
        fun onUsersUpdated()
    }

}

class UsersViewImpl(
    override val binding: UsersPageBinding,
    private val callbacks: UsersView.Callbacks,
    private val progressView: ProgressView,
    private val dialogs: DialogFactory,
) : UsersView,
    ProgressView by progressView {

    private val usersRecyclerView = binding.usersRecyclerView
    private val usersPlaceholder = binding.usersPlaceholder
    private val usersErrorPlaceholder = binding.usersErrorPlaceholder

    private val usersAdapter = UsersAdapter(callbacks::onUserClicked, callbacks::onUserLongClicked)

    init {
        usersRecyclerView.adapter = usersAdapter
    }

    override fun updateUsers(users: List<UserModel>) {
        usersPlaceholder.show(users.isEmpty())
        usersErrorPlaceholder.hide()
        usersAdapter.submitList(users) {
            usersRecyclerView.keepOnTop()
        }
        callbacks.onUsersUpdated()
    }

    override fun showUsersError(throwable: Throwable) {
        handleError(throwable, usersPlaceholder, usersErrorPlaceholder, usersAdapter)
    }

    override fun showUserProfiles(title: String, items: List<ProfileItem>) {
        val dialogItems: List<DialogItem> = items.map { uriItem ->
            createDialogItem(uriItem.title, uriItem.iconRes) {
                callbacks.onUserProfileClicked(uriItem)
            }
        }
        dialogs.showDialog(title, *dialogItems.toTypedArray())
    }

}