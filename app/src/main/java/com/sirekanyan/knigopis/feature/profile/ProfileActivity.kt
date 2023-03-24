package com.sirekanyan.knigopis.feature.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sirekanyan.knigopis.common.BaseActivity
import com.sirekanyan.knigopis.common.functions.createProfileShareIntent
import com.sirekanyan.knigopis.databinding.ProfileActivityBinding
import com.sirekanyan.knigopis.dependency.providePresenter

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

    override fun shareProfile(profileUrl: String) {
        startActivity(createProfileShareIntent(profileUrl))
    }

    override fun exit() {
        finish()
    }

    override fun onBackPressed() {
        presenter.back()
    }

}