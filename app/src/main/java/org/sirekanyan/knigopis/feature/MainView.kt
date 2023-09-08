package org.sirekanyan.knigopis.feature

import android.content.Context.MODE_PRIVATE
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import org.sirekanyan.knigopis.BuildConfig
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.menu.OptionItem
import org.sirekanyan.knigopis.common.android.menu.addAll
import org.sirekanyan.knigopis.common.android.menu.getOption
import org.sirekanyan.knigopis.common.android.menu.optionIds
import org.sirekanyan.knigopis.common.android.toast.CommonView
import org.sirekanyan.knigopis.common.extensions.context
import org.sirekanyan.knigopis.common.extensions.hide
import org.sirekanyan.knigopis.common.extensions.isNightMode
import org.sirekanyan.knigopis.common.extensions.show
import org.sirekanyan.knigopis.databinding.ActivityMainBinding
import org.sirekanyan.knigopis.model.CurrentTab
import org.sirekanyan.knigopis.model.CurrentTab.*
import org.sirekanyan.knigopis.repository.BookSorting
import org.sirekanyan.knigopis.repository.Theme
import org.sirekanyan.knigopis.repository.UserSorting
import org.sirekanyan.knigopis.repository.cache.COMMON_PREFS_NAME

private val DEBUG_OPTIONS = arrayOf(R.id.debug_option_clear_cache, R.id.debug_option_toggle_theme)

interface MainView : CommonView<ActivityMainBinding> {

    fun showAboutDialog()
    fun showPage(tab: CurrentTab)
    fun showNavigation(isVisible: Boolean)
    fun setNavigation(itemId: Int)
    fun showLoginOption(isVisible: Boolean)
    fun showProfileOption(isVisible: Boolean)
    fun setOptionChecked(item: OptionItem)
    fun setThemeOptionChecked(theme: Theme)
    fun setCrashReportOptionChecked(isChecked: Boolean)

    interface Callbacks {
        fun onNavigationClicked(itemId: Int)
        fun onLoginOptionClicked()
        fun onProfileOptionClicked()
        fun onAboutOptionClicked()
        fun onSortOptionClicked(sorting: BookSorting)
        fun onUserSortOptionClicked(sorting: UserSorting)
        fun onThemeOptionClicked(theme: Theme)
        fun onCrashReportOptionClicked(isChecked: Boolean)
    }

}

class MainViewImpl(
    override val binding: ActivityMainBinding,
    private val callbacks: MainView.Callbacks,
) : MainView {

    private val toolbar = binding.defaultAppBar.toolbar
    private val booksPage = binding.books.booksPage
    private val usersPage = binding.users.usersPage
    private val notesPage = binding.notes.notesPage
    private val addBookButton = binding.books.addBookButton
    private val booksRecyclerView = binding.books.booksRecyclerView
    private val usersRecyclerView = binding.users.usersRecyclerView
    private val notesRecyclerView = binding.notes.notesRecyclerView
    private val bottomNavigation = binding.bottomNavigation

    private val loginOption: MenuItem
    private val profileOption: MenuItem

    init {
        toolbar.inflateMenu(R.menu.options)
        toolbar.menu.findItem(R.id.option_sort_books).addAll(BookSorting.values())
        toolbar.menu.findItem(R.id.option_sort_users).addAll(UserSorting.values())
        val themeOptions = Theme.values().map(Theme::id)
        toolbar.setOnMenuItemClickListener { item ->
            when (val itemId = item.itemId) {
                R.id.option_login -> {
                    callbacks.onLoginOptionClicked()
                    true
                }
                R.id.option_profile -> {
                    callbacks.onProfileOptionClicked()
                    true
                }
                R.id.option_about -> {
                    callbacks.onAboutOptionClicked()
                    true
                }
                in optionIds<BookSorting>() -> {
                    item.isChecked = true
                    callbacks.onSortOptionClicked(getOption(itemId))
                    true
                }
                in optionIds<UserSorting>() -> {
                    item.isChecked = true
                    callbacks.onUserSortOptionClicked(getOption(itemId))
                    true
                }
                in themeOptions -> {
                    item.isChecked = true
                    callbacks.onThemeOptionClicked(Theme.getById(itemId))
                    true
                }
                R.id.option_crash_report -> {
                    item.isChecked = !item.isChecked
                    callbacks.onCrashReportOptionClicked(item.isChecked)
                    true
                }
                R.id.debug_option_clear_cache -> {
                    context.cacheDir.deleteRecursively()
                    context.getSharedPreferences(COMMON_PREFS_NAME, MODE_PRIVATE)
                        .edit().clear().apply()
                    true
                }
                R.id.debug_option_toggle_theme -> {
                    val newTheme = if (context.isNightMode) Theme.LIGHT else Theme.DARK
                    callbacks.onThemeOptionClicked(newTheme)
                    true
                }
                else -> false
            }
        }
        loginOption = toolbar.menu.findItem(R.id.option_login)
        profileOption = toolbar.menu.findItem(R.id.option_profile)
        DEBUG_OPTIONS.forEach { debugOption ->
            toolbar.menu.findItem(debugOption).isVisible = BuildConfig.DEBUG
        }
    }

    override fun showAboutDialog() {
        val dialogView = View.inflate(context, R.layout.about, null)
        dialogView.findViewById<TextView>(R.id.aboutAppVersion).text = BuildConfig.VERSION_NAME
        AlertDialog.Builder(context).setView(dialogView).show()
    }

    override fun showPage(tab: CurrentTab) {
        if (tab == BOOKS_TAB) {
            addBookButton.translationX = 0f
            addBookButton.translationY = 0f
        }
        booksRecyclerView.stopScroll()
        usersRecyclerView.stopScroll()
        notesRecyclerView.stopScroll()
        booksPage.show(tab == BOOKS_TAB)
        usersPage.show(tab == USERS_TAB)
        notesPage.show(tab == NOTES_TAB)
        toolbar.menu.findItem(R.id.option_sort_books).isVisible = tab == BOOKS_TAB
        toolbar.menu.findItem(R.id.option_sort_users).isVisible = tab == USERS_TAB
        toolbar.menu.findItem(R.id.option_crash_report).isVisible = BuildConfig.ACRA_ENABLED
    }

    override fun showNavigation(isVisible: Boolean) {
        if (isVisible) {
            bottomNavigation.show()
            bottomNavigation.setOnItemSelectedListener { item ->
                callbacks.onNavigationClicked(item.itemId)
                true
            }
        } else {
            bottomNavigation.hide()
            bottomNavigation.setOnItemSelectedListener(null)
        }
    }

    override fun setNavigation(itemId: Int) {
        bottomNavigation.menu.findItem(itemId).isChecked = true
    }

    override fun showLoginOption(isVisible: Boolean) {
        loginOption.isVisible = isVisible
    }

    override fun showProfileOption(isVisible: Boolean) {
        profileOption.isVisible = isVisible
    }

    override fun setOptionChecked(item: OptionItem) {
        toolbar.menu.findItem(item.id).isChecked = true
    }

    override fun setThemeOptionChecked(theme: Theme) {
        toolbar.menu.findItem(theme.id).isChecked = true
    }

    override fun setCrashReportOptionChecked(isChecked: Boolean) {
        toolbar.menu.findItem(R.id.option_crash_report).isChecked = isChecked
    }

}