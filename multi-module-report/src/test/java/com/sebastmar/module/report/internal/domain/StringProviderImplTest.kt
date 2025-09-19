package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.internal.ReportStrings
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StringProviderImplTest {

    private val reportStrings: ReportStrings = mockk()

    private val stringProvider: StringProvider = StringProviderImpl(reportStrings)

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify it returns the top section`() {
        val givenTopSection = "Top Section"

        every { reportStrings.topSection } returns givenTopSection

        val result = stringProvider.topSection()

        assertEquals(givenTopSection, result)
    }

    @Test
    fun `verify it returns the bottom section`() {
        val givenBottomSection = "Bottom Section"

        every { reportStrings.bottomSection } returns givenBottomSection

        val result = stringProvider.bottomSection()

        assertEquals(givenBottomSection, result)
    }

    @Test
    fun `verify it returns the unknown module name`() {
        val givenUnknownModuleName = "Unknown !"

        every { reportStrings.unknownModuleName } returns givenUnknownModuleName

        val result = stringProvider.unknownModuleName()

        assertEquals(givenUnknownModuleName, result)
    }

    @Test
    fun `verify it returns the root module name`() {
        val givenRootModule = "Root Module"

        every { reportStrings.projectRootModuleName } returns givenRootModule

        val result = stringProvider.projectRootModuleName()

        assertEquals(givenRootModule, result)
    }
}
