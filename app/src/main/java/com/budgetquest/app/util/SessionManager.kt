package com.budgetquest.app.util

import android.content.Context

/**
 * SharedPreferences wrapper for persisting logged-in user id.
 */
class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("budgetquest_session", Context.MODE_PRIVATE)

    fun saveUserId(userId: Long) {
        prefs.edit().putLong(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Long = prefs.getLong(KEY_USER_ID, -1L)

    fun clear() {
        prefs.edit().remove(KEY_USER_ID).apply()
    }

    fun setDemoAutoLoginEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DEMO_AUTO_LOGIN, enabled).apply()
    }

    fun isDemoAutoLoginEnabled(): Boolean = prefs.getBoolean(KEY_DEMO_AUTO_LOGIN, true)

    companion object {
        private const val KEY_USER_ID = "userId"
        private const val KEY_DEMO_AUTO_LOGIN = "demoAutoLoginEnabled"
    }
}
