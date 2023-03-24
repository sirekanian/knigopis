package org.sirekanyan.knigopis.feature.books

import android.view.View
import android.widget.TextView
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.adapter.CommonViewHolder
import org.sirekanyan.knigopis.model.BookHeaderModel
import org.sirekanyan.knigopis.model.BookModel

class BookHeaderViewHolder(containerView: View) : CommonViewHolder<BookModel>(containerView) {

    private val headerTitle = containerView.findViewById<TextView>(R.id.headerTitle)
    private val headerCount = containerView.findViewById<TextView>(R.id.headerCount)
    private val headerDivider = containerView.findViewById<View>(R.id.headerDivider)

    override fun onBind(position: Int, model: BookModel) {
        val header = model as BookHeaderModel
        headerTitle.text = header.title
        headerCount.text = header.count
        headerDivider.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
    }

}