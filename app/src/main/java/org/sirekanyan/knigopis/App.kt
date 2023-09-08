package org.sirekanyan.knigopis

import android.app.Application
import android.content.Context
import org.sirekanyan.knigopis.dependency.*

@Suppress("KotlinConstantConditions")
fun isPlayFlavor(): Boolean =
    BuildConfig.FLAVOR == "play"

class App : Application() {

    val config by lazy(::provideConfig)
    val resourceProvider by lazy(::provideResources)
    val tokenStorage by lazy(::provideTokenStorage)
    val authRepository by lazy(::provideAuthRepository)
    val bookRepository by lazy(::provideBookRepository)
    val userRepository by lazy(::provideUserRepository)
    val noteRepository by lazy(::provideNoteRepository)
    val networkChecker by lazy(::provideNetworkChecker)
    val endpoint by lazy(::provideEndpoint)
    val cache by lazy(::provideCache)
    val gson by lazy(::provideGson)

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        CrashReporter.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        config.theme.setup()
    }

}