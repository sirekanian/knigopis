package org.sirekanyan.knigopis.common.functions

import android.util.Log

private const val TAG = "Knigopis"

@Suppress("unused")
fun logWarn(message: String) = Log.w(TAG, message)

fun logError(message: String, throwable: Throwable?): Int {
    // todo: crash reporting
    // ACRA.errorReporter.handleException(throwable)
    return Log.e(TAG, message, throwable)
}
