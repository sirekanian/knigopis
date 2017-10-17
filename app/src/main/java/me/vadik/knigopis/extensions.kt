package me.vadik.knigopis

import android.app.Activity
import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val TAG = "Knigopis"

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(@StringRes messageId: Int) = Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()

fun Activity.app() = application as App

fun <T : View> Activity.findView(@IdRes id: Int): T = findViewById(id)

fun logWarn(message: String) = Log.w(TAG, message)

fun logError(message: String, throwable: Throwable?) = Log.e(TAG, message, throwable)

fun ViewGroup.inflate(@LayoutRes layout: Int): View =
    LayoutInflater.from(context).inflate(layout, this, false)

fun <T> Single<T>.io2main(): Single<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun Completable.io2main(): Completable =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun String.orDefault(default: String) = if (isEmpty()) default else this

fun <T> RequestBuilder<T>.doOnSuccess(onSuccess: () -> Unit): RequestBuilder<T> =
    listener(object : RequestListener<T> {
      override fun onResourceReady(
          resource: T?,
          model: Any?,
          target: Target<T>?,
          dataSource: DataSource?,
          isFirstResource: Boolean
      ): Boolean {
        onSuccess()
        return false
      }

      override fun onLoadFailed(
          e: GlideException?,
          model: Any?,
          target: Target<T>?,
          isFirstResource: Boolean
      ): Boolean {
        return false
      }
    })

fun View.show() {
  alpha = 1f
}

fun View.hide() {
  alpha = 0f
}

fun View.fadeIn() {
  animate().alpha(1f).setDuration(200)
      .withStartAction { visibility = View.VISIBLE }
      .start()
}

fun View.fadeOut() {
  animate().alpha(0f).setDuration(200)
      .withEndAction { visibility = View.GONE }
      .start()
}

fun Activity.hideKeyboard() {
  currentFocus?.let { view ->
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(view.windowToken, 0)
  }
}