package org.sirekanyan.knigopis.dependency

import android.app.Activity
import org.sirekanyan.knigopis.common.android.dialog.BottomSheetDialogFactory
import org.sirekanyan.knigopis.common.android.dialog.DialogFactory

fun Activity.provideDialogs(): DialogFactory =
    BottomSheetDialogFactory(this)