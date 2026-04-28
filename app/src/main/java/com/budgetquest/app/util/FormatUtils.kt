package com.budgetquest.app.util

import java.text.DecimalFormat

/**
 * Currency formatter for ZAR with compact integer display.
 */
object FormatUtils {
    fun zar(amount: Double): String {
        val df = DecimalFormat("R #,##0.##")
        return df.format(amount)
    }
}
