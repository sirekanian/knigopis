package org.sirekanyan.knigopis.feature.user

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.sirekanyan.knigopis.common.BaseActivity
import org.sirekanyan.knigopis.common.extensions.systemClipboardManager
import org.sirekanyan.knigopis.common.functions.extra
import org.sirekanyan.knigopis.databinding.UserActivityBinding
import org.sirekanyan.knigopis.dependency.providePresenter
import org.sirekanyan.knigopis.feature.book.createBookIntent
import org.sirekanyan.knigopis.model.EditBookModel

private val EXTRA_USER_ID = extra("user_id")
private val EXTRA_USER_NAME = extra("user_name")

fun Context.createUserIntent(id: String, name: String): Intent =
    Intent(this, UserActivity::class.java)
        .putExtra(EXTRA_USER_ID, id)
        .putExtra(EXTRA_USER_NAME, name)

class UserActivity : BaseActivity(), UserPresenter.Router {

    val binding by lazy { UserActivityBinding.inflate(layoutInflater) }
    private val presenter by lazy {
        providePresenter(
            checkNotNull(intent.getStringExtra(EXTRA_USER_ID)),
            checkNotNull(intent.getStringExtra(EXTRA_USER_NAME))
        )
    }

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

    override fun openBookScreen(book: EditBookModel) {
        startActivity(createBookIntent(book))
    }

    override fun copyToClipboard(text: String) {
        systemClipboardManager.setPrimaryClip(ClipData.newPlainText(null, text))
    }

}