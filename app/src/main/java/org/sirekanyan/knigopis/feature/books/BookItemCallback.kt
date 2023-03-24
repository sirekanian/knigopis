package org.sirekanyan.knigopis.feature.books

import androidx.recyclerview.widget.DiffUtil
import org.sirekanyan.knigopis.model.BookDataModel
import org.sirekanyan.knigopis.model.BookHeaderModel
import org.sirekanyan.knigopis.model.BookModel

class BookItemCallback : DiffUtil.ItemCallback<BookModel>() {

    override fun areItemsTheSame(oldItem: BookModel, newItem: BookModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BookModel, newItem: BookModel) =
        when {
            oldItem is BookHeaderModel && newItem is BookHeaderModel -> {
                oldItem.title == newItem.title
                        && oldItem.count == newItem.count
            }
            oldItem is BookDataModel && newItem is BookDataModel -> {
                oldItem.title == newItem.title
                        && oldItem.author == newItem.author
                        && oldItem.priority == newItem.priority
            }
            else -> false
        }

}