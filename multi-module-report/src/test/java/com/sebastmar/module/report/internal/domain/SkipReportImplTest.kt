package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.internal.SkipReportKeyword
import com.sebastmar.module.report.system.SystemWrapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SkipReportImplTest {

    private val systemWrapper: SystemWrapper = mockk()
    private lateinit var skipReport: SkipReport

    private fun setUpSkipReport(skipReportKeyword: String = "") {
        skipReport = SkipReportImpl(
            systemWrapper = systemWrapper,
            skipReportKeyword = SkipReportKeyword(skipReportKeyword),
        )
    }

    @BeforeEach
    fun setUp() {
        setUpSkipReport()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify skipReport returns true when pr body contains the skip keyword`() {
        every { systemWrapper.prBody() } returns "Some description with token MAGIC_SKIP"
        setUpSkipReport("MAGIC_SKIP")

        assertTrue(skipReport())
    }

    @Test
    fun `verify skipReport returns false when pr body does not contain the skip keyword`() {
        every { systemWrapper.prBody() } returns "Regular description without special words"
        setUpSkipReport("MAGIC_SKIP")

        assertFalse(skipReport())
    }

    @Test
    fun `verify skipReport returns false when pr body is empty`() {
        every { systemWrapper.prBody() } returns ""
        setUpSkipReport("MAGIC_SKIP")

        assertFalse(skipReport())
    }

    @Test
    fun `verify skipReport is case-sensitive and returns false when casing does not match`() {
        every { systemWrapper.prBody() } returns "text contains magic_skip in lower case"
        setUpSkipReport("MAGIC_SKIP")

        assertFalse(skipReport())
    }
}
