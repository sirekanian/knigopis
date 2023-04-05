package org.sirekanyan.knigopis

import android.content.ComponentName
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Test
import org.junit.runner.RunWith

private const val PACKAGE = "org.sirekanyan.knigopis.debug"
private const val ACTIVITY = "org.sirekanyan.knigopis.feature.MainActivity"

@RunWith(AndroidJUnit4::class)
class AndroidTest {

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val app = ApplicationProvider.getApplicationContext<App>().also {
        val token = InstrumentationRegistry.getArguments().getString("testToken")
        it.tokenStorage.accessToken = checkNotNull(token) { "Test token is not specified" }
    }
    private val intent =
        Intent.makeMainActivity(ComponentName(PACKAGE, ACTIVITY))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

    @Test
    fun addBook() {
        app.startActivity(intent)
        text("You don't have any books")
        id("addBookButton").click()
        text("Title").text = "My Book"
        id("option_save_book").click()
        text("Books")
        text("My Book")
        text("(no author)")
    }

    @Test
    fun deleteBook() {
        app.startActivity(intent)
        text("My Book").click(1000)
        text("Delete").click()
        text("DELETE").click()
        text("You don't have any books")
    }

    private fun id(id: String): UiObject2 =
        wait(By.res("$PACKAGE:id/$id"))

    private fun text(text: String): UiObject2 =
        wait(By.text(text))

    private fun wait(selector: BySelector): UiObject2 {
        val condition = Until.hasObject(selector)
        check(device.wait(condition, 60000)) { "Timeout reached" }
        return device.findObject(selector)
    }

}