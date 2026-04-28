package com.budgetquest.app.util

import java.security.MessageDigest

/**
 * Security helper for password hashing.
 */
object SecurityUtils {
    fun sha256(text: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(text.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
