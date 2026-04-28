package com.budgetquest.app

import android.app.Application
import timber.log.Timber

/**
 * Application entry point for global setup.
 */
class BudgetQuestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
