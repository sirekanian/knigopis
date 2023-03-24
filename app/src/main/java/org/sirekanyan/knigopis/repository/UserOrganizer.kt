package org.sirekanyan.knigopis.repository

import org.sirekanyan.knigopis.model.UserModel
import org.sirekanyan.knigopis.model.dto.Subscription
import org.sirekanyan.knigopis.model.toUserModel
import org.sirekanyan.knigopis.repository.UserSorting.*

class UserOrganizer(private val config: Configuration) {

    fun organize(subscriptions: List<Subscription>): List<UserModel> =
        sort(subscriptions.map(Subscription::toUserModel))


    private fun sort(users: List<UserModel>): List<UserModel> =
        when (config.userSorting) {
            DEFAULT -> users
            BY_NAME -> users.sortedBy(UserModel::name)
            BY_COUNT -> {
                users.sortedWith(
                    compareByDescending(UserModel::booksCount)
                        .thenByDescending(UserModel::newBooksCount)
                )
            }
            BY_NEW_COUNT -> {
                users.sortedWith(
                    compareByDescending(UserModel::newBooksCount)
                        .thenByDescending(UserModel::booksCount)
                )
            }
        }

}