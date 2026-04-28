package com.budgetquest.app

import com.budgetquest.app.util.SecurityUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class SecurityUtilsTest {
    @Test
    fun sha256_hashMatchesKnownValue() {
        assertEquals(
            "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f",
            SecurityUtils.sha256("Password123")
        )
    }
}
