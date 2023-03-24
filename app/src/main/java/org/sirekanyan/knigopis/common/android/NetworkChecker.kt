package org.sirekanyan.knigopis.common.android

import android.app.Application
import org.sirekanyan.knigopis.common.extensions.systemConnectivityManager

interface NetworkChecker {

    fun isNetworkAvailable(): Boolean

}

class NetworkCheckerImpl(app: Application) : NetworkChecker {

    private val connectivityManager = app.systemConnectivityManager

    @Suppress("DEPRECATION")
    override fun isNetworkAvailable(): Boolean =
        connectivityManager.activeNetworkInfo?.isConnected ?: false

}