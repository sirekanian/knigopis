package org.sirekanyan.knigopis.dependency

import org.sirekanyan.knigopis.feature.login.LoginActivity
import org.sirekanyan.knigopis.feature.login.LoginPresenter
import org.sirekanyan.knigopis.feature.login.LoginPresenterImpl
import org.sirekanyan.knigopis.feature.login.LoginViewImpl

fun LoginActivity.providePresenter(): LoginPresenter =
    LoginPresenterImpl(this).also { presenter ->
        presenter.view = LoginViewImpl(binding, presenter)
    }