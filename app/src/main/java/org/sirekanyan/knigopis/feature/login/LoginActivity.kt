package org.sirekanyan.knigopis.feature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.sirekanyan.knigopis.common.BaseActivity
import org.sirekanyan.knigopis.databinding.LoginActivityBinding
import org.sirekanyan.knigopis.dependency.providePresenter

fun Context.startLoginActivity() {
    startActivity(Intent(this, LoginActivity::class.java))
}

class LoginActivity : BaseActivity(), LoginPresenter.Router {

    val binding by lazy { LoginActivityBinding.inflate(layoutInflater) }
    private val presenter by lazy(::providePresenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        presenter.init(savedInstanceState?.getLoginState())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.saveLoginState(presenter.state)
    }

    override fun close() {
        finish()
    }
}
