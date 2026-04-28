package com.budgetquest.app.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Core user account with XP progression values.
 */
@Entity(indices = [Index(value = ["username"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val passwordHash: String,
    val securityQuestion: String,
    val securityAnswer: String,
    val xp: Int = 0,
    val level: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * User-defined categories.
 */
@Entity(
    foreignKeys = [ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("userId")]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val name: String,
    val emoji: String,
    val colorHex: String
)

/**
 * Expense row including optional receipt image path.
 */
@Entity(
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = CategoryEntity::class, parentColumns = ["id"], childColumns = ["categoryId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("userId"), Index("categoryId"), Index("date")]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val categoryId: Long,
    val amountZar: Double,
    val date: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val receiptPhotoPath: String?,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Monthly budget minimum and maximum.
 */
@Entity(indices = [Index(value = ["userId", "month"], unique = true)])
data class BudgetGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val monthlyMin: Double,
    val monthlyMax: Double,
    val month: String
)

/**
 * Per-category monthly limit.
 */
@Entity(indices = [Index(value = ["userId", "categoryId", "month"], unique = true)])
data class CategoryLimitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val categoryId: Long,
    val limitAmount: Double,
    val month: String
)

/**
 * Optional badge table for earned badges.
 */
@Entity(indices = [Index(value = ["userId", "badgeKey"], unique = true)])
data class BadgeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val badgeKey: String,
    val earnedAt: Long = System.currentTimeMillis()
)
