package com.sirekanyan.knigopis.feature.notes

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.android.adapter.CommonViewHolder
import com.sirekanyan.knigopis.common.extensions.setCircleImage
import com.sirekanyan.knigopis.common.extensions.setSquareImage
import com.sirekanyan.knigopis.model.NoteModel

class NoteViewHolder(
    containerView: View,
    private val onClick: (NoteModel) -> Unit,
) : CommonViewHolder<NoteModel>(containerView) {

    private val bookImage = containerView.findViewById<ImageView>(R.id.bookImage)
    private val bookTitle = containerView.findViewById<TextView>(R.id.bookTitle)
    private val bookAuthor = containerView.findViewById<TextView>(R.id.bookAuthor)
    private val userNotes = containerView.findViewById<TextView>(R.id.userNotes)
    private val userDate = containerView.findViewById<TextView>(R.id.userDate)
    private val userNickname = containerView.findViewById<TextView>(R.id.userNickname)
    private val userImage = containerView.findViewById<ImageView>(R.id.userImage)

    init {
        containerView.setOnClickListener {
            model?.let { note ->
                onClick(note)
            }
        }
    }

    override fun onBind(position: Int, model: NoteModel) {
        bookImage.setSquareImage(model.bookImage)
        bookTitle.text = model.bookTitle
        bookAuthor.text = model.bookAuthor
        userNotes.text = model.noteContent
        userDate.text = model.noteDate
        userNickname.text = model.userName
        userImage.setCircleImage(model.userImage)
    }

}