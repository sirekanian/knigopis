package org.sirekanyan.knigopis.feature.profile

import org.sirekanyan.knigopis.common.extensions.io2main
import org.sirekanyan.knigopis.model.BookDataModel
import org.sirekanyan.knigopis.model.ProfileModel
import org.sirekanyan.knigopis.model.dto.User
import org.sirekanyan.knigopis.model.toProfile
import org.sirekanyan.knigopis.model.toProfileModel
import org.sirekanyan.knigopis.repository.BookRepository
import org.sirekanyan.knigopis.repository.Endpoint
import org.sirekanyan.knigopis.repository.TokenStorage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Observables
import java.util.concurrent.TimeUnit

interface ProfileInteractor {

    fun getProfile(): Single<ProfileModel>
    fun getBooks(): Observable<BookDataModel>
    fun getBooksForExport(): Single<String>
    fun updateProfile(profile: ProfileModel): Completable
    fun logout()

}

class ProfileInteractorImpl(
    private val api: Endpoint,
    private val bookRepository: BookRepository,
    private val tokenStorage: TokenStorage,
    private val exportFormatter: ExportFormatter,
) : ProfileInteractor {

    override fun getProfile(): Single<ProfileModel> =
        api.getProfile()
            .map(User::toProfileModel)
            .io2main()

    override fun getBooks(): Observable<BookDataModel> =
        bookRepository.findCached()
            .toSingle(listOf())
            .map { it.filterIsInstance<BookDataModel>() }
            .map { it.shuffled() }
            .flatMapObservable {
                Observables.zip(
                    Observable.fromIterable(it),
                    Observable.interval(5, TimeUnit.MILLISECONDS)
                )
            }
            .map { (book) -> book }
            .io2main()

    override fun getBooksForExport(): Single<String> =
        bookRepository.findCached().toSingle(listOf()).map {
            exportFormatter.formatBooksForExport(it.filterIsInstance<BookDataModel>())
        }

    override fun updateProfile(profile: ProfileModel): Completable =
        api.updateProfile(profile.id, profile.toProfile())
            .io2main()

    override fun logout() {
        tokenStorage.clearTokens()
    }

}