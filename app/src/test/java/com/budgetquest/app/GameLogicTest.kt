package com.budgetquest.app

import com.budgetquest.app.domain.GameLogic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class GameLogicTest {
    @Test
    fun xpToLevel_isCalculatedCorrectly() {
        assertEquals(1, GameLogic.levelFromXp(100))
        assertEquals(2, GameLogic.levelFromXp(250))
        assertEquals(3, GameLogic.levelFromXp(700))
        assertEquals(4, GameLogic.levelFromXp(1500))
        assertEquals(5, GameLogic.levelFromXp(2200))
    }

    @Test
    fun budgetPercentage_calculationWorks() {
        assertEquals(46, GameLogic.budgetPercent(2300.0, 5000.0))
        assertEquals(0, GameLogic.budgetPercent(200.0, 0.0))
    }

    @Test
    fun weekWarrior_requiresSevenConsecutiveDates() {
        val today = LocalDate.of(2026, 4, 26)
        val dates = (0..6).map { today.minusDays(it.toLong()).toString() }
        assertTrue(GameLogic.hasSevenDayStreak(dates, today))
    }
}
