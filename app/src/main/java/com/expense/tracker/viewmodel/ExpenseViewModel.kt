package com.expense.tracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.expense.tracker.data.model.Category
import com.expense.tracker.data.model.Expense
import com.expense.tracker.data.repository.ExpenseRepository
import com.expense.tracker.utils.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    // ── Month navigation ──────────────────────────────────────────────────
    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    fun previousMonth() { _currentMonth.value = _currentMonth.value.minusMonths(1) }
    fun nextMonth()     { _currentMonth.value = _currentMonth.value.plusMonths(1) }

    // ── Derived month range → expenses ───────────────────────────────────
    val expenses: StateFlow<List<Expense>> = _currentMonth
        .flatMapLatest { month ->
            val (start, end) = DateUtils.monthRange(month)
            repository.getExpensesForMonth(start, end)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalSpent: StateFlow<Double> = _currentMonth
        .flatMapLatest { month ->
            val (start, end) = DateUtils.monthRange(month)
            repository.getTotalForMonth(start, end).map { it ?: 0.0 }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    // ── Categories ────────────────────────────────────────────────────────
    val categories: StateFlow<List<Category>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ── Actions ───────────────────────────────────────────────────────────
    fun addExpense(title: String, amount: Double, category: Category, dateMillis: Long) {
        viewModelScope.launch {
            repository.addExpense(
                Expense(
                    title = title,
                    amount = amount,
                    categoryId = category.id,
                    categoryName = category.name,
                    dateMillis = dateMillis
                )
            )
        }
    }

    fun updateExpense(expense: Expense, title: String, amount: Double, category: Category, dateMillis: Long) {
        viewModelScope.launch {
            repository.updateExpense(
                expense.copy(
                    title = title,
                    amount = amount,
                    categoryId = category.id,
                    categoryName = category.name,
                    dateMillis = dateMillis
                )
            )
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch { repository.deleteExpense(expense) }
    }

    fun addCategory(name: String) {
        viewModelScope.launch { repository.addCategory(name.trim()) }
    }

    // ── Factory ───────────────────────────────────────────────────────────
    class Factory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ExpenseViewModel(repository) as T
    }
}
