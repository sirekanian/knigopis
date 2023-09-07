package org.sirekanyan.knigopis.feature.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.sirekanyan.knigopis.common.BaseActivity
import org.sirekanyan.knigopis.common.functions.createTextShareIntent
import org.sirekanyan.knigopis.databinding.ProfileActivityBinding
import org.sirekanyan.knigopis.dependency.providePresenter

fun Context.createProfileIntent() = Intent(this, ProfileActivity::class.java)

class ProfileActivity : BaseActivity(), ProfilePresenter.Router {

    val binding by lazy { ProfileActivityBinding.inflate(layoutInflater) }
    private val presenter by lazy(::providePresenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        presenter.init()
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun shareText(content: String) {
        startActivity(createTextShareIntent(content))
    }

    override fun exit() {
        finish()
    }

    override fun onBackPressed() {
        presenter.back()
    }

}