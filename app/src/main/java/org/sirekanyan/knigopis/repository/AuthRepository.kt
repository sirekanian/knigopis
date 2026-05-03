package org.sirekanyan.knigopis.repository

import io.reactivex.Completable
import io.reactivex.Single
import org.sirekanyan.knigopis.common.extensions.io2main
import org.sirekanyan.knigopis.model.dto.Credentials
import org.sirekanyan.knigopis.model.dto.UserToSend

interface AuthRepository {
    fun login(username: String, password: String): Completable
    fun register(username: String, password: String): Completable
    fun isAuthorized(): Boolean
    fun saveToken(token: String)
    fun authorize(): Completable
}

class AuthRepositoryImpl(
    private val api: Endpoint,
    private val storage: TokenStorage,
) : AuthRepository {

    override fun login(username: String, password: String): Completable {
        return perform(api::login, username, password)
    }

    override fun register(username: String, password: String): Completable {
        return perform(api::register, username, password)
    }

    private fun perform(
        action: (UserToSend) -> Single<Credentials>,
        username: String,
        password: String,
    ): Completable {
        return action(UserToSend(username, password, "en"))
            .io2main()
            .doOnSuccess {
                storage.accessToken = it.accessToken
            }
            .ignoreElement()
    }

    override fun isAuthorized(): Boolean =
        storage.accessToken != null

    override fun saveToken(token: String) {
        storage.token = token
    }

    override fun authorize(): Completable {
        val token = storage.token
        return if (token == null || isAuthorized()) {
            Completable.complete()
        } else {
            api.getCredentials(token)
                .io2main()
                .doOnSuccess {
                    storage.accessToken = it.accessToken
                }
                .ignoreElement()
        }
    }

}