package org.sirekanyan.knigopis.model

import androidx.annotation.IdRes
import org.sirekanyan.knigopis.R

enum class LoginMode(@all:IdRes val itemId: Int) {

    LOGIN(R.id.option_log_in),
    SIGNUP(R.id.option_sign_up);

    companion object {
        fun getByItemId(@IdRes itemId: Int): LoginMode {
            return checkNotNull(entries.find { it.itemId == itemId })
        }
    }
}
