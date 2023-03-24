package org.sirekanyan.knigopis.common.extensions

import android.app.Activity
import android.view.ViewGroup
import org.sirekanyan.knigopis.App

val Activity.app get() = application as App

fun Activity.getRootView(): ViewGroup =
    findViewById(android.R.id.content)
