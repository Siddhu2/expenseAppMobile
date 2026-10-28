package com.expense.tracker.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expense.tracker.data.model.Expense
import com.expense.tracker.ui.components.ExpenseItem
import com.expense.tracker.ui.components.MonthSelector
import com.expense.tracker.ui.components.TotalSpentCard
import com.expense.tracker.utils.CsvExporter
import com.expense.tracker.viewmodel.ExpenseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: ExpenseViewModel,
    onNavigateToChart: () -> Unit
) {
    val context     = LocalContext.current
    val scope       = rememberCoroutineScope()
    val currentMonth by viewModel.currentMonth.collectAsState()
    val expenses     by viewModel.expenses.collectAsState()
    val totalSpent   by viewModel.totalSpent.collectAsState()
    val categories   by viewModel.categories.collectAsState()

    var showAddSheet by remember { mutableStateOf(false) }
    var expenseToEdit by remember { mutableStateOf<Expense?>(null) }

    // SAF launcher for CSV export
    val csvLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            scope.launch(Dispatchers.IO) {
                CsvExporter.export(context, uri, expenses)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Expense Tracker",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = onNavigateToChart) {
                        Icon(
                            Icons.Default.PieChart,
                            contentDescription = "View Chart",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = {
                        val fileName = "expenses_${currentMonth.format(DateTimeFormatter.ofPattern("yyyy_MM"))}.csv"
                        csvLauncher.launch(fileName)
                    }) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Export CSV",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Expense") },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                onClick = { 
                    expenseToEdit = null
                    showAddSheet = true 
                },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                MonthSelector(
                    month     = currentMonth,
                    onPrevious = viewModel::previousMonth,
                    onNext     = viewModel::nextMonth,
                    modifier  = Modifier.fillMaxWidth()
                )
            }

            item {
                TotalSpentCard(
                    totalSpent = totalSpent,
                    modifier   = Modifier.padding(vertical = 8.dp)
                )
            }

            if (expenses.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No expenses this month",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            items(expenses, key = { it.id }) { expense ->
                AnimatedVisibility(
                    visible = true,
                    enter   = fadeIn(),
                    exit    = fadeOut()
                ) {
                    ExpenseItem(
                        expense  = expense,
                        onEdit   = { 
                            expenseToEdit = expense
                            showAddSheet = true
                        },
                        onDelete = { viewModel.deleteExpense(expense) }
                    )
                }
            }
        }
    }

    if (showAddSheet) {
        AddExpenseBottomSheet(
            categories    = categories,
            existingExpense = expenseToEdit,
            onDismiss     = { 
                showAddSheet = false 
                expenseToEdit = null
            },
            onAddExpense  = viewModel::addExpense,
            onEditExpense = viewModel::updateExpense,
            onAddCategory = viewModel::addCategory
        )
    }
}
