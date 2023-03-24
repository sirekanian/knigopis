package com.sirekanyan.knigopis.feature.users

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.android.adapter.CommonViewHolder
import com.sirekanyan.knigopis.common.extensions.setCircleImage
import com.sirekanyan.knigopis.model.UserModel

class UserViewHolder(
    containerView: View,
    private val onClick: (UserModel) -> Unit,
    private val onLongClick: (UserModel) -> Unit,
) : CommonViewHolder<UserModel>(containerView) {

    private val userImage = containerView.findViewById<ImageView>(R.id.userImage)
    private val userNickname = containerView.findViewById<TextView>(R.id.userNickname)
    private val totalBooksCount = containerView.findViewById<TextView>(R.id.totalBooksCount)
    private val newBooksCount = containerView.findViewById<TextView>(R.id.newBooksCount)

    init {
        containerView.setOnClickListener {
            model?.let { user ->
                onClick(user)
            }
        }
        containerView.setOnLongClickListener {
            model?.let { user ->
                onLongClick(user)
            }
            true
        }
    }

    override fun onBind(position: Int, model: UserModel) {
        userImage.setCircleImage(model.image)
        userNickname.text = model.name
        totalBooksCount.text = model.booksCount?.toString()
        newBooksCount.text = model.newBooksCountFormatted
    }

}