package com.expense.tracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary          = Color(0xFF82AAFF),
    onPrimary        = Color(0xFF003258),
    primaryContainer = Color(0xFF00497D),
    secondary        = Color(0xFFBBC7DB),
    background       = Color(0xFF0F1117),
    surface          = Color(0xFF1C1F2A),
    surfaceVariant   = Color(0xFF252836),
    onBackground     = Color(0xFFE2E8F0),
    onSurface        = Color(0xFFE2E8F0),
    error            = Color(0xFFFF6B6B),
    tertiary         = Color(0xFF4ECDC4)
)

@Composable
fun ExpenseTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}
