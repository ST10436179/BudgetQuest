package com.budgetquest.app

import com.budgetquest.app.domain.GameLogic
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BadgeLogicTest {
    @Test
    fun firstEntryBadge_awardsOnlyOnFirstExpense() {
        assertTrue(GameLogic.shouldAwardFirstEntry(1))
        assertFalse(GameLogic.shouldAwardFirstEntry(2))
    }

    @Test
    fun budgetHeroBadge_requiresNoOverspend() {
        assertTrue(GameLogic.shouldAwardBudgetHero(false, 5))
        assertFalse(GameLogic.shouldAwardBudgetHero(true, 5))
        assertFalse(GameLogic.shouldAwardBudgetHero(false, 0))
    }
}
