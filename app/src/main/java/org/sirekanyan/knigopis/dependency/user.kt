package org.sirekanyan.knigopis.dependency

import org.sirekanyan.knigopis.common.extensions.app
import org.sirekanyan.knigopis.feature.user.*

fun UserActivity.providePresenter(id: String, name: String): UserPresenter {
    val interactor = UserInteractorImpl(app.endpoint, app.resourceProvider)
    return UserPresenterImpl(this, interactor, app.authRepository, id, name, app.resourceProvider)
        .also { presenter -> presenter.view = UserViewImpl(binding, presenter, provideDialogs()) }
}