package com.expense.tracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.expense.tracker.data.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    /**
     * Returns all expenses whose [dateMillis] falls in [startMillis, endMillis].
     * Callers should pass the first and last millisecond of the target month.
     */
    @Query(
        "SELECT * FROM expenses " +
        "WHERE dateMillis >= :startMillis AND dateMillis <= :endMillis " +
        "ORDER BY dateMillis DESC"
    )
    fun getExpensesForMonth(startMillis: Long, endMillis: Long): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT SUM(amount) FROM expenses WHERE dateMillis >= :startMillis AND dateMillis <= :endMillis")
    fun getTotalForMonth(startMillis: Long, endMillis: Long): Flow<Double?>
}
