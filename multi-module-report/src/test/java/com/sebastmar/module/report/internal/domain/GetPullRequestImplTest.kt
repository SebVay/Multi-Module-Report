package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.info.Module
import com.sebastmar.module.report.system.SystemWrapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class GetPullRequestImplTest {

    private val systemWrapper: SystemWrapper = mockk()
    private val getUpdatedModules: GetUpdatedModules = mockk()
    private val getPullRequest: GetPullRequest = GetPullRequestImpl(
        systemWrapper = systemWrapper,
        getUpdatedModules = getUpdatedModules,
    )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify it returns PullRequest with expected link and modules`() {
        val givenModules = listOf<Module>(mockk())

        every { systemWrapper.htmlLink() } returns "https://example.com/pr/1"
        every { getUpdatedModules() } returns givenModules

        val result = getPullRequest()

        assertEquals("https://example.com/pr/1", result.htmlLink)
        assertSame(givenModules, result.modules)
    }
}
