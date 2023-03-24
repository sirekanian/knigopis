package org.sirekanyan.knigopis.feature.notes

import org.sirekanyan.knigopis.common.android.toast.CommonView
import org.sirekanyan.knigopis.common.extensions.hide
import org.sirekanyan.knigopis.common.extensions.keepOnTop
import org.sirekanyan.knigopis.common.extensions.show
import org.sirekanyan.knigopis.common.functions.handleError
import org.sirekanyan.knigopis.databinding.NotesPageBinding
import org.sirekanyan.knigopis.feature.ProgressView
import org.sirekanyan.knigopis.model.NoteModel

interface NotesView : CommonView<NotesPageBinding>, ProgressView {

    fun updateNotes(notes: List<NoteModel>)
    fun showNotesError(throwable: Throwable)

    interface Callbacks {
        fun onNoteClicked(note: NoteModel)
        fun onNotesUpdated()
    }

}

class NotesViewImpl(
    override val binding: NotesPageBinding,
    private val callbacks: NotesView.Callbacks,
    private val progressView: ProgressView,
) : NotesView,
    ProgressView by progressView {

    private val notesRecyclerView = binding.notesRecyclerView
    private val notesPlaceholder = binding.notesPlaceholder
    private val notesErrorPlaceholder = binding.notesErrorPlaceholder

    private val notesAdapter = NotesAdapter(callbacks::onNoteClicked)

    init {
        notesRecyclerView.adapter = notesAdapter
    }

    override fun updateNotes(notes: List<NoteModel>) {
        notesPlaceholder.show(notes.isEmpty())
        notesErrorPlaceholder.hide()
        notesAdapter.submitList(notes) {
            notesRecyclerView.keepOnTop()
        }
        callbacks.onNotesUpdated()
    }

    override fun showNotesError(throwable: Throwable) {
        handleError(throwable, notesPlaceholder, notesErrorPlaceholder, notesAdapter)
    }

}