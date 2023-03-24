package org.sirekanyan.knigopis.common.android.header

import android.view.View
import android.widget.TextView
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.adapter.HeadedAdapter
import org.sirekanyan.knigopis.common.extensions.showNow
import org.sirekanyan.knigopis.model.BookModel

class StickyHeaderImpl(private val adapter: HeadedAdapter<BookModel>) : StickyHeader {

    override fun getHeaderPositionForItem(itemPosition: Int) = itemPosition

    override fun getHeaderLayout(headerPosition: Int) = R.layout.header

    override fun isHeader(itemPosition: Int) = adapter.getModelByPosition(itemPosition).isHeader

    override fun bindHeaderData(header: View, headerPosition: Int) {
        val group = adapter.getModelByPosition(headerPosition).group
        header.findViewById<TextView>(R.id.headerTitle).text = group.title
        header.findViewById<TextView>(R.id.headerCount).text = group.count
        header.findViewById<View>(R.id.headerBottomDivider).showNow()
    }

}