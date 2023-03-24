package org.sirekanyan.knigopis.dependency

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.sirekanyan.knigopis.App
import org.sirekanyan.knigopis.BuildConfig
import org.sirekanyan.knigopis.DATE_FORMAT
import org.sirekanyan.knigopis.MAIN_API
import org.sirekanyan.knigopis.common.android.NetworkChecker
import org.sirekanyan.knigopis.common.android.NetworkCheckerImpl
import org.sirekanyan.knigopis.common.android.ResourceProvider
import org.sirekanyan.knigopis.common.android.ResourceProviderImpl
import org.sirekanyan.knigopis.model.BookDataModel
import org.sirekanyan.knigopis.model.BookHeaderModel
import org.sirekanyan.knigopis.model.BookModel
import org.sirekanyan.knigopis.repository.*
import org.sirekanyan.knigopis.repository.cache.CommonCache
import org.sirekanyan.knigopis.repository.cache.CommonCacheImpl
import org.sirekanyan.knigopis.repository.cache.HeadedModelDeserializer
import org.sirekanyan.knigopis.repository.network.AuthInterceptor
import org.sirekanyan.knigopis.repository.network.CookieStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun App.provideConfig(): Configuration =
    ConfigurationImpl(this)

fun App.provideResources(): ResourceProvider =
    ResourceProviderImpl(this)

fun App.provideTokenStorage(): TokenStorage =
    TokenStorageImpl(this)

fun App.provideAuthRepository(): AuthRepository =
    AuthRepositoryImpl(endpoint, tokenStorage)

fun App.provideBookRepository(): BookRepository {
    val plannedOrganizer = PlannedBookOrganizerImpl(resourceProvider, config)
    val finishedOrganizer = FinishedBookOrganizerImpl(resourceProvider, config)
    return BookRepositoryImpl(endpoint, cache, plannedOrganizer, finishedOrganizer, networkChecker)
}

fun App.provideUserRepository(): UserRepository {
    val organizer = UserOrganizer(config)
    return UserRepositoryImpl(endpoint, cache, organizer, networkChecker)
}

fun App.provideNoteRepository(): NoteRepository =
    NoteRepositoryImpl(endpoint, cache, networkChecker)

fun App.provideNetworkChecker(): NetworkChecker =
    NetworkCheckerImpl(this)

fun App.provideEndpoint(): Endpoint =
    Retrofit.Builder()
        .baseUrl(MAIN_API)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(
            OkHttpClient.Builder()
                .cookieJar(CookieStorage)
                .addInterceptor(AuthInterceptor(tokenStorage))
                .setDebugEnabled(BuildConfig.DEBUG)
                .build()
        )
        .build()
        .create(Endpoint::class.java)

fun App.provideCache(): CommonCache =
    CommonCacheImpl(this, gson)

fun provideGson(): Gson =
    GsonBuilder().registerTypeAdapter(
        BookModel::class.java,
        HeadedModelDeserializer<BookModel>(
            BookHeaderModel::class.java,
            BookDataModel::class.java
        )
    )
        .setDateFormat(DATE_FORMAT)
        .create()

private fun OkHttpClient.Builder.setDebugEnabled(debugEnabled: Boolean) =
    apply {
        if (debugEnabled) {
            addNetworkInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            })
        }
    }