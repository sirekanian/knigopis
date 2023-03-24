package com.sirekanyan.knigopis.feature.user

import android.view.View
import android.widget.TextView
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.android.adapter.CommonViewHolder
import com.sirekanyan.knigopis.common.extensions.showNow
import com.sirekanyan.knigopis.model.BookHeaderModel
import com.sirekanyan.knigopis.model.BookModel

class UserBookHeaderViewHolder(containerView: View) : CommonViewHolder<BookModel>(containerView) {

    private val headerTitle = containerView.findViewById<TextView>(R.id.headerTitle)
    private val headerCount = containerView.findViewById<TextView>(R.id.headerCount)
    private val headerDivider = containerView.findViewById<View>(R.id.headerDivider)

    override fun onBind(position: Int, model: BookModel) {
        val header = model as BookHeaderModel
        headerTitle.text = header.title
        headerCount.text = header.count
        headerDivider.showNow(position > 0)
    }

}