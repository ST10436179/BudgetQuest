package com.budgetquest.app.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE id = :userId LIMIT 1")
    suspend fun findById(userId: Long): UserEntity?
}

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long

    @Update
    suspend fun update(category: CategoryEntity)

    @Delete
    suspend fun delete(category: CategoryEntity)

    @Query("SELECT * FROM CategoryEntity WHERE userId = :userId ORDER BY name")
    fun getByUser(userId: Long): LiveData<List<CategoryEntity>>

    @Query("SELECT * FROM CategoryEntity WHERE userId = :userId ORDER BY name")
    suspend fun getByUserNow(userId: Long): List<CategoryEntity>
}

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity): Long

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("SELECT * FROM ExpenseEntity WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC, createdAt DESC")
    fun getByDateRange(userId: Long, startDate: String, endDate: String): LiveData<List<ExpenseEntity>>

    @Query("SELECT * FROM ExpenseEntity WHERE userId = :userId AND categoryId = :categoryId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    suspend fun getByCategoryAndDateRange(userId: Long, categoryId: Long, startDate: String, endDate: String): List<ExpenseEntity>

    @Query("SELECT * FROM ExpenseEntity WHERE userId = :userId AND strftime('%Y-%m', date) = :month")
    suspend fun getByMonth(userId: Long, month: String): List<ExpenseEntity>

    @Query("SELECT COUNT(*) FROM ExpenseEntity WHERE userId = :userId")
    suspend fun countByUser(userId: Long): Int

    @Query("SELECT DISTINCT date FROM ExpenseEntity WHERE userId = :userId ORDER BY date DESC")
    suspend fun distinctDates(userId: Long): List<String>

    @Query("SELECT * FROM ExpenseEntity WHERE id = :expenseId LIMIT 1")
    suspend fun getById(expenseId: Long): ExpenseEntity?
}

@Dao
interface BudgetGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: BudgetGoalEntity): Long

    @Update
    suspend fun update(goal: BudgetGoalEntity)

    @Delete
    suspend fun delete(goal: BudgetGoalEntity)

    @Query("SELECT * FROM BudgetGoalEntity WHERE userId = :userId AND month = :month LIMIT 1")
    suspend fun getByMonth(userId: Long, month: String): BudgetGoalEntity?
}

@Dao
interface CategoryLimitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(limit: CategoryLimitEntity): Long

    @Update
    suspend fun update(limit: CategoryLimitEntity)

    @Delete
    suspend fun delete(limit: CategoryLimitEntity)

    @Query("SELECT * FROM CategoryLimitEntity WHERE userId = :userId AND month = :month")
    suspend fun getByMonth(userId: Long, month: String): List<CategoryLimitEntity>

    @Query("SELECT * FROM CategoryLimitEntity WHERE userId = :userId AND categoryId = :categoryId AND month = :month LIMIT 1")
    suspend fun getByCategoryAndMonth(userId: Long, categoryId: Long, month: String): CategoryLimitEntity?
}

@Dao
interface BadgeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(badge: BadgeEntity): Long

    @Update
    suspend fun update(badge: BadgeEntity)

    @Delete
    suspend fun delete(badge: BadgeEntity)

    @Query("SELECT * FROM BadgeEntity WHERE userId = :userId")
    suspend fun getByUser(userId: Long): List<BadgeEntity>

    @Query("SELECT COUNT(*) FROM BadgeEntity WHERE userId = :userId AND badgeKey = :badgeKey")
    suspend fun hasBadge(userId: Long, badgeKey: String): Int
}
