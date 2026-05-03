package org.sirekanyan.knigopis.feature.login

import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.BasePresenter
import org.sirekanyan.knigopis.common.Presenter
import org.sirekanyan.knigopis.common.extensions.toast
import org.sirekanyan.knigopis.common.functions.logError
import org.sirekanyan.knigopis.feature.login.LoginPresenter.Router
import org.sirekanyan.knigopis.model.LoginMode
import org.sirekanyan.knigopis.repository.AuthRepository

interface LoginPresenter : Presenter {

    val state: LoginPresenterState
    fun init(state: LoginPresenterState?)

    interface Router {

        fun close()
    }
}

class LoginPresenterImpl(
    private val router: Router,
    private val authRepository: AuthRepository,
) : BasePresenter<LoginView>(),
    LoginPresenter,
    LoginView.Callbacks {

    private var mode = LoginMode.LOGIN

    override val state get() = LoginPresenterState(mode)

    override fun init(state: LoginPresenterState?) {
        state?.mode?.let { mode = it }
        updateViews()
    }

    override fun onLoginMenuClicked() {
        mode = LoginMode.LOGIN
        updateViews()
    }

    override fun onSignupMenuClicked() {
        mode = LoginMode.SIGNUP
        updateViews()
    }

    private fun updateViews() {
        val isLogIn = mode == LoginMode.LOGIN
        view.setTitle(if (isLogIn) R.string.login_title else R.string.signup_title)
        view.showPasswordRepeat(!isLogIn)
        view.setButtonText(if (isLogIn) R.string.login_button else R.string.signup_button)
        view.setButtonColor(if (isLogIn) R.color.knigopis_color_primary else R.color.knigopis_color_secondary)
        view.showMenu(if (isLogIn) R.id.option_sign_up else R.id.option_log_in)
    }

    override fun onPrimaryButtonClicked(
        username: String,
        password: String,
        passwordRepeat: String,
    ) {
        val mode = mode
        if (mode == LoginMode.SIGNUP && password != passwordRepeat) {
            view.toast(R.string.signup_error_passwords_do_not_match)
            return
        }
        when (mode) {
            LoginMode.LOGIN -> authRepository.login(username, password)
            LoginMode.SIGNUP -> authRepository.register(username, password)
        }.bind(
            onSuccess = router::close,
            onError = { exception ->
                logError("Cannot authorize ($mode)", exception)
                view.toast(R.string.common_error_unknown)
            },
        )
    }

    override fun onBackClicked() {
        router.close()
    }
}
