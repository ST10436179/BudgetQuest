package com.budgetquest.app.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Contains pure game and budget computations for testability.
 */
object GameLogic {
    const val BADGE_FIRST_ENTRY = "FIRST_ENTRY"
    const val BADGE_WEEK_WARRIOR = "WEEK_WARRIOR"
    const val BADGE_BUDGET_HERO = "BUDGET_HERO"

    fun levelFromXp(xp: Int): Int = when {
        xp >= 2000 -> 5
        xp >= 1000 -> 4
        xp >= 500 -> 3
        xp >= 200 -> 2
        else -> 1
    }

    fun rankFromXp(xp: Int): String = when {
        xp >= 2000 -> "BudgetQuest Master"
        xp >= 1000 -> "Finance Sage"
        xp >= 500 -> "Money Manager"
        xp >= 200 -> "Budget Apprentice"
        else -> "Penny Pincher"
    }

    fun budgetPercent(spent: Double, maxGoal: Double): Int {
        if (maxGoal <= 0.0) return 0
        return ((spent / maxGoal) * 100).toInt().coerceAtLeast(0)
    }

    fun hasSevenDayStreak(distinctDates: List<String>, today: LocalDate = LocalDate.now()): Boolean {
        val set = distinctDates.toSet()
        val fmt = DateTimeFormatter.ISO_DATE
        for (i in 0..6) {
            val day = today.minusDays(i.toLong()).format(fmt)
            if (!set.contains(day)) return false
        }
        return true
    }

    fun shouldAwardFirstEntry(totalExpensesAfterSave: Int): Boolean = totalExpensesAfterSave == 1

    fun shouldAwardBudgetHero(hasAnyOverspend: Boolean, monthExpenseCount: Int): Boolean =
        !hasAnyOverspend && monthExpenseCount > 0
}
