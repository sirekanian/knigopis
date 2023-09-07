package org.sirekanyan.knigopis.common.extensions

import android.app.Activity
import org.sirekanyan.knigopis.App

val Activity.app get() = application as App
