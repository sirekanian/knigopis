package org.sirekanyan.knigopis.feature.notes

import org.sirekanyan.knigopis.common.BasePresenter
import org.sirekanyan.knigopis.common.extensions.io2main
import org.sirekanyan.knigopis.common.extensions.showProgressBar
import org.sirekanyan.knigopis.common.functions.logError
import org.sirekanyan.knigopis.feature.PagePresenter
import org.sirekanyan.knigopis.feature.PagesPresenter
import org.sirekanyan.knigopis.model.CurrentTab
import org.sirekanyan.knigopis.model.NoteModel
import org.sirekanyan.knigopis.repository.NoteRepository

interface NotesPresenter : PagePresenter {

    interface Router {
        fun openUserScreen(note: NoteModel)
    }

}

class NotesPresenterImpl(
    private val router: NotesPresenter.Router,
    private val noteRepository: NoteRepository
) : BasePresenter<NotesView>(),
    NotesPresenter,
    NotesView.Callbacks {

    lateinit var parent: PagesPresenter

    override fun refresh() {
        noteRepository.observeNotes()
            .io2main()
            .showProgressBar(view)
            .bind({ notes ->
                view.updateNotes(notes)
            }, {
                logError("cannot load notes", it)
                view.showNotesError(it)
            })
    }

    override fun onNoteClicked(note: NoteModel) {
        router.openUserScreen(note)
    }

    override fun onNotesUpdated() {
        parent.onPageUpdated(CurrentTab.NOTES_TAB)
    }

}