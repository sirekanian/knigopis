package org.sirekanyan.knigopis.repository

import org.sirekanyan.knigopis.common.android.NetworkChecker
import org.sirekanyan.knigopis.model.NoteModel
import org.sirekanyan.knigopis.model.dto.Note
import org.sirekanyan.knigopis.model.toNoteModel
import org.sirekanyan.knigopis.repository.cache.CacheKey
import org.sirekanyan.knigopis.repository.cache.CommonCache
import org.sirekanyan.knigopis.repository.cache.genericType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface NoteRepository {

    fun observeNotes(): Flowable<List<NoteModel>>

}

class NoteRepositoryImpl(
    private val api: Endpoint,
    private val cache: CommonCache,
    networkChecker: NetworkChecker,
) : CommonRepository<List<NoteModel>>(networkChecker),
    NoteRepository {

    override fun observeNotes() = observe()

    override fun loadFromNetwork(): Single<List<NoteModel>> =
        api.getLatestBooksWithNotes().map { it.values.map(Note::toNoteModel) }

    override fun findCached(): Maybe<List<NoteModel>> =
        cache.find(CacheKey.NOTES, genericType<List<NoteModel>>())

    override fun saveToCache(data: List<NoteModel>): Completable =
        cache.save(CacheKey.NOTES, data)

}