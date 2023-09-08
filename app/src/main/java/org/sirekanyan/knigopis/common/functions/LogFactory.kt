package org.sirekanyan.knigopis.common.functions

import android.util.Log
import org.sirekanyan.knigopis.CrashReporter

private const val TAG = "Knigopis"

@Suppress("unused")
fun logWarn(message: String) = Log.w(TAG, message)

fun logError(message: String, throwable: Throwable?): Int {
    CrashReporter.handleException(throwable ?: Exception(message))
    return Log.e(TAG, message, throwable)
}
