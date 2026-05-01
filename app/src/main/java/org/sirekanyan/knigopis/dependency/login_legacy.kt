package org.sirekanyan.knigopis.dependency

import org.sirekanyan.knigopis.feature.login.u.LoginActivity
import org.sirekanyan.knigopis.feature.login.u.LoginPresenter
import org.sirekanyan.knigopis.feature.login.u.LoginPresenterImpl
import org.sirekanyan.knigopis.feature.login.u.LoginViewImpl

fun LoginActivity.providePresenter(): LoginPresenter =
    LoginPresenterImpl(this).also { presenter ->
        presenter.view = LoginViewImpl(binding, presenter)
    }