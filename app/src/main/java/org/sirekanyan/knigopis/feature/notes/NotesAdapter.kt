package org.sirekanyan.knigopis.feature.notes

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.adapter.SimpleItemCallback
import org.sirekanyan.knigopis.common.extensions.inflate
import org.sirekanyan.knigopis.model.NoteModel

class NotesAdapter(
    private val onClick: (NoteModel) -> Unit
) : ListAdapter<NoteModel, NoteViewHolder>(SimpleItemCallback(NoteModel::id)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoteViewHolder(parent.inflate(R.layout.note), onClick)

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }

}