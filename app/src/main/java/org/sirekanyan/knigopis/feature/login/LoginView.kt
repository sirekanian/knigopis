package org.sirekanyan.knigopis.feature.login

import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.view.forEach
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.toast.CommonView
import org.sirekanyan.knigopis.common.extensions.context
import org.sirekanyan.knigopis.common.extensions.showNow
import org.sirekanyan.knigopis.databinding.LoginActivityBinding

interface LoginView : CommonView<LoginActivityBinding> {

    fun setTitle(@StringRes title: Int)
    fun showPasswordRepeat(isVisible: Boolean)
    fun setButtonText(@StringRes text: Int)
    fun setButtonColor(@ColorRes color: Int)
    fun showMenu(@IdRes vararg items: Int)

    interface Callbacks {

        fun onPrimaryButtonClicked(username: String, password: String, passwordRepeat: String)
        fun onLoginMenuClicked()
        fun onSignupMenuClicked()
        fun onBackClicked()
    }
}

class LoginViewImpl(
    override val binding: LoginActivityBinding,
    private val callbacks: LoginView.Callbacks,
) : LoginView {

    private val toolbar = binding.defaultAppBar.toolbar
    private val primaryButton = binding.loginPrimaryButton

    init {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { callbacks.onBackClicked() }
        toolbar.inflateMenu(R.menu.login_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.option_log_in -> {
                    callbacks.onLoginMenuClicked()
                    true
                }
                R.id.option_sign_up -> {
                    callbacks.onSignupMenuClicked()
                    true
                }
                else -> false
            }
        }
        primaryButton.setOnClickListener {
            callbacks.onPrimaryButtonClicked(
                username = binding.usernameEditText.text.toString(),
                password = binding.passwordEditText.text.toString(),
                passwordRepeat = binding.passwordRepeatEditText.text.toString(),
            )
        }
    }

    override fun setTitle(title: Int) {
        toolbar.setTitle(title)
    }

    override fun setButtonText(text: Int) {
        primaryButton.setText(text)
    }

    override fun setButtonColor(color: Int) {
        primaryButton.setBackgroundColor(context.getColor(color))
    }

    override fun showPasswordRepeat(isVisible: Boolean) {
        binding.passwordRepeatInput.showNow(isVisible)
    }

    override fun showMenu(vararg items: Int) {
        toolbar.menu.forEach { item ->
            item.isVisible = item.itemId in items
        }
    }
}
