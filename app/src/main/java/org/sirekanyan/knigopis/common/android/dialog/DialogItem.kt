package org.sirekanyan.knigopis.common.android.dialog

import androidx.annotation.DrawableRes
import org.sirekanyan.knigopis.common.android.StringResource

class DialogItem(
    val title: StringResource,
    @all:DrawableRes val iconRes: Int,
    val onClick: () -> Unit,
)