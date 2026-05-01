package org.sirekanyan.knigopis.repository

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import org.sirekanyan.knigopis.R

enum class Theme(@all:IdRes val id: Int, @all:NightMode val mode: Int) {

    LIGHT(
        R.id.option_light_theme,
        AppCompatDelegate.MODE_NIGHT_NO
    ),

    DARK(
        R.id.option_dark_theme,
        AppCompatDelegate.MODE_NIGHT_YES
    ),

    DEFAULT(
        R.id.option_default_theme,
        if (SDK_INT >= Q) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        }
    );

    fun setup() {
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    companion object {

        fun getById(@IdRes id: Int): Theme =
            entries.find { it.id == id } ?: DEFAULT

        fun getCurrent(): Theme =
            entries.find { it.mode == AppCompatDelegate.getDefaultNightMode() } ?: DEFAULT

    }

}