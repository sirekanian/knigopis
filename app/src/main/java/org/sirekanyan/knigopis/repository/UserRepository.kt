package org.sirekanyan.knigopis.repository

import org.sirekanyan.knigopis.common.android.NetworkChecker
import org.sirekanyan.knigopis.model.UserModel
import org.sirekanyan.knigopis.repository.cache.CacheKey
import org.sirekanyan.knigopis.repository.cache.CommonCache
import org.sirekanyan.knigopis.repository.cache.genericType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface UserRepository {

    fun observeUsers(): Flowable<List<UserModel>>

}

class UserRepositoryImpl(
    private val api: Endpoint,
    private val cache: CommonCache,
    private val organizer: UserOrganizer,
    networkChecker: NetworkChecker
) : CommonRepository<List<UserModel>>(networkChecker),
    UserRepository {

    override fun observeUsers() = observe()

    override fun loadFromNetwork(): Single<List<UserModel>> =
        api.getSubscriptions().map(organizer::organize)

    override fun findCached(): Maybe<List<UserModel>> =
        cache.find(CacheKey.USERS, genericType<List<UserModel>>())

    override fun saveToCache(data: List<UserModel>): Completable =
        cache.save(CacheKey.USERS, data)

}