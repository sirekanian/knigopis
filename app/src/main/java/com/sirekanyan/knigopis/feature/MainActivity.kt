package com.sirekanyan.knigopis.feature

import android.Manifest.permission.READ_PHONE_STATE
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.sirekanyan.knigopis.BuildConfig
import com.sirekanyan.knigopis.R
import com.sirekanyan.knigopis.Router
import com.sirekanyan.knigopis.common.BaseActivity
import com.sirekanyan.knigopis.common.ResourceProvider
import com.sirekanyan.knigopis.common.extensions.*
import com.sirekanyan.knigopis.common.functions.logError
import com.sirekanyan.knigopis.common.view.dialog.DialogFactory
import com.sirekanyan.knigopis.createParameters
import com.sirekanyan.knigopis.feature.book.createEditBookIntent
import com.sirekanyan.knigopis.feature.book.createNewBookIntent
import com.sirekanyan.knigopis.feature.profile.createProfileIntent
import com.sirekanyan.knigopis.feature.user.createUserIntent
import com.sirekanyan.knigopis.model.BookDataModel
import com.sirekanyan.knigopis.model.CurrentTab
import com.sirekanyan.knigopis.model.CurrentTab.HOME_TAB
import com.sirekanyan.knigopis.model.CurrentTab.NOTES_TAB
import com.sirekanyan.knigopis.repository.*
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.books_page.*
import org.koin.android.ext.android.inject

private const val ULOGIN_REQUEST_CODE = 0
private const val BOOK_REQUEST_CODE = 1
private const val CURRENT_TAB_KEY = "current_tab"

class MainActivity : BaseActivity(), Router, MainPresenter.Router {

    private val api by inject<Endpoint>()
    private val config by inject<Configuration>()
    private val auth by inject<KAuth>()
    private val dialogs by inject<DialogFactory>(parameters = createParameters())
    private val bookRepository by inject<BookRepository>()
    private val userRepository by inject<UserRepository>()
    private val noteRepository by inject<NoteRepository>()
    private val resourceProvider by inject<ResourceProvider>()
    private var userLoggedIn = false
    private var booksChanged = false
    private lateinit var loginOption: MenuItem
    private lateinit var profileOption: MenuItem
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if (config.isDarkTheme) R.style.DarkAppTheme else R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenterImpl(
            this,
            config,
            bookRepository,
            userRepository,
            noteRepository,
            resourceProvider
        ).apply {
            view = MainViewImpl(getRootView(), this, dialogs)
        }
        val currentTabId = savedInstanceState?.getInt(CURRENT_TAB_KEY)
        val currentTab = currentTabId?.let { CurrentTab.getByItemId(it) }
        val defaultTab = if (auth.isAuthorized()) HOME_TAB else NOTES_TAB
        refresh(currentTab ?: defaultTab)
        initNavigationView()
        initToolbar(toolbar)
        booksRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                when {
                    dy > 0 -> addBookButton.hide()
                    dy < 0 -> addBookButton.show()
                }
            }
        })
        swipeRefresh.setOnRefreshListener {
            refresh(isForce = true)
        }
    }

    override fun onStart() {
        super.onStart()
        refreshOptionsMenu()
        auth.requestAccessToken().bind({
            refreshOptionsMenu()
            if (userLoggedIn) {
                userLoggedIn = false
                refresh()
            }
        }, {
            logError("cannot check credentials", it)
        })
        if (booksChanged) {
            booksChanged = false
            refresh(isForce = true)
        }
        intent.data?.also { userUrl ->
            intent.data = null
            val normalizedUri = Uri.parse(userUrl.toString().replaceFirst("/#/", "/"))
            normalizedUri.getQueryParameter("u")?.let { userId ->
                api.getUser(userId)
                    .io2main()
                    .bind({ user ->
                        openUserScreen(userId, user.name, user.photo)
                    }, {
                        logError("Cannot get user", it)
                    })
            }
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(CURRENT_TAB_KEY, presenter.currentTab.itemId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ULOGIN_REQUEST_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    auth.saveTokenResponse(data)
                    userLoggedIn = true
                }
            }
            BOOK_REQUEST_CODE -> {
                booksChanged = resultCode == RESULT_OK
            }
        }
    }

    override fun openBookScreen(book: BookDataModel) {
        startActivityForResult(createEditBookIntent(book), BOOK_REQUEST_CODE)
    }

    override fun openUserScreen(id: String, name: String, image: String?) {
        startActivity(createUserIntent(id, name, image))
    }

    override fun openWebPage(uri: Uri) {
        startActivityOrNull(Intent(ACTION_VIEW, uri)) ?: toast(R.string.users_info_no_browser)
    }

    override fun onBackPressed() {
        if (presenter.currentTab == HOME_TAB || !auth.isAuthorized()) {
            super.onBackPressed()
        } else {
            refresh(HOME_TAB)
        }
    }

    override fun openProfileScreen() {
        startActivity(createProfileIntent())
    }

    override fun reopenScreen() {
        recreate()
    }

    override fun openNewBookScreen() {
        startActivityForResult(createNewBookIntent(), BOOK_REQUEST_CODE)
    }

    private fun initNavigationView() {
        if (auth.isAuthorized()) {
            bottomNavigation.show()
            bottomNavigation.setOnNavigationItemSelectedListener { item ->
                val t = CurrentTab.getByItemId(item.itemId)
                presenter.currentTab = t
                presenter.showPage(t, false)
                true
            }
        } else {
            bottomNavigation.hide()
            bottomNavigation.setOnNavigationItemSelectedListener(null)
        }
    }

    private fun initToolbar(toolbar: Toolbar) {
        loginOption = toolbar.menu.findItem(R.id.option_login)
        profileOption = toolbar.menu.findItem(R.id.option_profile)
        val darkThemeOption = toolbar.menu.findItem(R.id.option_dark_theme)
        darkThemeOption.isChecked = config.isDarkTheme
        val clearCacheOption = toolbar.menu.findItem(R.id.option_clear_cache)
        clearCacheOption.isVisible = BuildConfig.DEBUG
        toolbar.setOnClickListener {
            if (presenter.currentTab == HOME_TAB) {
                config.sortingMode = if (config.sortingMode == 0) 1 else 0
                refresh(isForce = true)
            }
        }
    }

    override fun login() {
        RxPermissions(this).requestEach(READ_PHONE_STATE).bind({
            when {
                it.granted -> {
                    if (auth.isAuthorized()) {
                        auth.logout()
                        refresh()
                    } else {
                        startActivityForResult(auth.getTokenRequest(), ULOGIN_REQUEST_CODE)
                    }
                    refreshOptionsMenu()
                }
                it.shouldShowRequestPermissionRationale -> {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.permissions_title_no_access)
                        .setMessage(R.string.permissions_message_no_access)
                        .setPositiveButton(R.string.common_button_retry) { _, _ ->
                            login()
                        }
                        .setNegativeButton(R.string.common_button_cancel, null)
                        .setCancelable(false)
                        .show()
                }
                else -> {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.permissions_title_request)
                        .setMessage(R.string.permissions_message_request)
                        .setPositiveButton(R.string.permissions_button_settings) { _, _ ->
                            startActivity(
                                Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", packageName, null)
                                )
                            )
                        }
                        .setNegativeButton(R.string.common_button_cancel, null)
                        .setCancelable(false)
                        .show()
                }
            }
        }, {
            logError("cannot request permission", it)
        })
    }

    override fun forceRefresh() {
        refresh(isForce = true)
    }

    private fun refreshOptionsMenu() {
        initNavigationView()
        auth.isAuthorized().let { authorized ->
            loginOption.isVisible = !authorized
            profileOption.isVisible = authorized
        }
    }

    private fun refresh(tab: CurrentTab? = null, isForce: Boolean = false) {
        val t = if (auth.isAuthorized()) (tab ?: presenter.currentTab) else NOTES_TAB
        presenter.currentTab = t
        presenter.showPage(t, isForce)
        bottomNavigation.selectedItemId = t.itemId
    }

}
