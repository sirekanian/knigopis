package com.sirekanyan.knigopis

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sirekanyan.knigopis.common.NetworkChecker
import com.sirekanyan.knigopis.common.NetworkCheckerImpl
import com.sirekanyan.knigopis.common.ResourceProvider
import com.sirekanyan.knigopis.common.ResourceProviderImpl
import com.sirekanyan.knigopis.common.view.dialog.BottomSheetDialogFactory
import com.sirekanyan.knigopis.common.view.dialog.DialogFactory
import com.sirekanyan.knigopis.feature.user.UserInteractor
import com.sirekanyan.knigopis.feature.user.UserInteractorImpl
import com.sirekanyan.knigopis.repository.*
import com.sirekanyan.knigopis.repository.api.Endpoint
import com.sirekanyan.knigopis.repository.cache.*
import com.sirekanyan.knigopis.repository.cache.common.CommonCache
import com.sirekanyan.knigopis.repository.cache.common.CommonCacheImpl
import com.sirekanyan.knigopis.repository.model.FinishedBook
import com.sirekanyan.knigopis.repository.model.PlannedBook
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.context.Context
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val MAIN_API_URL = "http://api.knigopis.com"
private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

val appModule = applicationContext {
    bean {
        BookRepositoryImpl(
            get(),
            get(),
            get(),
            get("planned"),
            get("finished"),
            get()
        ) as BookRepository
    }
    bean { SubscriptionRepositoryImpl(get(), get(), get(), get()) as SubscriptionRepository }
    bean { NoteRepositoryImpl(get(), get(), get()) as NoteRepository }
    bean { KAuthImpl(get(), get()) as KAuth }
    bean { createMainEndpoint(get()) }
    bean("planned") { PlannedBookOrganizerImpl(get(), get()) as BookOrganizer<PlannedBook> }
    bean("finished") { FinishedBookPrepareImpl(get()) as BookOrganizer<FinishedBook> }
    bean { ConfigurationImpl(get()) as Configuration }
    bean { ResourceProviderImpl(get()) as ResourceProvider }
    bean { NetworkCheckerImpl(get()) as NetworkChecker }
    bean { BookCacheImpl(get()) as BookCache }
    bean { SubscriptionCacheImpl(get()) as SubscriptionCache }
    bean { NoteCacheImpl(get()) as NoteCache }
    bean { CommonCacheImpl(get(), get()) as CommonCache }
    bean { GsonBuilder().setDateFormat(DATE_FORMAT).create() }
    factory { BottomSheetDialogFactory(it["activity"]) as DialogFactory }
    userModule()
}

private fun Context.userModule() {
    bean { UserInteractorImpl(get(), get()) as UserInteractor }
}

private fun createMainEndpoint(gson: Gson) =
    Retrofit.Builder()
        .baseUrl(MAIN_API_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(gson)
        )
        .client(
            OkHttpClient.Builder()
                .setDebugEnabled(BuildConfig.DEBUG)
                .build()
        )
        .build()
        .create(Endpoint::class.java)

private fun OkHttpClient.Builder.setDebugEnabled(debugEnabled: Boolean) =
    apply {
        if (debugEnabled) {
            addNetworkInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            })
        }
    }