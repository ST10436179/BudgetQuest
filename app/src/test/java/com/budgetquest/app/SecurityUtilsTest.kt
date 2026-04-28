package com.budgetquest.app

import com.budgetquest.app.util.SecurityUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class SecurityUtilsTest {
    @Test
    fun sha256_hashMatchesKnownValue() {
        assertEquals(
            "008c70392e3abfbd0fa47bbc2ed96aa99bd49e159727fcba0f2e6abeb3a9d601",
            SecurityUtils.sha256("Password123")
        )
    }
}
