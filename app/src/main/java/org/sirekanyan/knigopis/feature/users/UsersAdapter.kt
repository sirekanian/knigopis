package org.sirekanyan.knigopis.feature.users

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.extensions.inflate
import org.sirekanyan.knigopis.model.UserModel

class UsersAdapter(
    private val onClick: (UserModel) -> Unit,
    private val onLongClick: (UserModel) -> Unit,
) : ListAdapter<UserModel, UserViewHolder>(UserItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserViewHolder(parent.inflate(R.layout.user), onClick, onLongClick)

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }

}