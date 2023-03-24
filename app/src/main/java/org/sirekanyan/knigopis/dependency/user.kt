package com.sirekanyan.knigopis.dependency

import com.sirekanyan.knigopis.common.extensions.app
import com.sirekanyan.knigopis.feature.user.*

fun UserActivity.providePresenter(id: String, name: String): UserPresenter {
    val interactor = UserInteractorImpl(app.endpoint, app.resourceProvider)
    return UserPresenterImpl(this, interactor, id, name, app.resourceProvider).also { presenter ->
        presenter.view = UserViewImpl(binding, presenter, provideDialogs())
    }
}