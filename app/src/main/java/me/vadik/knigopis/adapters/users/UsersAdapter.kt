package me.vadik.knigopis.adapters.users

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import me.vadik.knigopis.R
import me.vadik.knigopis.inflate
import me.vadik.knigopis.model.subscription.Subscription

class UsersAdapter(
    private val users: List<Subscription>
) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = parent.inflate(R.layout.user)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        with(user.subUser) {
            holder.avatarUrl = photo
            val booksRead = "$booksCount (+${booksCount - user.lastBooksCount})"
            holder.nickname = nickname + " // " + booksRead
            holder.profile = profile ?: identity ?: ""
        }
    }
}