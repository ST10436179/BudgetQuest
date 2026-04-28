package com.budgetquest.app.data

import androidx.lifecycle.LiveData
import com.budgetquest.app.data.db.*
import com.budgetquest.app.domain.GameLogic
import java.time.LocalDate
import timber.log.Timber

/**
 * Central repository coordinating Room reads/writes and gamification updates.
 */
class BudgetRepository(private val db: AppDatabase) {
    private val users = db.userDao()
    private val categories = db.categoryDao()
    private val expenses = db.expenseDao()
    private val goals = db.budgetGoalDao()
    private val limits = db.categoryLimitDao()
    private val badges = db.badgeDao()

    suspend fun registerUser(user: UserEntity): Long = users.insert(user)
    suspend fun findUserByUsername(username: String) = users.findByUsername(username)
    suspend fun findUserById(userId: Long) = users.findById(userId)
    suspend fun updateUser(user: UserEntity) = users.update(user)

    fun categoriesLive(userId: Long): LiveData<List<CategoryEntity>> = categories.getByUser(userId)
    suspend fun categoriesNow(userId: Long) = categories.getByUserNow(userId)
    suspend fun addCategory(category: CategoryEntity) = categories.insert(category)
    suspend fun updateCategory(category: CategoryEntity) = categories.update(category)
    suspend fun deleteCategory(category: CategoryEntity) = categories.delete(category)

    fun expensesByRange(userId: Long, start: String, end: String): LiveData<List<ExpenseEntity>> =
        expenses.getByDateRange(userId, start, end)

    suspend fun expenseById(expenseId: Long): ExpenseEntity? = expenses.getById(expenseId)

    suspend fun saveExpenseWithXp(expense: ExpenseEntity, isUpdate: Boolean = false) {
        Timber.d("saveExpenseWithXp user=%s update=%s", expense.userId, isUpdate)
        if (isUpdate) expenses.update(expense) else expenses.insert(expense)

        val user = users.findById(expense.userId) ?: return
        val newXp = user.xp + if (isUpdate) 0 else 10
        users.update(user.copy(xp = newXp, level = GameLogic.levelFromXp(newXp)))

        evaluateBadges(expense.userId)
    }

    suspend fun saveMonthlyGoals(goal: BudgetGoalEntity, categoryLimits: List<CategoryLimitEntity>) {
        goals.insert(goal)
        categoryLimits.forEach { limits.insert(it) }
    }

    suspend fun goalByMonth(userId: Long, month: String) = goals.getByMonth(userId, month)
    suspend fun limitsByMonth(userId: Long, month: String) = limits.getByMonth(userId, month)
    suspend fun monthExpenses(userId: Long, month: String) = expenses.getByMonth(userId, month)

    suspend fun evaluateBadges(userId: Long) {
        val total = expenses.countByUser(userId)
        if (total == 1) badges.insert(BadgeEntity(userId = userId, badgeKey = GameLogic.BADGE_FIRST_ENTRY))

        val dates = expenses.distinctDates(userId)
        if (GameLogic.hasSevenDayStreak(dates, LocalDate.now())) {
            badges.insert(BadgeEntity(userId = userId, badgeKey = GameLogic.BADGE_WEEK_WARRIOR))
        }

        val month = LocalDate.now().toString().substring(0, 7)
        val monthExpenses = expenses.getByMonth(userId, month)
        val monthLimits = limits.getByMonth(userId, month).associateBy { it.categoryId }
        val overspent = monthExpenses.groupBy { it.categoryId }.any { (categoryId, exps) ->
            val limit = monthLimits[categoryId]?.limitAmount ?: Double.MAX_VALUE
            exps.sumOf { it.amountZar } > limit
        }
        if (!overspent && monthExpenses.isNotEmpty()) {
            badges.insert(BadgeEntity(userId = userId, badgeKey = GameLogic.BADGE_BUDGET_HERO))
        }
    }

    suspend fun badges(userId: Long) = badges.getByUser(userId).map { it.badgeKey }.toSet()
}
