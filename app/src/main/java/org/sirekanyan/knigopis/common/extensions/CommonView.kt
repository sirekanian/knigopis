package org.sirekanyan.knigopis.common.extensions

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.sirekanyan.knigopis.common.android.ResourceProvider
import org.sirekanyan.knigopis.common.android.toast.CommonView

val CommonView<*>.containerView: View
    get() = binding.root

val CommonView<*>.context: Context
    get() = containerView.context

val CommonView<*>.resources: ResourceProvider
    get() = containerView.resourceProvider

fun CommonView<*>.toast(messageId: Int, vararg args: Any) {
    context.showToast(messageId, *args)
}

fun CommonView<*>.toast(messageId: Int) {
    context.showToast(messageId)
}

fun CommonView<*>.snackbar(messageId: Int) {
    Snackbar.make(containerView, messageId, Snackbar.LENGTH_LONG).show()
}