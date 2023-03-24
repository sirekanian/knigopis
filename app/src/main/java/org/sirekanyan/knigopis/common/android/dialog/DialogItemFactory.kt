package org.sirekanyan.knigopis.common.android.dialog

import org.sirekanyan.knigopis.common.android.IdStringResource
import org.sirekanyan.knigopis.common.android.PlainStringResource

fun createDialogItem(titleRes: Int, iconRes: Int, onClick: () -> Unit) =
    DialogItem(IdStringResource(titleRes), iconRes, onClick)

fun createDialogItem(title: String, iconRes: Int, onClick: () -> Unit) =
    DialogItem(PlainStringResource(title), iconRes, onClick)