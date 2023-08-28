package org.sirekanyan.knigopis

import android.app.Application
import android.content.Context
import org.acra.ACRA
import org.acra.config.CoreConfigurationBuilder
import org.acra.config.HttpSenderConfigurationBuilder
import org.acra.sender.HttpSender
import org.sirekanyan.knigopis.dependency.*

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
        if (config.crashReportEnabled) {
            initCrashReporting()
        }
    }

    private fun initCrashReporting() {
        val httpSenderConfig = HttpSenderConfigurationBuilder()
            .withUri(BuildConfig.ACRA_URI)
            .withHttpMethod(HttpSender.Method.POST)
            .withBasicAuthLogin(BuildConfig.ACRA_LOGIN)
            .withBasicAuthPassword(BuildConfig.ACRA_PASSWORD)
            .withEnabled(BuildConfig.ACRA_ENABLED)
            .build()
        val config = CoreConfigurationBuilder()
            .withBuildConfigClass(BuildConfig::class.java)
            .withPluginConfigurations(httpSenderConfig)
            .build()
        ACRA.init(this, config)
    }

    override fun onCreate() {
        super.onCreate()
        config.theme.setup()
    }

}