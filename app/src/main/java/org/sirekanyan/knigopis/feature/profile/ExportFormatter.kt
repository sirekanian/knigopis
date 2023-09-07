package org.sirekanyan.knigopis.feature.profile

import org.sirekanyan.knigopis.common.android.ResourceProvider
import org.sirekanyan.knigopis.common.extensions.getFullTitleString
import org.sirekanyan.knigopis.model.BookDataModel
import org.sirekanyan.knigopis.model.DateModel

class ExportFormatter(private val resources: ResourceProvider) {

    fun formatBooksForExport(books: List<BookDataModel>): String =
        books.withIndex().joinToString("\n\n") { (index, book) -> formatBook(index, book) }

    private fun formatBook(index: Int, book: BookDataModel): String {
        val title = "${index + 1}. ${resources.getFullTitleString(book.title, book.author)}"
        val details = if (book.isFinished) formatDate(book.date) else "${book.priority}%"
        return listOf(title, details, book.notes).filter(String::isNotBlank).joinToString("\n")
    }

    private fun formatDate(d: DateModel): String =
        buildString {
            if (d.year.isBlank()) return toString() else append(d.year)
            if (d.month.isBlank()) return toString() else append('-' + d.month.padStart(2, '0'))
            if (d.day.isBlank()) return toString() else append('-' + d.day.padStart(2, '0'))
        }

}