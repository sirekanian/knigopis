package me.vadik.knigopis

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import me.vadik.knigopis.api.BookCoverSearch
import me.vadik.knigopis.api.BookCoverSearchImpl
import me.vadik.knigopis.api.Endpoint
import me.vadik.knigopis.api.ImageEndpoint
import me.vadik.knigopis.auth.KAuth
import me.vadik.knigopis.auth.KAuthImpl
import me.vadik.knigopis.model.FinishedBookToSend
import me.vadik.knigopis.model.PlannedBookToSend

class BookActivity : AppCompatActivity() {

  private val api by lazy { app().baseApi.create(Endpoint::class.java) }
  private val auth by lazy { KAuthImpl(applicationContext, api) as KAuth }
  private val imageSearch: BookCoverSearch by lazy {
    BookCoverSearchImpl(
        app().imageApi.create(ImageEndpoint::class.java),
        getSharedPreferences("knigopis", MODE_PRIVATE)
    )
  }
  private val toolbar by lazy { findView<Toolbar>(R.id.toolbar) }
  private val titleEditText by lazy { findView<TextView>(R.id.book_title_edit_text) }
  private val authorEditText by lazy { findView<TextView>(R.id.book_author_edit_text) }
  private val dayEditText by lazy { findView<TextView>(R.id.book_day_edit_text) }
  private val monthEditText by lazy { findView<TextView>(R.id.book_month_edit_text) }
  private val yearEditText by lazy { findView<TextView>(R.id.book_year_edit_text) }
  private val readCheckbox by lazy { findView<CheckBox>(R.id.book_read_checkbox) }
  private val coverImageView by lazy { findView<ImageView>(R.id.cover_image_view) }
  private val dateInputViews by lazy {
    arrayOf<View>(
        findView(R.id.book_day_input),
        findView(R.id.book_month_input),
        findView(R.id.book_year_input)
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.book_edit)
    toolbar.inflateMenu(R.menu.book_menu)
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
    toolbar.setNavigationOnClickListener {
      finish()
    }
    toolbar.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.option_save_book -> {
          if (readCheckbox.isChecked) {
            api.postFinishedBook(auth.getAccessToken(), FinishedBookToSend(
                titleEditText.text.toString(),
                authorEditText.text.toString(),
                dayEditText.text.toString(),
                monthEditText.text.toString(),
                yearEditText.text.toString()
            ))
          } else {
            api.postPlannedBook(auth.getAccessToken(), PlannedBookToSend(
                titleEditText.text.toString(),
                authorEditText.text.toString()
            ))
          }.io2main().subscribe(
              { finish() },
              { logError("cannot post planned book", it) }
          )
          true
        }
        else -> false
      }
    }
    titleEditText.setOnFocusChangeListener { _, focus ->
      val editable = titleEditText.editableText
      if (!focus && !editable.isEmpty()) {
        imageSearch.search(editable.toString())
            .subscribe({ coverUrl ->
              Glide.with(applicationContext)
                  .load(coverUrl)
                  .apply(RequestOptions.centerCropTransform())
                  .into(coverImageView)
            }, {
              logError("cannot load thumbnail", it)
            })
      }
    }
    readCheckbox.setOnCheckedChangeListener { _, checked ->
      dateInputViews.forEach { view ->
        view.visibility = if (checked) VISIBLE else GONE
      }
    }
  }
}