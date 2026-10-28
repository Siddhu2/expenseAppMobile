package com.expense.tracker.utils

import java.time.YearMonth
import java.time.ZoneId

object DateUtils {
    fun monthRange(yearMonth: YearMonth): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val start = yearMonth.atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val end   = yearMonth.atEndOfMonth().atTime(23, 59, 59).atZone(zone).toInstant().toEpochMilli()
        return start to end
    }
}
