package org.sirekanyan.knigopis.feature.login

import android.os.Bundle
import org.sirekanyan.knigopis.model.LoginMode

private const val LOGIN_STATE_KEY = "login_state"

fun Bundle.getLoginState(): LoginPresenterState {
    return LoginPresenterState(LoginMode.getByItemId(getInt(LOGIN_STATE_KEY)))
}

fun Bundle.saveLoginState(state: LoginPresenterState) {
    putInt(LOGIN_STATE_KEY, state.mode.itemId)
}

class LoginPresenterState(val mode: LoginMode)
