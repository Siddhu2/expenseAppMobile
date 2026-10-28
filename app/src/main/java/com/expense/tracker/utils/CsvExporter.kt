package com.expense.tracker.utils

import android.content.Context
import android.net.Uri
import com.expense.tracker.data.model.Expense
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExporter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Writes [expenses] to the file pointed by [uri] (obtained via SAF).
     * Call this from a coroutine on an IO dispatcher.
     */
    fun export(context: Context, uri: Uri, expenses: List<Expense>) {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            OutputStreamWriter(outputStream, Charsets.UTF_8).use { writer ->
                writer.appendLine("Date,Title,Category,Amount")
                expenses.forEach { expense ->
                    val date = dateFormat.format(Date(expense.dateMillis))
                    val title = expense.title.escapeCsv()
                    val category = expense.categoryName.escapeCsv()
                    writer.appendLine("$date,$title,$category,${expense.amount}")
                }
            }
        }
    }

    private fun String.escapeCsv(): String {
        return if (contains(",") || contains("\"") || contains("\n")) {
            "\"${replace("\"", "\"\"")}\""
        } else this
    }
}
