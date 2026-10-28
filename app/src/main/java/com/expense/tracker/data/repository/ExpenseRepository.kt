package com.expense.tracker.data.repository

import com.expense.tracker.data.dao.CategoryDao
import com.expense.tracker.data.dao.ExpenseDao
import com.expense.tracker.data.model.Category
import com.expense.tracker.data.model.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) {
    fun getExpensesForMonth(startMillis: Long, endMillis: Long): Flow<List<Expense>> =
        expenseDao.getExpensesForMonth(startMillis, endMillis)

    fun getTotalForMonth(startMillis: Long, endMillis: Long): Flow<Double?> =
        expenseDao.getTotalForMonth(startMillis, endMillis)

    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun addExpense(expense: Expense) = expenseDao.insert(expense)
    
    suspend fun updateExpense(expense: Expense) = expenseDao.update(expense)

    suspend fun deleteExpense(expense: Expense) = expenseDao.delete(expense)

    suspend fun addCategory(name: String) = categoryDao.insert(Category(name = name))
}
