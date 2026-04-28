package com.budgetquest.app.ui

import android.app.Application
import androidx.lifecycle.*
import com.budgetquest.app.data.BudgetRepository
import com.budgetquest.app.data.db.*
import com.budgetquest.app.domain.GameLogic
import com.budgetquest.app.util.SecurityUtils
import com.budgetquest.app.util.SessionManager
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate

/**
 * Single shared VM for this sample app, exposing auth/budget operations.
 */
class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = BudgetRepository(AppDatabase.getInstance(application))
    private val session = SessionManager(application)

    val currentUserId = MutableLiveData(session.getUserId())
    val authError = MutableLiveData<String?>()
    val infoMessage = MutableLiveData<String?>()
    val currentUser = MutableLiveData<UserEntity?>()
    val badges = MutableLiveData<Set<String>>(emptySet())

    fun attemptAutoLogin() {
        val id = session.getUserId()
        if (id > 0) {
            viewModelScope.launch {
                Timber.d("attemptAutoLogin userId=%s", id)
                currentUser.postValue(repo.findUserById(id))
                badges.postValue(repo.badges(id))
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            Timber.d("login username=%s", username)
            val user = repo.findUserByUsername(username.trim())
            val hash = SecurityUtils.sha256(password)
            if (user == null || user.passwordHash != hash) {
                authError.postValue("Invalid username or password")
            } else {
                session.saveUserId(user.id)
                currentUserId.postValue(user.id)
                currentUser.postValue(user)
                badges.postValue(repo.badges(user.id))
                authError.postValue(null)
            }
        }
    }

    fun register(
        username: String,
        password: String,
        question: String,
        answer: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            Timber.d("register username=%s", username)
            if (repo.findUserByUsername(username) != null) {
                authError.postValue("Username already exists")
                return@launch
            }
            val id = repo.registerUser(
                UserEntity(
                    username = username,
                    passwordHash = SecurityUtils.sha256(password),
                    securityQuestion = question,
                    securityAnswer = answer
                )
            )
            seedDefaultCategories(id)
            session.saveUserId(id)
            currentUserId.postValue(id)
            currentUser.postValue(repo.findUserById(id))
            onDone()
        }
    }

    private suspend fun seedDefaultCategories(userId: Long) {
        val defaults = listOf(
            Triple("Groceries", "🛒", "#6AA84F"),
            Triple("Transport", "🚗", "#3D85C6"),
            Triple("Entertainment", "🎬", "#8E7CC3"),
            Triple("Utilities", "⚡", "#F1C232"),
            Triple("Healthcare", "💊", "#CC4125"),
            Triple("Dining Out", "🍽️", "#E69138")
        )
        defaults.forEach { (name, emoji, color) ->
            repo.addCategory(CategoryEntity(userId = userId, name = name, emoji = emoji, colorHex = color))
        }
    }

    fun categoriesLive(): LiveData<List<CategoryEntity>> {
        val userId = currentUserId.value ?: -1L
        return if (userId > 0) repo.categoriesLive(userId) else MutableLiveData(emptyList())
    }

    fun expensesLive(startDate: String, endDate: String): LiveData<List<ExpenseEntity>> {
        val userId = currentUserId.value ?: -1L
        return if (userId > 0) repo.expensesByRange(userId, startDate, endDate) else MutableLiveData(emptyList())
    }

    fun saveExpense(expense: ExpenseEntity, isUpdate: Boolean, onDone: () -> Unit) {
        viewModelScope.launch {
            Timber.d("saveExpense id=%s update=%s", expense.id, isUpdate)
            repo.saveExpenseWithXp(expense, isUpdate)
            refreshUser()
            onDone()
        }
    }

    fun getExpense(expenseId: Long, onResult: (ExpenseEntity?) -> Unit) {
        viewModelScope.launch {
            Timber.d("getExpense id=%s", expenseId)
            onResult(repo.expenseById(expenseId))
        }
    }

    fun saveGoals(monthlyMin: Double, monthlyMax: Double, perCategory: Map<Long, Double>) {
        viewModelScope.launch {
            val userId = currentUserId.value ?: return@launch
            val month = LocalDate.now().toString().substring(0, 7)
            Timber.d("saveGoals month=%s", month)
            repo.saveMonthlyGoals(
                BudgetGoalEntity(userId = userId, monthlyMin = monthlyMin, monthlyMax = monthlyMax, month = month),
                perCategory.map { (categoryId, amount) ->
                    CategoryLimitEntity(userId = userId, categoryId = categoryId, limitAmount = amount, month = month)
                }
            )
            infoMessage.postValue("Goals saved")
        }
    }

    fun logout() {
        Timber.d("logout")
        session.clear()
        currentUserId.value = -1L
        currentUser.value = null
    }

    fun refreshUser() {
        val userId = currentUserId.value ?: return
        viewModelScope.launch {
            Timber.d("refreshUser id=%s", userId)
            currentUser.postValue(repo.findUserById(userId))
            badges.postValue(repo.badges(userId))
            repo.evaluateBadges(userId)
            badges.postValue(repo.badges(userId))
        }
    }

    fun rankName(): String = GameLogic.rankFromXp(currentUser.value?.xp ?: 0)
}
