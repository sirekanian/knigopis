package com.sirekanyan.knigopis.feature

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.common.BaseActivity
import com.sirekanyan.knigopis.common.extensions.app
import com.sirekanyan.knigopis.common.extensions.io2main
import com.sirekanyan.knigopis.common.extensions.showToast
import com.sirekanyan.knigopis.common.functions.logError
import com.sirekanyan.knigopis.dependency.providePresenter
import com.sirekanyan.knigopis.feature.book.createBookIntent
import com.sirekanyan.knigopis.feature.books.BooksPresenter
import com.sirekanyan.knigopis.feature.login.startLoginActivity
import com.sirekanyan.knigopis.feature.notes.NotesPresenter
import com.sirekanyan.knigopis.feature.profile.createProfileIntent
import com.sirekanyan.knigopis.feature.user.createUserIntent
import com.sirekanyan.knigopis.feature.users.UsersPresenter
import com.sirekanyan.knigopis.feature.users.getMainState
import com.sirekanyan.knigopis.feature.users.saveMainState
import com.sirekanyan.knigopis.model.*

fun Context.startMainActivity() {
    startActivity(
        Intent(this, MainActivity::class.java)
            .setFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
    )
}

class MainActivity : BaseActivity(),
    MainPresenter.Router,
    BooksPresenter.Router,
    UsersPresenter.Router,
    NotesPresenter.Router {

    private val presenter by lazy { providePresenter() }
    private val api by lazy { app.endpoint }
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!presenter.back()) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Knigopis)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val restoredCurrentTab = savedInstanceState?.getMainState()?.currentTab
        presenter.init(restoredCurrentTab)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
        intent.data?.also { userUrl ->
            intent.data = null
            val normalizedUri = Uri.parse(userUrl.toString().replaceFirst("/#/", "/"))
            normalizedUri.getQueryParameter("u")?.let { userId ->
                api.getUser(userId)
                    .io2main()
                    .bind({ user ->
                        openUserScreen(userId, user.name)
                    }, {
                        logError("Cannot get user", it)
                    })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.state?.let { outState.saveMainState(it) }
    }

    private val resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            presenter.onBookScreenResult()
        }
    }

    override fun openLoginScreen() {
        startLoginActivity()
    }

    override fun openProfileScreen() {
        startActivity(createProfileIntent())
    }

    override fun openNewBookScreen() {
        resultLauncher.launch(createBookIntent(EMPTY_BOOK))
    }

    override fun openBookScreen(book: BookDataModel) {
        resultLauncher.launch(createBookIntent(book.toEditModel()))
    }

    override fun openUserScreen(user: UserModel) {
        openUserScreen(user.id, user.name)
    }

    override fun openUserScreen(note: NoteModel) {
        openUserScreen(note.userId, note.userName)
    }

    private fun openUserScreen(id: String, name: String) {
        startActivity(createUserIntent(id, name))
    }

    override fun openWebPage(uri: Uri) {
        try {
            startActivity(Intent(ACTION_VIEW, uri))
        } catch (ex: ActivityNotFoundException) {
            showToast(R.string.users_info_no_browser)
        }
    }

}
