package com.sirekanyan.knigopis.common.android.dialog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.extensions.inflate

interface DialogFactory {

    fun showDialog(title: String, vararg items: DialogItem)

}

class BottomSheetDialogFactory(private val context: Context) : DialogFactory {

    override fun showDialog(title: String, vararg items: DialogItem) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottom_sheet_dialog_view)
        dialog.findViewById<TextView>(R.id.bottomSheetTitle)!!.text = title
        val container = dialog.findViewById<ViewGroup>(R.id.bottomSheetContainer)!!
        items.forEach { item ->
            val itemView = container.inflate<View>(R.layout.bottom_sheet_dialog_item)
            itemView.findViewById<ImageView>(R.id.bottomSheetItemIcon).setImageResource(item.iconRes)
            item.title.setValueTo(itemView.findViewById(R.id.bottomSheetItemText))
            itemView.setOnClickListener {
                item.onClick()
                dialog.dismiss()
            }
            container.addView(itemView)
        }
        dialog.show()
    }

}