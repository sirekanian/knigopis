package org.sirekanyan.knigopis.feature.login

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import org.sirekanyan.knigopis.R
import org.sirekanyan.knigopis.common.android.toast.CommonView
import org.sirekanyan.knigopis.common.extensions.context
import org.sirekanyan.knigopis.common.extensions.inflate
import org.sirekanyan.knigopis.databinding.LoginActivityBinding

interface LoginView : CommonView<LoginActivityBinding> {

    fun addWebsite(website: Website)
    fun showNoBrowserDialog()

    interface Callbacks {

        fun onWebsiteClicked(website: Website)
        fun onInstallBrowserClicked(packageName: String)
        fun onBackClicked()

    }

}

class LoginViewImpl(
    override val binding: LoginActivityBinding,
    private val callbacks: LoginView.Callbacks,
) : LoginView {

    private val toolbar = binding.defaultAppBar.toolbar
    private val websitesContainer = binding.websitesContainer

    init {
        toolbar.setTitle(R.string.login_title)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { callbacks.onBackClicked() }
    }

    override fun addWebsite(website: Website) {
        websitesContainer.addView(
            websitesContainer.inflate<ViewGroup>(R.layout.website_layout).also { container ->
                container.findViewById<TextView>(R.id.websiteTitle).setText(website.title)
                container.findViewById<ImageView>(R.id.websiteLogo).setImageResource(website.icon)
                container.setOnClickListener {
                    callbacks.onWebsiteClicked(website)
                }
            }
        )
    }

    override fun showNoBrowserDialog() {
        AlertDialog.Builder(context)
            .setTitle(R.string.login_browser_title)
            .setItems(R.array.login_browsers) { _, which ->
                when (which) {
                    0 -> callbacks.onInstallBrowserClicked("org.mozilla.firefox")
                    1 -> callbacks.onInstallBrowserClicked("com.android.chrome")
                }
            }
            .show()
    }

}