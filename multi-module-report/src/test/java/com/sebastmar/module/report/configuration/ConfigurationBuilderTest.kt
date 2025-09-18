package com.sebastmar.module.report.configuration

import com.sebastmar.module.report.info.Module
import com.sebastmar.module.report.internal.Configuration
import com.sebastmar.module.report.internal.ReportStrings
import com.sebastmar.module.report.internal.ShouldLinkifyFiles
import com.sebastmar.module.report.internal.ShowCircleIndicators
import com.sebastmar.module.report.internal.ShowLineIndicators
import com.sebastmar.module.report.internal.SkipReportKeyword
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ConfigurationBuilderTest {

    private val builder = ConfigurationBuilder()

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify the default configuration`() {
        val givenDefaultLinkify = true
        val givenDefaultShowCircle = true
        val givenDefaultShowLine = true
        val givenDefaultSkipKeyword = "module-no-report"

        assertEquals(givenDefaultLinkify, builder.linkifyFiles)
        assertEquals(givenDefaultShowCircle, builder.showCircleIndicators)
        assertEquals(givenDefaultShowLine, builder.showLineIndicators)
        assertEquals(givenDefaultSkipKeyword, builder.skipReportKeyword)
    }

    @Test
    fun `verify it builds the default configuration`() {
        val givenDefaultLinkify = true
        val givenDefaultShowCircle = true
        val givenDefaultShowLine = true
        val givenDefaultSkipKeyword = "module-no-report"
        val givenDefaultReportStrings = ReportStringsBuilder().build()

        val result = builder.build()

        val expected = Configuration(
            shouldLinkifyFiles = ShouldLinkifyFiles(givenDefaultLinkify),
            showCircleIndicators = ShowCircleIndicators(givenDefaultShowCircle),
            showLineIndicators = ShowLineIndicators(givenDefaultShowLine),
            modulesInterceptor = NoOpModulesInterceptor,
            skipReportKeyword = SkipReportKeyword(givenDefaultSkipKeyword),
            reportStrings = givenDefaultReportStrings,
        )

        assertEquals(expected, result)
    }

    @Test
    fun `verify it builds the configuration with custom flags and strings`() {
        val givenLinkify = false
        val givenShowCircle = false
        val givenShowLine = false
        val givenSkipKeyword = "skip-this-report"

        val reportStringsTop = "Custom Top"
        val reportStringsBottomSection = "Custom Bottom section"
        val reportStringsRootModule = "Root"
        val reportStringsUnknownModule = "Misc"

        val result = builder.apply {
            linkifyFiles = givenLinkify
            showCircleIndicators = givenShowCircle
            showLineIndicators = givenShowLine
            skipReportKeyword = givenSkipKeyword
            reportStrings {
                topSection = reportStringsTop
                bottomSection = reportStringsBottomSection
                projectRootModuleName = reportStringsRootModule
                unknownModuleName = reportStringsUnknownModule
            }
        }.build()

        val expected = Configuration(
            shouldLinkifyFiles = ShouldLinkifyFiles(givenLinkify),
            showCircleIndicators = ShowCircleIndicators(givenShowCircle),
            showLineIndicators = ShowLineIndicators(givenShowLine),
            modulesInterceptor = NoOpModulesInterceptor,
            skipReportKeyword = SkipReportKeyword(givenSkipKeyword),
            reportStrings = ReportStrings(
                topSection = reportStringsTop,
                bottomSection = reportStringsBottomSection,
                projectRootModuleName = reportStringsRootModule,
                unknownModuleName = reportStringsUnknownModule,
            ),
        )

        assertEquals(expected, result)
    }

    @Test
    fun `verify it applies the provided modules interceptor`() {
        val givenModules = listOf<Module>()
        val givenInterceptedModules = listOf<Module>(mockk())

        val result = builder.apply {
            modulesInterceptor { givenInterceptedModules }
        }.build()

        val transformed = result.modulesInterceptor.intercept(givenModules)
        assertEquals(givenInterceptedModules, transformed)
    }
}
