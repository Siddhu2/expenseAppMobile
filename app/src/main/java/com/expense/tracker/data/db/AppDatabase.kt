package com.expense.tracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.expense.tracker.data.dao.CategoryDao
import com.expense.tracker.data.dao.ExpenseDao
import com.expense.tracker.data.model.Category
import com.expense.tracker.data.model.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Category::class, Expense::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        private val DEFAULT_CATEGORIES = listOf(
            "Food", "Transport", "Rent", "Bills", "Entertainment", "Health", "Others"
        )

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_tracker.db"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Pre-populate default categories on first creation
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.categoryDao()?.insertAll(
                                DEFAULT_CATEGORIES.map { Category(name = it) }
                            )
                        }
                    }
                })
                .build()
                .also { INSTANCE = it }
            }
        }
    }
}
