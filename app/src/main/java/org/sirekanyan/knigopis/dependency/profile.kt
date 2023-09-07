package org.sirekanyan.knigopis.dependency

import org.sirekanyan.knigopis.common.extensions.app
import org.sirekanyan.knigopis.feature.profile.*

fun ProfileActivity.providePresenter(): ProfilePresenter {
    val interactor =
        ProfileInteractorImpl(
            app.endpoint,
            app.bookRepository,
            app.tokenStorage,
            ExportFormatter(app.resourceProvider),
        )
    return ProfilePresenterImpl(this, interactor).also { presenter ->
        presenter.view = ProfileViewImpl(binding, presenter)
    }
}