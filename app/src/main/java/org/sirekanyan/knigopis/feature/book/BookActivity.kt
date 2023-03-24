package org.sirekanyan.knigopis.feature.book

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.sirekanyan.knigopis.common.BaseActivity
import org.sirekanyan.knigopis.common.extensions.getParcelableExtraCompat
import org.sirekanyan.knigopis.common.functions.extra
import org.sirekanyan.knigopis.databinding.BookEditBinding
import org.sirekanyan.knigopis.dependency.providePresenter
import org.sirekanyan.knigopis.model.EditBookModel

private val EXTRA_BOOK = extra("book")

fun Context.createBookIntent(book: EditBookModel): Intent =
    Intent(this, BookActivity::class.java).putExtra(EXTRA_BOOK, book)

class BookActivity : BaseActivity(), BookPresenter.Router {

    val binding by lazy { BookEditBinding.inflate(layoutInflater) }
    private val presenter by lazy { providePresenter(intent.getParcelableExtraCompat(EXTRA_BOOK)!!) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        presenter.init()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun exit(ok: Boolean) {
        if (ok) setResult(RESULT_OK)
        finish()
    }

}