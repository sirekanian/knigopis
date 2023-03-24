package org.sirekanyan.knigopis.feature.users

import androidx.recyclerview.widget.DiffUtil
import org.sirekanyan.knigopis.model.UserModel

class UserItemCallback : DiffUtil.ItemCallback<UserModel>() {

    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
        oldItem.name == newItem.name &&
                oldItem.booksCount == newItem.booksCount &&
                oldItem.newBooksCount == newItem.newBooksCount

}